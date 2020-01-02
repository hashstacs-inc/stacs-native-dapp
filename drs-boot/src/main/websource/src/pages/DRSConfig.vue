<template>
  <div class="drs-config">
    <Header />
    <div class="drs-content"  v-loading="loading">
      <p class="title">DRS Configuration</p>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
        label-width="190px" label-position="left">
        <el-form-item label="Dapp-store Address" prop="dappStorePath">
          <el-input v-model="ruleForm.dappStorePath" placeholder="Please enter url"></el-input>
        </el-form-item>
        <el-form-item label="Domain URL" prop="baseUrl">
          <el-input v-model="ruleForm.baseUrl" placeholder="Please enter url"></el-input>
        </el-form-item>
        <el-form-item label="Domain Pubkey" prop="chainPubKey">
          <el-input v-model="ruleForm.chainPubKey" placeholder="Please enter url"></el-input>
        </el-form-item>
        <el-form-item label="Merchant PriKey" prop="merchantPriKey">
          <el-input v-model="ruleForm.merchantPriKey" placeholder="Please enter url"></el-input>
        </el-form-item>
        <el-form-item label="Merchant Aeskey" prop="aesKey">
          <el-input v-model="ruleForm.aesKey"></el-input>
        </el-form-item>
        <el-form-item label="Merchant ID" prop="merchantId">
          <el-input v-model="ruleForm.merchantId"></el-input>
        </el-form-item>
        <el-form-item label="Callback URL" prop="callbackUrl">
          <el-input v-model="ruleForm.callbackUrl" placeholder="Please enter url"></el-input>
        </el-form-item>
        <el-form-item label="Download-path" prop="downloadPath">
          <el-input v-model="ruleForm.downloadPath" disabled></el-input>
        </el-form-item>
        <el-form-item label="Dapp Configuration-path" prop="configPath">
          <el-input v-model="ruleForm.configPath" disabled></el-input>
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
      loading: false,
      ruleForm: {
        aesKey: '',
        baseUrl: '',
        callbackUrl: '',
        chainPubKey: '',
        merchantId: '',
        merchantPriKey: '',
        configPath: '',
        dappStorePath: '',
        downloadPath: ''
      },
      rules: {
        dappStorePath: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        baseUrl: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        chainPubKey: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        merchantPriKey: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        aesKey: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        merchantId: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        callbackUrl: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        downloadPath: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        configPath: [
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
    // Get default configuration
    async SysConfig () {
      this.loading = true;
      let data = await getSysConfig();
      for(let i in data.data) {
        this.ruleForm = Object.assign(this.ruleForm, data.data[i]);
      }
      this.loading = false;
    },
    changeTab (v) {
      this.$router.push({name: v.pathName});
    },
    // submit
    submitFrom () {
      this.$refs['ruleForm'].validate(async valid => {
        if (valid) {
          let fromData = {
            domainConfig: {
              aesKey: this.ruleForm.aesKey,
              baseUrl: this.ruleForm.baseUrl,
              callbackUrl: this.ruleForm.callbackUrl,
              chainPubKey: this.ruleForm.chainPubKey,
              merchantId: this.ruleForm.merchantId,
              merchantPriKey: this.ruleForm.merchantPriKey,
            },
            drsConfig: {
              configPath: this.ruleForm.configPath,
              dappStorePath: this.ruleForm.dappStorePath,
              downloadPath: this.ruleForm.downloadPath
            }
          }
          let params = {
            slient: true,
            data: Object.assign({}, fromData)
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