<template>
  <div class="BD-specification">
    <p class="title">Special Information</p>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
      label-width="150px" class="general-form" label-position="left">
      <el-form-item label="BD Name" prop="name">
        <el-input v-model="ruleForm.name" :maxlength="64"></el-input>
      </el-form-item>
      <el-form-item label="BD Code" prop="code">
        <el-input v-model="ruleForm.code" placeholder="Please enter" :maxlength="32"></el-input>
      </el-form-item>
      <el-form-item label="BD Type" prop="bdType">
        <el-select v-model="ruleForm.bdType" @change="changeBDType">
          <el-option :label="v.name" :value="v.name" v-for="(v, k) in BDTypeList" :key="k"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Descision" prop="desc">
        <el-input v-model="ruleForm.desc" :maxlength="1024"></el-input>
      </el-form-item>
      <el-form-item label="Init Permission" prop="initPermission" ref="initPermission">
        <el-select v-model="ruleForm.initPermission">
          <el-option :label="v.permissionName" :value="v.permissionName" v-for="(v, k) in initPermissionList" :key="k"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Init Policy" prop="initPolicy" ref="initPolicy">
        <el-select v-model="ruleForm.initPolicy">
          <el-option :label="v.policyName" :value="v.policyId" v-for="(v, k) in initPolicyList" :key="k"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="BD Version" prop="BDVersion">
        <el-input v-model="ruleForm.BDVersion" :maxlength="4"></el-input>
      </el-form-item>
      <div class="new-function">
        <el-form :model="ruleForm" v-for="(v, k) in ruleForm.functions" :key="k" label-width="150px" 
          ref="newFunctionFrom" class="functio-from-box">
          <div class="delete-new-from" v-if="v.locking" @click="deleteNew(k)">Delete</div>
          <el-form-item label="Type" :prop="'functions.' + k + '.type'"
            :rules="{ required: true, message: 'This filed is required', trigger: 'change' }">
            <el-select v-model="v.type" v-if="!v.locking" @change="changeNewType">
              <el-option :label="v.name" :value="v.name" v-for="(v, k) in v.typeList" :key="k"></el-option>
            </el-select>
            <el-input v-model="v.type" v-else :disabled="v.locking"></el-input>
          </el-form-item>
          <el-form-item label="Function Name" :prop="'functions.' + k + '.name'"
            :rules="{ required: true, message: 'This filed is required' }">
            <el-select v-model="v.name" placeholder="Please select" v-if="v.type === 'SystemAction' && !v.locking" @change="changeAddFunction">
              <el-option :label="v.desc" :value="v.name" v-for="(v, k) in functionNameList" :key="k"></el-option>
            </el-select>
            <el-input v-model="v.name" :maxlength="256" v-else :disabled="v.locking"></el-input>
          </el-form-item>
          <el-form-item label="Function Descision" :prop="'functions.' + k + '.desc'">
            <el-input v-model="v.desc" :maxlength="256" :disabled="v.type === 'SystemAction' || v.locking"></el-input>
          </el-form-item>
          <el-form-item label="Method Sign" :prop="'functions.' + k + '.methodSign'"
            :rules="{ required: true, message: 'This filed is required' }">
            <el-input v-model="v.methodSign" :maxlength="256" :disabled="v.type === 'SystemAction' || v.locking"></el-input>
          </el-form-item>
          <el-form-item label="Init Permission" :prop="'functions.' + k + '.execPermission'"
            :rules="{ required: true, message: 'This filed is required' }">
            <el-select v-model="v.execPermission" v-if="v.type === 'SystemAction' && !v.locking">
              <el-option :label="v.permissionName" :value="v.permissionIndex" v-for="(v, k) in initPermissionList" :key="k"></el-option>
            </el-select>
            <el-input v-model="v.execPermission" :maxlength="256" v-else :disabled="v.locking"></el-input>
          </el-form-item>
          <el-form-item label="Exec Policy" :prop="'functions.' + k + '.execPolicy'"
            :rules="{ required: true, message: 'This filed is required' }">
            <el-select v-model="v.execPolicy" v-if="v.type === 'SystemAction' && !v.locking">
              <el-option :label="v.policyName" :value="v.policyId" v-for="(v, k) in initPolicyList" :key="k"></el-option>
            </el-select>
            <el-input v-model="v.execPolicy" :maxlength="256" v-else :disabled="v.locking"></el-input>
          </el-form-item>
        </el-form>
        <p class="add-new-btn" @click="addNewFunction('newFunctionFrom')">
          <span class="symbol">+</span>
          <span>Add New Function Name</span>
        </p>
      </div>
    </el-form>
  </div>
</template>
<script>
import { getPermissionList, getPolicyList } from '@/api/storeApi';
import { mapGetters } from 'vuex';

export default {
  name: 'BDSpecification',
  data () {
    const BDCodeValidator = (rule, value, callback) => {
      if (/^[a-zA-Z0-9]*(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*$/.test(value)) {
        callback();
      } else {
        callback(new Error('Please enter letters and numbers'));
      }
    }
    return {
      ruleForm: {
        name: '',
        code: '',
        bdType: '',
        desc: '',
        initPermission: '',
        initPolicy: '',
        BDVersion: '',
        functions: [
          {
            type: 'SystemAction',
            typeList: [
              { name: 'SystemAction' },
              { name: 'Contract' },
              { name: 'ContractQuery' }
            ],
            name: '',
            desc: '',
            methodSign: '',
            execPermission: '',
            execPolicy: '',
            locking: false
          }
        ]
      },
      rules: {
        name: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        code: [
          { required: true, message: 'This filed is required', trigger: 'blur' },
          { validator: BDCodeValidator, trigger: 'change' }
        ],
        bdType: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ],
        initPermission: [
          { required: false, message: 'This filed is required', trigger: 'change' }
        ],
        initPolicy: [
          { required: false, message: 'This filed is required', trigger: 'change' }
        ],
        BDVersion: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        execPermission: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ]
      },
      BDTypeList: [
        {
          name: 'System'
        }, {
          name: 'Contract'
        }, {
          name: 'Assets'
        }
      ],
      initPermissionList: [],
      initPolicyList: []
    }
  },
  methods: {
    changeAddFunction (name) {
      let currentFunction = this.functionNameList.filter(v => name === v.name);
      let lockingFunction = this.ruleForm.functions.filter(v => !v.locking);
      lockingFunction[0] = Object.assign(lockingFunction[0], currentFunction[0]);
    },
    validateFrom () {
      let ruleFormCopy = Object.assign({}, this.ruleForm);
      let locked = ruleFormCopy.functions.filter(v => v.locking);
      ruleFormCopy.functions = locked;
      let validCode = {
        valid: false,
        ruleForm: ruleFormCopy
      };
      this.$refs['ruleForm'].validate(valid => {
        if (locked.length === 0) {
          this.$refs['newFunctionFrom'][0].validate(validNew => {
            if (valid && validNew) {
              validCode.valid = true;
            }
          });
        } else {
          validCode.valid = valid;
        }
      });
      return validCode;
    },
    deleteNew (k) {
      this.$confirm('Continue to delete？', 'Tips', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Back'
      }).then(() => {
        this.ruleForm.functions.splice(k, 1);
      });
    },
    changeNewType () {
      this.ruleForm.functions.forEach((v, k) => {
        if (!v.locking) {
          this.$refs['newFunctionFrom'][k].clearValidate();
        }
      });
    },
    addNewFunction (from) {
      this.ruleForm.functions.forEach((v, k) => {
        if (!v.locking) {
          this.$refs[from][k].validate(valid => {
            if (valid) {
              let copyV = JSON.parse(JSON.stringify(v));
              v.locking = true;
              copyV.locking = false;
              for(let i in copyV) {
                if (i !== 'typeList' && i !== 'functionNameList' && i !== 'locking') {
                  copyV[i] = '';
                }
              }
              copyV.type = 'SystemAction';
              this.ruleForm.functions.push(copyV);
            }
          });
        }
      });
    },
    changeBDType (v) {
      if (v !== 'System') {
        this.rules.initPermission[0].required = true;
        this.rules.initPolicy[0].required = true;
      } else {
        this.rules.initPermission[0].required = false;
        this.rules.initPolicy[0].required = false;
        this.$refs['initPermission'].clearValidate();
        this.$refs['initPolicy'].clearValidate();
      }
    },
    async getPermission () {
      let data = await getPermissionList();
      this.initPermissionList = JSON.parse(JSON.stringify(data.data));
    },
    async getPolicyList () {
      let data = await getPolicyList();
      this.initPolicyList = JSON.parse(JSON.stringify(data.data));
    }
  },
  computed: {
    ...mapGetters(['functionNameList'])
  },
  created () {
    this.getPermission();
    this.getPolicyList();
  }
}
</script>
<style scoped lang="scss">
.BD-specification {
  margin-top: 40px;
  .title {
    color: #333333;
    font-size: 18px;
    font-weight: 500;
    margin-bottom: 25px;
  }
  .new-function {
    width: 680px;
    transform: translateX(-25px);
    text-align: center;
    .add-new-btn {
      color: #2146A9;
      font-size: 14px;
      cursor: pointer;
      padding-left: 50px;
      display: inline-block;
      .symbol {
        color: #2146A9;
        font-size: 22px;
        margin-right: 10px;
      }
    }
    .functio-from-box {
      margin-bottom: 20px;
      width: 680px;
      padding: 20px 85px 10px 25px;
      background-color: #F9FCFF;
      position: relative;
      .delete-new-from {
        position: absolute;
        right: 10px;
        top: 10px;
        padding: 2px 6px;
        background-color: #DC7171;
        color: #fff;
        font-size: 14px;
        cursor: pointer;
      }
    }
  }
}
</style>