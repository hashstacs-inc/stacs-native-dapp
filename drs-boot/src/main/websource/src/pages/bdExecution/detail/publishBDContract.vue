<template>
  <div class="publish-BD-contract" v-loading="loading">
    <p class="title">Publish BD Contract</p>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
      label-width="150px" class="general-form" label-position="left">
      <el-form-item label="BD Name" prop="bdName">
        <el-input v-model="ruleForm.bdName" disabled></el-input>
      </el-form-item>
      <el-form-item label="Operation Address" prop="submitter">
        <el-input v-model="ruleForm.submitter" :maxlength="40"></el-input>
      </el-form-item>
      <el-form-item label="Contract Address" prop="contractAddress">
        <el-input v-model="ruleForm.contractAddress" :maxlength="40"></el-input>
      </el-form-item>
      <el-form-item label="Contract Name" prop="name">
        <el-input v-model="ruleForm.name" :maxlength="64"></el-input>
      </el-form-item>
      <el-form-item label="Extension" prop="extension">
        <el-input v-model="ruleForm.extension" :maxlength="1024"></el-input>
      </el-form-item>
      <el-form-item label="Contractor" prop="contractor">
        <el-input v-model="ruleForm.contractor"></el-input>
      </el-form-item>
      <el-form-item label="Source Code" prop="sourceCode">
        <el-input v-model="ruleForm.sourceCode" type="textarea" :autosize="{ minRows: 4}"></el-input>
      </el-form-item>
      <el-form :model="argsForm" ref="argsForm" 
      label-width="150px" class="general-form" label-position="left">
        <el-form-item label="Init Args" :prop="'argsList.' + k + '.value'" v-for="(v, k) in argsForm.argsList" :key="k"
          :rules="{ required: true, message: 'This filed is required', trigger: 'blur' }" class="args-box">
          <el-input v-model="v.value" :disabled="v.disabled"></el-input>
          <div class="add-args" :class="{'args-disabled': v.disabled}" @click="addArgs(v, k)">{{v.disabled ? '-' : '+'}}</div>
        </el-form-item>
      </el-form>
    </el-form>
    <div class="submit-btn">
      <p @click="validateFrom">Submit</p>
    </div>
    <!-- 签名dialog-->
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
import { submitBDConfig, getSignValue } from '@/api/storeApi';
import { notify } from '@/common/util';
import { Loading } from 'element-ui';

export default {
  name: 'PublishBDContract',
  data () {
    return {
      // 表单绑定数据
      ruleForm: {
        bdName: '',
        submitter: '',
        contractAddress: '',
        name: '',
        extension: '',
        contractor: '',
        sourceCode: '',
        initArgs: []
      },
      loading: false,
      // args表单数据
      argsForm: {
        argsList: [
          {
            value: '',
            disabled: false
          }
        ]
      },
      // 表单规则
      rules: {
        bdName: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        submitter: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        name: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        contractor: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        sourceCode: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ]
      },
      signVisible: false,
      // 签名数据
      signData: {
        txId: '',
        sign: '',
        submitterSign: ''
      }
    }
  },
  created () {
    // 控制左边菜单active
    this.$store.commit('changeBdMenu', this.$route.meta.menu);
    this.ruleForm.bdName = this.$route.query.name;
  },
  methods: {
    // 复制待签名字段成功
    onCopySign () {
      this.$notify.success({message: 'Operation Success'});
    },
    // 提交签名内容
    confirmSign () {
      this.$refs['contractFrom'].validateField('submitterSign', error => {
        if (!error) {
          // 提交表单内容
          this.submitContract(false);
        }
      });
    },
    // 打开签名页面
    goSingLink () {
      window.open('https://www.myetherwallet.com/access-my-wallet', '_blank');
    },
    // 效验表单
    validateFrom () {
      let isValid = false;
      this.$refs['ruleForm'].validate(async valid => {
        if (this.argsForm.argsList.length === 1) {
          this.$refs['argsForm'].validate(argsValid => {
            if (valid && argsValid) {
              isValid = true;
              this.submitContract(true, isValid);
            }
          });
        } else {
          isValid = valid;
          this.submitContract(true, isValid);
        }
      });
    },
    // 提交表单
    async submitContract (flag, valid) {
      if (valid) {
        let subData = Object.assign({}, this.ruleForm);
        delete subData.bdName;
        subData['code'] = this.$route.query.bdCode;
        subData['bdCode'] = this.$route.query.bdCode;
        subData['fromAddr'] = subData.submitter;
        let subArgs = Object.assign([], this.argsForm.argsList.filter(v => v.disabled));
        subData.initArgs = subArgs.map(v => {
          return v.value;
        });
        let reqData = {
          data: {
            functionName: 'CREATE_CONTRACT',
            param: {}
          },
          slient: true,
          notify: notify.error
        }
        reqData.data.param = Object.assign(reqData.data.param, subData);
        if (flag) {
          // 获取签名
          this.loading = true;
          let sign = await getSignValue(reqData);
          if (sign.code === '000000') {
            this.signData = JSON.parse(JSON.stringify(sign.data));
            this.signVisible = true;
          }
          this.loading = false;
        } else {
          let loadingInstance = Loading.service();
          // 合并签名数据
          reqData.data.param = Object.assign(reqData.data.param, this.signData);
          reqData.data.param.txId = this.signData.txId;
          reqData.data.param.submitterSign = this.signData.submitterSign;
          reqData.notify = notify.any;
          // 提交
          let subResult = await submitBDConfig(reqData);
          loadingInstance.close();
          if (subResult.code === '000000') {
            this.signVisible = false;
            // 返回
            this.$router.push({ name: 'History' });
          }
        }
      }
    },
    // 添加一条Args
    addArgs(v, k) {
      if (!v.disabled) {
        this.$refs['argsForm'].validateField(`argsList.${k}.value`, error => {
          if (!error) {
            let add = Object.assign({}, v);
            add.disabled = true;
            this.argsForm.argsList[0].value = '';
            this.argsForm.argsList[0].disabled = false;
            this.argsForm.argsList.push(add);
          }
        });
      } else {
        // 删除
        this.argsForm.argsList.splice(k ,1);
      }
    }
  }
}
</script>
<style scoped lang="scss">
.publish-BD-contract {
  width: 710px;
  background-color: #fff;
  padding: 40px 40px;
  padding-right: 100px;
  float: left;
  .title {
    color: #333333;
    font-size: 18px;
    font-weight: 500;
    margin-bottom: 25px;
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
  .args-box {
    position: relative;
    .add-args {
      position: absolute;
      right: -48px;
      top: 50%;
      transform: translateY(-50%);
      width: 28px;
      height: 28px;
      background-color: #5782F7;
      color: #fff;
      font-size: 25px;
      text-align: center;
      cursor: pointer;
      line-height: 24px;
    }
    .args-disabled {
      background-color: #DC7171;
    }
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