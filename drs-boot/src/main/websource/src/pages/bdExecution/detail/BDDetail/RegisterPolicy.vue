<template>
  <div class="register-policy">
    <p class="title">Special Information</p>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
      label-width="150px" class="general-form" label-position="left">
      <el-form-item label="Policy Name" prop="policyName">
        <el-select v-model="ruleForm.policyName" placeholder="Please select">
          <el-option :label="v.policyName" :value="v.policyId" v-for="(v, k) in policyList" :key="k"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Domain IDs" prop="domainIDs">
        <el-select v-model="ruleForm.domainIDs" placeholder="Please select domian IDs" multiple filterable>
          <el-option :label="v.desc" :value="v.domainId" v-for="(v, k) in domainList" :key="k"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Vote Pattern" prop="votePattern" class="vote-pattern">
        <el-select v-model="ruleForm.votePattern">
          <el-option :label="v.name" :value="v.name" v-for="(v, k) in voteList" :key="k"></el-option>
        </el-select>
        <p class="vote-pattern-tips" v-if="ruleForm.votePattern === 'Synchronously'">vote is processed synchronously.</p>
        <p class="vote-pattern-tips" v-else>vote is processed asynchronously.</p>
      </el-form-item>
      <el-form-item label="Callback Type" prop="callbackType">
        <el-select v-model="ruleForm.callbackType">
          <el-option :label="v.name" :value="v.name" v-for="(v, k) in callbackList" :key="k"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Decision Type" prop="decisionType">
        <el-select v-model="ruleForm.decisionType">
          <el-option :label="v.name" :value="v.name" v-for="(v, k) in decisionList" :key="k"></el-option>
        </el-select>
      </el-form-item>
      <!-- 投票类型选择Assign Num时 -->
      <template v-if="ruleForm.decisionType === 'Assign Num Vote'">
        <p class="title meta">Assig Meta</p>
        <el-form-item label="Verify Num" prop="verifyNum">
          <el-input v-model="ruleForm.verifyNum" placeholder="Please enter an integer"></el-input>
        </el-form-item>
        <el-form-item label="Must Domain IDs" prop="mustDomainIDs">
          <!-- <el-select v-model="ruleForm.mustDomainIDs" placeholder="Please select from Domain IDs" multiple filterable>
            <el-option :label="v.name" :value="v.name" v-for="(v, k) in mustList" :key="k"></el-option>
          </el-select> -->
          <el-input v-model="ruleForm.mustDomainIDs" placeholder="Please enter an Domain IDs"></el-input>
        </el-form-item>
        <el-form-item label="Expression" prop="expression">
          <el-input v-model="ruleForm.expression" placeholder="Please fill in the expression. Only support integer、 +、-、x 、/ . as (n+1)/2"></el-input>
        </el-form-item>
      </template>
      <el-form-item label="Require Auth IDs" prop="requireAuthIDs">
        <el-select v-model="ruleForm.requireAuthIDs" placeholder="Please select from Domain IDs" multiple filterable>
          <el-option :label="v.policyName" :value="v.policyId" v-for="(v, k) in requireList" :key="k"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { getPolicyList, getDomainList } from '@/api/storeApi';

export default {
  name: 'RegisterPolicy',
  data () {
    return {
      ruleForm: {
        policyName: '',
        domainIDs: '',
        votePattern: 'Synchronously',
        callbackType: 'All Received',
        decisionType: 'Full Vote',
        requireAuthIDs: '',
        verifyNum: '',
        mustDomainIDs: '',
        expression: ''
      },
      policyList: [],
      domainList: [],
      voteList: [
        {
          name: 'Synchronously'
        }, {
          name: 'Aynchronously'
        }
      ],
      callbackList: [
        {
          name: 'All Received'
        }, {
          name: 'Only Self Received'
        }
      ],
      decisionList: [
        {
          name: 'Full Vote'
        }, {
          name: 'One Vote'
        }, {
          name: 'Assign Num Vote'
        }
      ],
      requireList: [],
      mustList: [
        {
          name: '11111'
        }, {
          name: '22222'
        }
      ],
      rules: {
        policyName: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ],
        domainIDs: [
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
        requireAuthIDs: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ],
        verifyNum: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        mustDomainIDs: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ]
      }
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