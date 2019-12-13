<template>
  <div class="bd-box" v-loading="loading">
    <div class="general-information">
      <p class="title">General Information</p>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
        label-width="150px" class="general-form" label-position="left">
        <el-form-item label="BD Name" prop="code">
          <el-select v-model="ruleForm.code" @change="changeBDName">
            <el-option :label="v.name" :value="v.code" v-for="(v, k) in dbNameList" :key="k"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Contract Address" prop="contractName" v-if="ruleForm.code !== 'SystemBD' && ruleForm.code !== ''">
          <el-input v-model="ruleForm.contractName" placeholder="Please enter address" :maxlength="40"></el-input>
        </el-form-item>
        <el-form-item label="Function Name" prop="functionName">
          <el-select v-model="ruleForm.functionName" placeholder="Please select"
            :disabled="ruleForm.code !== 'SystemBD' ? ruleForm.contractName ? false : true: false">
            <el-option :label="v.desc" :value="v.name" v-for="(v, k) in functionNameList" :key="k"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Opeartion Address" prop="submitter">
          <el-input v-model="ruleForm.submitter" placeholder="Please enter address" :maxlength="40"></el-input>
        </el-form-item>
        <el-form-item label="Fee Currency" prop="feeCurrency">
          <el-input v-model="ruleForm.feeCurrency" :maxlength="32"></el-input>
        </el-form-item>
        <el-form-item label="Fee Max Amount" prop="feeMaxAmount">
          <el-input v-model="ruleForm.feeMaxAmount" placeholder="Please enter fee max amount" :maxlength="18"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <RegisterPolicy v-if="ruleForm.functionName === 'REGISTER_POLICY'" ref="REGISTER_POLICY"/>
    <ModifyPolicy v-if="ruleForm.functionName === 'MODIFY_POLICY'" ref="MODIFY_POLICY"/>
    <IdentitySetting v-if="ruleForm.functionName === 'IDENTITY_SETTING'" ref="IDENTITY_SETTING"/>
    <PermissionRegister v-if="ruleForm.functionName === 'PERMISSION_REGISTER'" ref="PERMISSION_REGISTER"/>
    <AuthorizePermission v-if="ruleForm.functionName === 'AUTHORIZE_PERMISSION'" ref="AUTHORIZE_PERMISSION" />
    <CancelPermission v-if="ruleForm.functionName === 'CANCEL_PERMISSION'" ref="CANCEL_PERMISSION"/>
    <KYCSetting v-if="ruleForm.functionName === 'KYC_SETTING'" ref="KYC_SETTING"/>
    <SaveAttestation v-if="ruleForm.functionName === 'SAVE_ATTESTATION'" ref="SAVE_ATTESTATION"/>
    <SystemProperty v-if="ruleForm.functionName === 'SYSTEM_PROPERTY'" ref="SYSTEM_PROPERTY"/>
    <BDSpecification v-if="ruleForm.functionName === 'BD_PUBLISH'" ref="BD_PUBLISH"/>
    <SetFeeRule v-if="ruleForm.functionName === 'SET_FEE_RULE'" ref="SET_FEE_RULE"/>
    <IdentityBDManage v-if="ruleForm.functionName === 'IDENTITY_BD_MANAGE'" ref="IDENTITY_BD_MANAGE"/>
    <div class="submit-btn">
      <p @click="validateBD">Submit</p>
    </div>
  </div>
</template>
<script>
import { BDOptionInfo, getDomainList, getPolicyList, submitBDConfig } from '@/api/storeApi';
import RegisterPolicy from './BDDetail/RegisterPolicy';
import ModifyPolicy from './BDDetail/ModifyPolicy';
import IdentitySetting from './BDDetail/IdentitySetting';
import PermissionRegister from './BDDetail/PermissionRegister';
import AuthorizePermission from './BDDetail/AuthorizePermission';
import CancelPermission from './BDDetail/CancelPermission';
import KYCSetting from './BDDetail/KYCSetting';
import SaveAttestation from './BDDetail/SaveAttestation';
import SystemProperty from './BDDetail/SystemProperty';
import BDSpecification from './BDDetail/BDSpecification';
import SetFeeRule from './BDDetail/SetFeeRule';
import IdentityBDManage from './BDDetail/IdentityBDManage';
import { notify } from '@/common/util';
import { mapGetters } from 'vuex';

export default {
  name: 'BD',
  watch: {
    'ruleForm.feeMaxAmount': {
      handler (n, o) {
        if (n.indexOf('.') !== -1) {
          let data = n.split('.');
          if (data[1].length > 8) {
            this.ruleForm.feeMaxAmount = o;
          }
        }
      },
      deep: true
    },
    'ruleForm.contractName': {
      handler (n) {
        if (!n) this.ruleForm.functionName = '';
      },
      deep: true
    }
  },
  data () {
    return {
      ruleForm: {
        code: '',
        functionName: '',
        submitter: '',
        feeCurrency: '',
        feeMaxAmount: '',
        contractName: ''
      },
      rules: {
        code: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        functionName: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ],
        submitter: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        contractName: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ]
      },
      loading: false,
      dbNameList: [],
      contractNameList: [
        {
          name: 'qewe'
        }, {
          name: '22223232' 
        }
      ],
      domainIDList: [],
      votePatternList: [
        {
          name: 'SYNC',
          id: 'SYNC'
        }, {
          name: 'ASYNC',
          id: 'ASYNC'
        }
      ],
      callbackTypeList: [
        {
          name: 'ALL',
          id: 'ALL'
        }, {
          name: 'SELF',
          id: 'SELF'
        }
      ],
      decisionTypeList: [
        {
          name: 'Full Vote',
          id: 'FULL_VOTE'
        }, {
          name: 'One Vote',
          id: 'ONE_VOTE'
        }, {
          name: 'Assign Num',
          id: 'ASSIGN_NUM'
        }
      ],
      requireAuthIDList: []
    }
  },
  computed: {
    ...mapGetters(['functionNameList'])
  },
  created () {
    this.$store.commit('changeBdMenu', this.$route.meta.menu);
    this.getOption();
    this.getDomain();
    this.getPolicy();
  },
  methods: {
    async submitBD (valid, validChild) {
      if (valid) {
        let submitData = Object.assign(validChild.ruleForm, this.ruleForm);
        delete submitData.functionName;
        if (this.ruleForm.code === 'SystemBD') delete submitData.contractName;
        let params = {
          notify: notify.error,
          data: {
            functionName: this.ruleForm.functionName,
            param: submitData
          }
        }
        let data = await submitBDConfig(params);
        console.log(data)
      }
    },
    validateBD () {
      this.$refs['ruleForm'].validate(valid => {
        let validChild = this.$refs[this.ruleForm.functionName].validateFrom();
        this.submitBD(valid && validChild.valid, validChild);
      });
    },
    changeBDName (code) {
      if (code === 'SystemBD') this.ruleForm.contractName = '';
      let filterFunction = JSON.parse(this.dbNameList.filter(v => v.code === code)[0].functions);
      this.$store.commit('changeFunctionNameList', filterFunction);
      this.ruleForm.functionName = '';
      console.log(filterFunction)
    },
    async getPolicy () {
      let data = await getPolicyList();
      this.requireAuthIDList = JSON.parse(JSON.stringify(data.data));
    },
    async getDomain () {
      let data = await getDomainList();
      this.domainIDList = JSON.parse(JSON.stringify(data.data));
    },
    async getOption () {
      this.loading = true;
      let params = {
        bdCode: ''
      }
      let data = await BDOptionInfo(params);
      this.dbNameList = JSON.parse(JSON.stringify(data.data));
      this.$store.commit('changeFunctionNameList', JSON.parse(JSON.stringify(JSON.parse(data.data[0].functions))));
      this.ruleForm.code = this.dbNameList[0].code;
      this.loading = false;
    }
  },
  components: {
    RegisterPolicy,
    ModifyPolicy,
    IdentitySetting,
    PermissionRegister,
    AuthorizePermission,
    CancelPermission,
    KYCSetting,
    SaveAttestation,
    SystemProperty,
    BDSpecification,
    SetFeeRule,
    IdentityBDManage
  }
}
</script>
<style scoped lang="scss">
.bd-box {
  width: 710px;
  background-color: #fff;
  padding: 40px 40px;
  padding-right: 100px;
  float: left;
  .general-information {
    .title {
      color: #333333;
      font-size: 18px;
      font-weight:500;
      margin-bottom: 25px;
    }
    .meta::before {
      content: '*';
      color: #F56C6C;
      margin-right: 4px;
    }
    .general-form {
      margin-top: 25px;
    }
  }
  .submit-btn {
    padding-left: 65px;
    p {
      width: 250px;
      height: 40px;
      background-color: #053C8C;
      margin: 0 auto;
      text-align: center;
      line-height: 40px;
      color: #fff;
      font-size: 18px;
      cursor: pointer;
      margin-top: 40px;
    }
  }
  .vote-pattern-tips {
    line-height: 25px;
    font-size: 12px;
    color: #333333;
    padding-left: 10px;
  }
}
</style>