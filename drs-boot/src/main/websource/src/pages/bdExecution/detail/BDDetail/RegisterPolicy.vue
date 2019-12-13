<template>
  <div class="register-policy">
    <p class="title">Special Information</p>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
      label-width="150px" class="general-form" label-position="left">
      <el-form-item label="Policy Id" prop="policyId">
        <el-input v-model="ruleForm.policyId" placeholder="Please enter an Policy Id"></el-input>
      </el-form-item>
      <el-form-item label="Policy Name" prop="policyName">
        <el-input v-model="ruleForm.policyName" placeholder="Please enter an Policy Name"></el-input>
      </el-form-item>
      <el-form-item label="Domain IDs" prop="domainIds">
        <el-select v-model="ruleForm.domainIds" placeholder="Please select domian IDs" multiple filterable @change="changeDomain">
          <el-option :label="v.domainId" :value="v.domainId" v-for="(v, k) in domainList" :key="k"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Vote Pattern" prop="votePattern" class="vote-pattern">
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
        <el-select v-model="ruleForm.decisionType">
          <el-option :label="v.name" :value="v.id" v-for="(v, k) in decisionList" :key="k"></el-option>
        </el-select>
      </el-form-item>
      <!-- 投票类型选择Assign Num时 -->
      <template v-if="ruleForm.decisionType === 'ASSIGN_NUM'">
        <p class="title meta">Assig Meta</p>
        <el-form-item label="Verify Num" prop="verifyNum">
          <el-input v-model="ruleForm.verifyNum" placeholder="Please enter an integer"></el-input>
        </el-form-item>
        <el-form-item label="Must Domain IDs" prop="mustDomainIds">
          <el-select v-model="ruleForm.mustDomainIds" placeholder="Please select from Domain IDs" multiple filterable>
            <el-option :label="v" :value="v" v-for="(v, k) in ruleForm.domainIds" :key="k"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Expression" prop="expression">
          <el-input v-model="ruleForm.expression" placeholder="Please fill in the expression. Only support integer、 +、-、x 、/ . as (n+1)/2"></el-input>
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
import { getDomainList } from '@/api/storeApi';

export default {
  name: 'RegisterPolicy',
  data () {
    return {
      ruleForm: {
        policyId: '',
        policyName: '',
        domainIds: [],
        votePattern: 'SYNC',
        callbackType: 'ALL',
        decisionType: 'FULL_VOTE',
        requireAuthIds: [],
        verifyNum: '',
        mustDomainIds: [],
        expression: ''
      },
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
      rules: {
        policyName: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
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
        verifyNum: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        mustDomainIds: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ],
        policyId: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    changeDomain () {
      if (this.ruleForm.domainIds.length === 0) {
        this.ruleForm.requireAuthIds = [];
        this.ruleForm.mustDomainIds = [];
      }
    },
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
    async getDomain () {
      let data = await getDomainList();
      this.domainList = JSON.parse(JSON.stringify(data.data));
    }
  },
  created () {
    this.getDomain();
  }
}
</script>
<style scoped lang="scss">
.register-policy {
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