<template>
  <div class="modify-policy">
    <p class="title">Special Information</p>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
      label-width="180px" class="general-form" label-position="left">
      <el-form-item label="Policy ID" prop="policyId">
        <el-select v-model="ruleForm.policyId" placeholder="Please select" @change="changePolicyId">
          <el-option :label="v.policyName" :value="v.policyId" v-for="(v, k) in policyList" :key="k"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Policy Name" prop="policyId">
        <el-input v-model="ruleForm.policyId" disabled></el-input>
      </el-form-item>
      <el-form-item label="Domain IDs" prop="domainIds">
        <el-select v-model="ruleForm.domainIds" placeholder="Please select domian IDs" multiple filterable @change="changeDomain">
          <el-option :label="v.desc" :value="v.domainId" v-for="(v, k) in domainList" :key="k"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Vote Pattern" prop="votePattern" :class="{'vote-pattern': ruleForm.votePattern}">
        <el-select v-model="ruleForm.votePattern">
          <el-option :label="v.name" :value="v.id" v-for="(v, k) in voteList" :key="k"></el-option>
        </el-select>
        <p class="vote-pattern-tips" v-if="ruleForm.votePattern === 'SYNC'">The vote is processed synchronously.</p>
        <p class="vote-pattern-tips" v-else-if="ruleForm.votePattern === 'ASYNC'">The vote is processed asynchronously.</p>
      </el-form-item>
      <el-form-item label="Callback Type" prop="callbackType">
        <el-select v-model="ruleForm.callbackType">
          <el-option :label="v.name" :value="v.id" v-for="(v, k) in callbackList" :key="k"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Decision Type" prop="decisionType">
        <el-select v-model="ruleForm.decisionType" @change="changeDecision">
          <el-option :label="v.name" :value="v.id" v-for="(v, k) in decisionList" :key="k"></el-option>
        </el-select>
      </el-form-item>
      <!-- decisionType = Assign Num -->
      <template v-if="ruleForm.decisionType === 'ASSIGN_NUM'">
        <p class="title meta">Assig Meta</p>
        <el-form-item label="Verify Num" prop="assignMeta.verifyNum">
          <el-input v-model="ruleForm.assignMeta.verifyNum" placeholder="Please enter an integer"></el-input>
        </el-form-item>
        <el-form-item label="Must Domain IDs" prop="assignMeta.mustDomainIds">
          <el-select v-model="ruleForm.assignMeta.mustDomainIds" placeholder="Please select from Domain IDs" multiple filterable>
            <el-option :label="v" :value="v" v-for="(v, k) in ruleForm.domainIds" :key="k"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Expression" prop="assignMeta.expression">
          <el-input v-model="ruleForm.assignMeta.expression" placeholder="Please fill in the expression. Only support integer、 +、-、x 、/ . as (n+1)/2"></el-input>
        </el-form-item>
      </template>
      <el-form-item label="Require Auth IDs" prop="requireAuthIds">
        <el-select v-model="ruleForm.requireAuthIds" placeholder="Please select from Domain IDs" multiple filterable>
          <el-option :label="v" :value="v" v-for="(v, k) in ruleForm.domainIds" :key="k"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { getPolicyList, getDomainList } from '@/api/storeApi';

export default {
  name: 'ModifyPolicy',
  data () {
    const validatorExpression = (rule, value, callback) => {
      if (!value) {
        callback();
      } else {
        if (/\(\d*[+|\-|*|/]{1}\d*\)[+|\-|*|/]{1}[1-9]+/.test(value)) {
          callback();
        } else {
          callback(new Error('Please enter the right format, eg: (n+1)/2.'));
        }
      }
    }
    return {
      ruleForm: {
        policyId: '',
        policyName: '',
        domainIds: [],
        votePattern: '',
        callbackType: '',
        decisionType: '',
        requireAuthIds: [],
        assignMeta: {
          verifyNum: '',
          mustDomainIds: [],
          expression: ''
        }
      },
      policyList: [],
      domainList: [],
      voteList: [
        {
          name: 'Synchronously',
          id: 'SYNC'
        }, {
          name: 'Aynchronously',
          id: 'ASYNC'
        }
      ],
      callbackList: [
        {
          name: 'All Received',
          id: 'ALL'
        }, {
          name: 'Only Self Received',
          id: 'SELF'
        }
      ],
      decisionList: [
        {
          name: 'Full Vote',
          id: 'FULL_VOTE'
        }, {
          name: 'One Vote',
          id: 'ONE_VOTE'
        }, {
          name: 'Assign Num Vote',
          id: 'ASSIGN_NUM'
        }
      ],
      requireList: [],
      rules: {
        policyName: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ],
        domainIds: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ],
        votePattern: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ],
        callbackType: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ],
        decisionType: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ],
        requireAuthIds: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ],
        'assignMeta.verifyNum': [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        'assignMeta.mustDomainIds': [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ],
        'assignMeta.expression': [
          { validator: validatorExpression, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    changeDecision (name) {
      if (name !== 'ASSIGN_NUM') {
        this.ruleForm.assignMeta.verifyNum = '';
        this.ruleForm.assignMeta.mustDomainIds = [];
        this.ruleForm.assignMeta.expression = '';
      }
    },
    changePolicyId (id) {
      let filterArr = this.policyList.filter(v => v.policyId === id);
      this.ruleForm = Object.assign(this.ruleForm, filterArr[0]);
    },
    changeDomain () {
      if (this.ruleForm.domainIds.length === 0) {
        this.ruleForm.requireAuthIds = [];
        this.ruleForm.mustDomainIds = [];
      }
    },
    validateForm () {
      let validCode = {
        valid: false,
        ruleForm: this.ruleForm
      };
      this.$refs['ruleForm'].validate(valid => {
        validCode.valid = valid;
      });
      return validCode;
    },
    async getPolicy () {
      let data = await getPolicyList();
      this.policyList = JSON.parse(JSON.stringify(data.data));
      this.requireList = JSON.parse(JSON.stringify(data.data));
    },
    async getDomain () {
      let data = await getDomainList();
      this.domainList = JSON.parse(JSON.stringify(data.data));
    }
  },
  created () {
    this.getPolicy();
    this.getDomain();
  }
}
</script>
<style scoped lang="scss">
.modify-policy {
  margin-top: 40px;
  overflow: hidden;
  .title {
    color: #333333;
    font-size: 18px;
    font-weight: 500;
    margin-bottom: 25px;
  }
  .meta::before {
    content: '*';
    color: #F56C6C;
    margin-right: 4px;
  }
  .vote-pattern {
    margin-bottom: 0px !important;
  }
  .vote-pattern-tips {
    line-height: 25px;
    font-size: 12px;
    color: #333333;
    padding-left: 10px;
  }
}
</style>