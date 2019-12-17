<template>
  <div class="bd-box" v-loading="loading">
    <div class="general-information">
      <p class="title">General Information</p>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
        label-width="150px" class="general-form" label-position="left">
        <el-form-item label="BD Name" prop="bdCode">
          <el-select v-model="ruleForm.bdCode" @change="changeBDName">
            <el-option :label="v.name" :value="v.code" v-for="(v, k) in dbNameList" :key="k"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Contract Address" prop="contractName" v-if="ruleForm.bdCode !== 'SystemBD' && ruleForm.bdCode !== ''">
          <el-select v-model="ruleForm.contractName">
            <el-option :label="v.name" :value="v.address" v-for="(v, k) in contractList" :key="k" placeholder="Please select address"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Function Name" prop="functionName">
          <el-select v-model="ruleForm.functionName" placeholder="Please select" @change="changeFunction"
            :disabled="ruleForm.bdCode !== 'SystemBD' ? ruleForm.contractName ? false : true: false">
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
    <!-- 根据选择的functionName展示对应的表单组件 -->
    <div v-if="ruleForm.bdCode === 'SystemBD'">
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
    </div>
    <!-- 非系统BD展示 -->
    <div v-else-if="Object.keys(contractSubmit).length > 0" class="general-information" style="margin-top:40px;">
      <p class="title">Special Information</p>
      <el-form ref="contractFrom" 
        label-width="150px" class="general-form" label-position="left">
        <el-form-item :label="k" :prop="k" v-for="(v, k) in contractParamData" :key="k">
          <el-input v-model="contractSubmit[k]" :placeholder="'Please enter new identity ' + contractParamData[k]" :maxlength="40"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <div class="submit-btn">
      <p @click="validateBD(true)">Submit</p>
    </div>
    <!-- 签名弹窗 -->
    <el-dialog
      title="System"
      :visible.sync="signVisible"
      width="600px">
      <el-form ref="contractFrom" :model="signData"
        label-width="150px" class="general-form" label-position="left">
        <el-form-item label="Signature Data" prop="signatureData" class="sign-item">
          <el-input v-model="signData.sign" placeholder="Please enter signature value" :disabled="true"></el-input>
          <div class="copy-sign" v-clipboard:copy="signData.sign" v-clipboard:success="onCopySign">
            <img src="../../../assets/img/copy.png" alt="logo">
          </div>
        </el-form-item>
        <el-form-item label="Signature value" prop="submitterSign"
          :rules="{ required: true, message: 'This filed is required', trigger: 'blur' }">
          <el-input v-model="signData.submitterSign" placeholder="Please enter signature value"></el-input>
        </el-form-item>
        <el-form-item label="" class="sign-icon-box">
          <div class="sign-icon" @click="goSingLink">
            <img src="../../../assets/img/sign-icon.png" alt="logo">
          </div>          
        </el-form-item>
        <div class="sign-tips">
          <img src="../../../assets/img/tips.png" alt="logo" class="tips-icon">
          <p class="sign-title">Note</p>
          <p class="sign-doc">For the security of your wallet, copy the value of Signature Data to sign other wallets, and then fill the returned signature value into the Signature Value box to complete the signature.</p>
        </div>
      </el-form>
      <p slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirmSign">Confirm</el-button>
        <el-button @click="signVisible = false">Return</el-button>
      </p>
    </el-dialog>
  </div>
</template>
<script>
import { BDOptionInfo, getDomainList, submitBDConfig, 
  getContractList, getContractParam, getSignValue } from '@/api/storeApi';
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
import { Loading } from 'element-ui';

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
      // 通用参数表单
      ruleForm: {
        bdCode: 'SystemBD',
        functionName: '',
        submitter: '',
        feeCurrency: '',
        feeMaxAmount: '',
        contractName: ''
      },
      // 通用参数表单效验规则
      rules: {
        bdCode: [
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
      // BD列表
      dbNameList: [],
      // domain列表
      domainIDList: [],
      // 写死
      votePatternList: [
        {
          name: 'SYNC',
          id: 'SYNC'
        }, {
          name: 'ASYNC',
          id: 'ASYNC'
        }
      ],
      // 写死
      callbackTypeList: [
        {
          name: 'ALL',
          id: 'ALL'
        }, {
          name: 'SELF',
          id: 'SELF'
        }
      ],
      // 写死
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
      // 合约列表
      contractList: [],
      // 自定义参数
      contractParamData: {},
      contractSubmit: {},
      signVisible: false,
      // 签名参数
      signData: {
        txId: '',
        sign: '',
        submitterSign: ''
      }
    }
  },
  computed: {
    ...mapGetters(['functionNameList'])
  },
  created () {
    // 控制左边菜单active
    this.$store.commit('changeBdMenu', this.$route.meta.menu);
    this.getOption();
    this.getDomain();
    this.getContract();
  },
  methods: {
    // 打开签名页面
    goSingLink () {
      window.open('https://www.myetherwallet.com/access-my-wallet', '_blank');
    },
    // 复制待签名字段成功
    onCopySign () {
      this.$notify.success({message: 'Operation Success'});
    },
    // 提交签名内容
    confirmSign () {
      // 效验表单
      this.$refs['contractFrom'].validateField('submitterSign', error => {
        if (!error) {
          // 提交BD
          this.validateBD(false);
        }
      });
    },
    // 改变functionName
    async changeFunction (name) {
      // 选择非系统BD
      if (this.ruleForm.bdCode !== 'SystemBD') {
        let currentFunction = this.functionNameList.filter(v => name === v.name);
        let params = {
          data: {
            contractAddress: this.ruleForm.contractName,
            methodSign: currentFunction[0].methodSign
          }
        }
        // 获取需展示的key，value
        let data = await getContractParam(params);
        this.contractParamData = JSON.parse(JSON.stringify(data.data));
        this.contractSubmit = JSON.parse(JSON.stringify(data.data));
        for (let i in this.contractSubmit) {
          this.contractSubmit[i] = '';
        }
      }
    },
    // 获取合约列表
    async getContract () {
      let data = await getContractList();
      this.contractList = JSON.parse(JSON.stringify(data.data));
    },
    // 提交DB
    async submitBD (valid, validChild, showDialog) {
      // 表单效验通过
      if (valid) {
        let submitData = Object.assign(validChild.ruleForm, this.ruleForm);
        delete submitData.functionName;
        if (this.ruleForm.bdCode === 'SystemBD') delete submitData.contractName;
        let params = {
          notify: notify.error,
          data: {
            functionName: this.ruleForm.functionName,
            param: submitData
          },
          slient: true
        }
        if (showDialog) {
          // 获取签名值
          let sign = await getSignValue(params);
          if (sign.code === '000000') {
            this.signData = JSON.parse(JSON.stringify(sign.data));
            // 打开签名dialog
            this.signVisible = true;
          }
        } else {
          let loadingInstance = Loading.service();
          // 合并签名字段
          params.data.param = Object.assign(submitData, this.signData);
          submitData.submitterSign = this.signData.submitterSign;
          submitData.txId = this.signData.txId;
          params.notify = notify.any;
          // 提交BD内容
          let subResult = await submitBDConfig(params);
          loadingInstance.close();
          if (subResult.code === '000000') {
            this.signVisible = false;
          }
        }
      }
    },
    // 效验BD表单
    validateBD (flag) {
      this.$refs['ruleForm'].validate(async valid => {
        // 选择系统BD 还需要效验对应的子表单
        if (this.ruleForm.bdCode === 'SystemBD') {
          let validChild = this.$refs[this.ruleForm.functionName].validateFrom();
          this.submitBD(valid && validChild.valid, validChild, flag);
        } else {
          // 非系统BD
          let args = Object.assign({}, { args: this.contractSubmit });
          this.submitBD(valid, { ruleForm: args }, flag);
        }
      });
    },
    // 改变BDName
    changeBDName (code) {
      if (code === 'SystemBD') this.ruleForm.contractName = ''; this.contractParamData = {}; this.contractSubmit = {};
      let filterFunction = this.dbNameList.filter(v => v.code === code)[0].functions;
      // 把对应的functionName存入store，多组件需用
      this.$store.commit('changeFunctionNameList', filterFunction);
      this.ruleForm.functionName = '';
    },
    // 获取Domain
    async getDomain () {
      let data = await getDomainList();
      this.domainIDList = JSON.parse(JSON.stringify(data.data));
    },
    // 获取BD列表
    async getOption () {
      this.loading = true;
      let params = {
        bdCode: '',
        slient: true
      }
      let data = await BDOptionInfo(params);
      if (data.code === '000000' && data.data) {
        this.dbNameList = JSON.parse(JSON.stringify(data.data));
        // 默认第一个是系统BD
        this.$store.commit('changeFunctionNameList', JSON.parse(JSON.stringify(data.data[0].functions)));
        this.ruleForm.bdCode = this.dbNameList[0].code;
      }
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
  .sign-item {
    position: relative;
    .copy-sign {
      position: absolute;
      right: 10px;
      top: 0px;
      cursor: pointer;
      img {
        width: 14px;
        height: 14px;
      }
    }
  }
  .sign-icon-box {
    margin-bottom: 60px;
    .sign-icon {
      width: 40px;
      height: 40px;
      cursor: pointer;
      img {
        width: 100%;
        height: 100%;
      }
    }
  }
  .sign-tips {
    width: 400px;
    margin: 0 auto;
    background-color: #F1FFF5;
    height: 150px;
    position: relative;
    padding: 30px 14px 0 14px;
    .tips-icon {
      width: 48px;
      height: 48px;
      position: absolute;
      left: 50%;
      top: -50%;
      transform: translate(-50%, 100%);
    }
    .sign-title {
      margin-bottom: 10px;
      color: #333333;
      font-size: 12px;
    }
    .sign-doc {
      color: #66AE79;
      font-size: 14px;
      word-wrap: break-word;
      word-break: normal;
      text-align: left;
    }
  }
}
</style>
<style lang="scss">
.bd-box {
  .el-form-item__content {
    width: 400px;
    .el-input__inner {
      padding-right: 30px;
    }
  }
}

</style>