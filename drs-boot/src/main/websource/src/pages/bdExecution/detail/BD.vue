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
    <!-- Display according to the selected FunctioNname -->
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
    <!-- No SystemBD Show -->
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
    <!-- sign -->
    <el-dialog
      title="System"
      :visible.sync="signVisible"
      width="600px">
      <el-form ref="contractFrom" :model="signData"
        label-width="150px" class="general-form" label-position="left">
        <!-- <el-form-item label="Signature Data" prop="signatureData" class="sign-item">
          <el-input v-model="signData.sign" placeholder="Please enter signature value" :disabled="true"></el-input>
          <div class="copy-sign" v-clipboard:copy="signData.sign" v-clipboard:success="onCopySign">
            <img src="../../../assets/img/copy.png" alt="logo">
          </div>
        </el-form-item> -->
        <el-form-item label="Private Key" prop="privateKey"
          :rules="{ required: true, message: 'This filed is required', trigger: 'blur' }">
          <el-input v-model="signData.privateKey" placeholder="Please enter your private key" type="textarea" :rows="3"></el-input>
        </el-form-item>
        <!-- <el-form-item label="" class="sign-icon-box">
          <div class="sign-icon" @click="goSingLink">
            <img src="../../../assets/img/sign-icon.png" alt="logo">
          </div>
        </el-form-item>
        <div class="sign-tips">
          <img src="../../../assets/img/tips.png" alt="logo" class="tips-icon">
          <p class="sign-title">Note</p>
          <p class="sign-doc">For the security of your wallet, copy the value of Signature Data to sign other wallets, and then fill the returned signature value into the Signature Value box to complete the signature.</p>
        </div> -->
      </el-form>
      <p slot="footer" class="dialog-footer">
        <el-button @click="signVisible = false">Return</el-button>
        <el-button type="primary" @click="confirmSign">Confirm</el-button>
      </p>
    </el-dialog>
  </div>
</template>
<script>
import { BDOptionInfo, getDomainList, submitBDConfig, 
  getContractList, getContractParam } from '@/api/storeApi';
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
      // General parameter form
      ruleForm: {
        bdCode: 'SystemBD',
        functionName: '',
        submitter: '',
        feeCurrency: '',
        feeMaxAmount: '',
        contractName: ''
      },
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
      // BDList
      dbNameList: [],
      // domainList
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
      contractList: [],
      contractParamData: {},
      contractSubmit: {},
      signVisible: false,
      // sign
      signData: {
        // txId: '',
        // sign: '',
        privateKey: ''
      },
      saveFunctionArr: ['IDENTITY_SETTING', 'BD_PUBLISH', 'PERMISSION_REGISTER', 'AUTHORIZE_PERMISSION', 'CANCEL_PERMISSION', 'REGISTER_POLICY', 
      'MODIFY_POLICY', 'SYSTEM_PROPERTY', 'IDENTITY_BD_MANAGE', 'KYC_SETTING', 'SET_FEE_RULE', 'SAVE_ATTESTATION', 'BUILD_SNAPSHOT']
    }
  },
  computed: {
    ...mapGetters(['functionNameList'])
  },
  created () {
    this.$store.commit('changeBdMenu', this.$route.meta.menu);
    this.getOption();
    this.getDomain();
    this.getContract();
  },
  methods: {
    // open pagr
    goSingLink () {
      window.open('https://www.myetherwallet.com/access-my-wallet', '_blank');
    },
    onCopySign () {
      this.$notify.success({message: 'Operation Success'});
    },
    // submit sign
    confirmSign () {
      // validate form
      this.$refs['contractFrom'].validateField('privateKey', error => {
        if (!error) {
          // submit BD
          this.validateBD(false);
        }
      });
    },
    // changge functionName
    async changeFunction (name) {
      // Select non system BD
      if (this.ruleForm.bdCode !== 'SystemBD') {
        let currentFunction = this.functionNameList.filter(v => name === v.name);
        let params = {
          data: {
            contractAddress: this.ruleForm.contractName,
            methodSign: currentFunction[0].methodSign
          }
        }
        // get key，value
        let data = await getContractParam(params);
        this.contractParamData = JSON.parse(JSON.stringify(data.data));
        this.contractSubmit = JSON.parse(JSON.stringify(data.data));
        for (let i in this.contractSubmit) {
          this.contractSubmit[i] = '';
        }
      }
    },
    async getContract () {
      let data = await getContractList();
      this.contractList = JSON.parse(JSON.stringify(data.data));
    },
    // submit DB
    async submitBD (valid, validChild, showDialog) {
      // form validate
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
          // let sign = await getSignValue(params);
          // if (sign.code === '000000') {
          //   this.signData = JSON.parse(JSON.stringify(sign.data));
          this.signVisible = true;
          // }
        } else {
          let loadingInstance = Loading.service();
          // params.data.param = Object.assign(submitData, this.signData);
          // submitData.privateKey = this.signData.privateKey;
          // submitData.txId = this.signData.txId;

          params.data = Object.assign(params.data, this.signData);
          params.notify = notify.any;
          // submit BD
          let subResult = await submitBDConfig(params);
          loadingInstance.close();
          if (subResult.code === '000000') {
            this.signVisible = false;
          }
        }
      }
    },
    validateBD (flag) {
      this.$refs['ruleForm'].validate(async valid => {
        // Selecting BD system also needs to validate the corresponding subform
        if (this.ruleForm.bdCode === 'SystemBD') {
          if (this.ruleForm.functionName) {
            let validChild = this.$refs[this.ruleForm.functionName].validateForm();
            this.submitBD(valid && validChild.valid, validChild, flag);
          }
        } else {
          // Non system BD
          let args = Object.assign({}, { args: this.contractSubmit });
          this.submitBD(valid, { ruleForm: args }, flag);
        }
      });
    },
    // change BDName
    changeBDName (code) {
      if (code === 'SystemBD') this.ruleForm.contractName = ''; this.contractParamData = {}; this.contractSubmit = {};
      let filterFunction = this.dbNameList.filter(v => v.code === code)[0].functions;
      let tempArr = filterFunction;
      if (code === 'SystemBD') {
        // Filter unnecessary FunctionNames
        tempArr = filterFunction.filter(v => {
          let data;
          this.saveFunctionArr.forEach(va => {
            if (v.name === va) {
              data = v;
            }
          });
          return data;
        });
      }
      // Store the corresponding FunctionNameList in the store, which is required by multi-component
      this.$store.commit('changeFunctionNameList', tempArr);
      this.ruleForm.functionName = '';
    },
    // getDomainList
    async getDomain () {
      let data = await getDomainList();
      this.domainIDList = JSON.parse(JSON.stringify(data.data));
    },
    // getBDList
    async getOption () {
      this.loading = true;
      let params = {
        bdCode: '',
        slient: true
      }
      let data = await BDOptionInfo(params);
      if (data.code === '000000' && data.data) {
        this.dbNameList = JSON.parse(JSON.stringify(data.data));
        // The first one by default is system BD
        this.ruleForm.bdCode = this.dbNameList[0].code;
        // Filter unnecessary functionnames
        let tempArr = data.data[0].functions.filter(v => {
          let data;
          this.saveFunctionArr.forEach(va => {
            if (v.name === va) {
              data = v;
            }
          });
          return data;
        });
        this.$store.commit('changeFunctionNameList', tempArr);
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