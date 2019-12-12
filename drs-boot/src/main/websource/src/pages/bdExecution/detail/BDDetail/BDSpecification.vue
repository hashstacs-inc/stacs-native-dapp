<template>
  <div class="BD-specification">
    <p class="title">Special Information</p>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
      label-width="150px" class="general-form" label-position="left">
      <el-form-item label="BD Name" prop="BDName">
        <el-input v-model="ruleForm.BDName" :maxlength="64"></el-input>
      </el-form-item>
      <el-form-item label="BD Code" prop="BDCode">
        <el-input v-model="ruleForm.BDCode" placeholder="Please enter" :maxlength="32"></el-input>
      </el-form-item>
      <el-form-item label="BD Type" prop="BDType">
        <el-select v-model="ruleForm.BDType" @change="changeBDType">
          <el-option :label="v.name" :value="v.name" v-for="(v, k) in BDTypeList" :key="k"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Descision" prop="descision">
        <el-input v-model="ruleForm.descision" :maxlength="1024"></el-input>
      </el-form-item>
      <el-form-item label="Init Permission" prop="initPermission" ref="initPermission">
        <el-select v-model="ruleForm.initPermission">
          <el-option :label="v.permissionName" :value="v.permissionIndex" v-for="(v, k) in initPermissionList" :key="k"></el-option>
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
        <el-form :model="ruleForm" v-for="(v, k) in ruleForm.newFunction" :key="k" label-width="150px" 
          ref="newFunctionFrom" class="functio-from-box">
          <div class="delete-new-from" v-if="v.locking" @click="deleteNew(k)">Delete</div>
          <el-form-item label="Type" :prop="'newFunction.' + k + '.type'"
            :rules="{ required: true, message: 'This filed is required', trigger: 'blur' }">
            <el-select v-model="v.type" v-if="!v.locking" @change="changeNewType">
              <el-option :label="v.name" :value="v.name" v-for="(v, k) in v.typeList" :key="k"></el-option>
            </el-select>
            <el-input v-model="v.type" v-else :disabled="v.locking"></el-input>
          </el-form-item>
          <el-form-item label="Function Name" :prop="'newFunction.' + k + '.functionName'"
            :rules="{ required: true, message: 'This filed is required', trigger: 'blur' }">
            <el-select v-model="v.functionName" placeholder="Please select" v-if="v.type === 'System Action' && !v.locking">
              <el-option :label="v.desc" :value="v.name" v-for="(v, k) in functionNameList" :key="k"></el-option>
            </el-select>
            <el-input v-model="v.functionName" :maxlength="256" v-else :disabled="v.locking"></el-input>
          </el-form-item>
          <el-form-item label="Function Descision" :prop="'newFunction.' + k + '.functionDescision'">
            <el-input v-model="v.functionDescision" :maxlength="256" :disabled="v.type === 'System Action' || v.locking"></el-input>
          </el-form-item>
          <el-form-item label="Method Sign" :prop="'newFunction.' + k + '.methodSign'"
            :rules="{ required: true, message: 'This filed is required', trigger: 'blur' }">
            <el-input v-model="v.methodSign" :maxlength="256" :disabled="v.type === 'System Action' || v.locking"></el-input>
          </el-form-item>
          <el-form-item label="Init Permission" :prop="'newFunction.' + k + '.initPermission'"
            :rules="{ required: true, message: 'This filed is required', trigger: 'blur' }">
            <el-select v-model="v.initPermission" v-if="v.type === 'System Action' && !v.locking">
              <el-option :label="v.permissionName" :value="v.permissionIndex" v-for="(v, k) in initPermissionList" :key="k"></el-option>
            </el-select>
            <el-input v-model="v.initPermission" :maxlength="256" v-else :disabled="v.locking"></el-input>
          </el-form-item>
          <el-form-item label="Exec Policy" :prop="'newFunction.' + k + '.execPolicy'"
            :rules="{ required: true, message: 'This filed is required', trigger: 'blur' }">
            <el-select v-model="v.execPolicy" v-if="v.type === 'System Action' && !v.locking">
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
        BDName: '',
        BDCode: '',
        BDType: '',
        descision: '',
        initPermission: '',
        initPolicy: '',
        BDVersion: '',
        newFunction: [
          {
            type: 'System Action',
            typeList: [
              { name: 'System Action'},
              { name: 'Contract'}
            ],
            functionName: '',
            functionNameList: [{
                "desc":"Permission撤销授权",
                "execPermission":"RS",
                "execPolicy":"CANCEL_PERMISSION",
                "methodSign":"CANCEL_PERMISSION",
                "name":"CANCEL_PERMISSION",
                "type":"SyetemAction"
            }, {
                "desc":"注册policy",
                "execPermission":"RS",
                "execPolicy":"REGISTER_POLICY",
                "methodSign":"REGISTER_POLICY",
                "name":"REGISTER_POLICY",
                "type":"SyetemAction"
            }],
            functionDescision: '',
            methodSign: '',
            initPermission: '',
            execPolicy: '',
            locking: false
          }
        ]
      },
      rules: {
        BDName: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        BDCode: [
          { required: true, message: 'This filed is required', trigger: 'blur' },
          { validator: BDCodeValidator, trigger: 'change' }
        ],
        BDType: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        initPermission: [
          { required: false, message: 'This filed is required', trigger: 'blur' }
        ],
        initPolicy: [
          { required: false, message: 'This filed is required', trigger: 'blur' }
        ],
        BDVersion: [
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
    validateFrom () {
      let validCode = {
        valid: false,
        ruleForm: this.ruleForm
      };
      this.$refs['ruleForm'].validate(valid => {
        validCode.valid = valid;
      });
      return validCode;
    },
    deleteNew (k) {
      this.$confirm('Continue to delete？', 'Tips', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Back'
      }).then(() => {
        this.ruleForm.newFunction.splice(k, 1);
      });
    },
    changeNewType () {
      this.ruleForm.newFunction.forEach((v, k) => {
        if (!v.locking) {
          this.$refs['newFunctionFrom'][k].clearValidate();
        }
      });
    },
    addNewFunction (from) {
      this.ruleForm.newFunction.forEach((v, k) => {
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
              copyV.type = 'System Action';
              this.ruleForm.newFunction.push(copyV);
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