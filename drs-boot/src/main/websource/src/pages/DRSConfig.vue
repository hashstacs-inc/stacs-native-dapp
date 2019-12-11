<template>
  <div class="drs-config">
    <Header />
    <div class="drs-content">
      <p class="title">DRS Configuration</p>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
        label-width="190px" label-position="left">
        <el-form-item label="Dapp-store Address" prop="dappStoreAddress">
          <el-input v-model="ruleForm.dappStoreAddress" placeholder="Please enter url"></el-input>
        </el-form-item>
        <el-form-item label="Domain URL" prop="domainURL">
          <el-input v-model="ruleForm.domainURL" placeholder="Please enter url"></el-input>
        </el-form-item>
        <el-form-item label="Domain Pubkey" prop="domainPubkey">
          <el-input v-model="ruleForm.domainPubkey" placeholder="Please enter url"></el-input>
        </el-form-item>
        <el-form-item label="Merchant PriKey" prop="merchantPriKey">
          <el-input v-model="ruleForm.merchantPriKey" placeholder="Please enter url"></el-input>
        </el-form-item>
        <el-form-item label="Merchant Pubkey" prop="merchantPubkey">
          <el-input v-model="ruleForm.merchantPubkey" disabled></el-input>
        </el-form-item>
        <el-form-item label="Merchant Aeskey" prop="merchantAeskey">
          <el-input v-model="ruleForm.merchantAeskey"></el-input>
        </el-form-item>
        <el-form-item label="Merchant ID" prop="merchantID">
          <el-input v-model="ruleForm.merchantID"></el-input>
        </el-form-item>
        <el-form-item label="Callback URL" prop="callbackURL">
          <el-input v-model="ruleForm.callbackURL" placeholder="Please enter url"></el-input>
        </el-form-item>
        <el-form-item label="Download-path" prop="downloadPath">
          <el-input v-model="ruleForm.downloadPath" disabled></el-input>
        </el-form-item>
        <el-form-item label="Dapp Configuration-path" prop="dappConfigurationPath">
          <el-input v-model="ruleForm.dappConfigurationPath" disabled></el-input>
        </el-form-item>
        <el-form-item label="Base path" prop="basePath">
          <el-input v-model="ruleForm.basePath" placeholder="Please enter url"></el-input>
        </el-form-item>
        <div class="foot-btn">
          <el-button @click="$router.push({ name: 'Home'})">Back</el-button>
          <el-button type="primary" @click="submitFrom">Confirm</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>
<script>
import { getSysConfig, modifySysConfig } from '@/api/storeApi';
import Header from './header';

export default {
  name: 'DrsConfig',
  data () {
    return {
      ruleForm: {
        dappStoreAddress: '',
        domainURL: '',
        domainPubkey: '',
        merchantPriKey: '',
        merchantPubkey: '',
        merchantAeskey: '',
        merchantID: '',
        callbackURL: '',
        downloadPath: '',
        dappConfigurationPath: '',
        basePath: ''
      },
      rules: {
        dappStoreAddress: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        domainURL: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        domainPubkey: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        merchantPriKey: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        merchantPubkey: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        merchantAeskey: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        merchantID: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        callbackURL: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        downloadPath: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        dappConfigurationPath: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        basePath: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ]
      },
      menuList: [
        {
          name: 'DAPP Store',
          path: 'store',
          pathName: 'Store'
        }, {
          name: 'BD Execution',
          path: 'BD',
          pathName: 'BD'
        }
      ]
    }
  },
  created () {
    this.SysConfig();
  },
  methods: {
    async SysConfig () {
      let data = await getSysConfig();
      // let data = {
      //   code: '000000',
      //   data: {
      //     dappStoreAddress: 'baidu.comDappAddress',
      //     domainURL: 'baidu.comDomainURL',
      //     domainPubkey: 'baidu.comDomainPubkey',
      //     merchantPriKey: 'baidu.comMerchantPriKey',
      //     merchantPubkey: 'baidu.comMerchantPubkey',
      //     merchantAeskey: 'baidu.comMerchantAeskey',
      //     merchantID: 'baidu.comMerchantID',
      //     callbackURL: 'baidu.comCallbackURL',
      //     downloadPath: 'baidu.comDownloadPath',
      //     dappConfigurationPath: 'baidu.comConfigurationPath',
      //     basePath: 'baidu.comBasePath'
      //   },
      //   msg: 'SUCCESS'
      // }
      this.ruleForm = JSON.parse(JSON.stringify(data.data));
    },
    changeTab (v) {
      this.$router.push({name: v.pathName});
    },
    submitFrom () {
      this.$refs['ruleForm'].validate(async valid => {
        if (valid) {
          let params = {
            slient: true,
            data: Object.assign({}, this.ruleForm)
          };
          let data = await modifySysConfig(params);
          if (data.code === '000000') {
            this.$alert('Configuration success.', 'System', {
              confirmButtonText: 'OK',
              callback: () => {
                this.$router.go(-1);
              }
            });
          } else {
            this.$alert(`<p>Configuration failed, please try again.</p><p>Reason：${data.mas}</p>`, 'System', {
              confirmButtonText: 'OK',
              dangerouslyUseHTMLString: true
            });
          }
        }
      });
    }
  },
  components: {
    Header
  }
}
</script>
<style scoped lang="scss">
.drs-config {
  min-height: 100%;
  background-color: #F2F5FC;
  padding-bottom: 50px;
  .drs-content {
    width: 900px;
    margin: 0 auto;
    background-color: #fff;
    padding: 40px 170px;
    margin-top: 20px;
    .title {
      color: #333333;
      font-size: 18px;
      font-weight:500;
      margin-bottom: 24px;
    }
    .foot-btn {
      margin-top: 90px;
      text-align: center;
    }
  }
}
</style>
<style lang="scss">
.drs-config {
  .el-form-item__label {
    font-size: 14px;
    color: #666666;
    padding-left: 0px !important;
  }
}
</style>