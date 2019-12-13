<template>
  <div class="KYC-setting">
    <p class="title">Special Information</p>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
      label-width="150px" class="general-form" label-position="left">
      <el-form-item label="Identity Address" prop="identityAddress">
        <el-input v-model="ruleForm.identityAddress" placeholder="Please enter identity address" :maxlength="40"></el-input>
      </el-form-item>
    </el-form>
    <p class="title">KYC</p>
    <el-form :model="ruleForm" ref="ruleKycForm">
      <div v-for="(v, k) in ruleForm.KYC" :key="k" class="kyc-box">
        <el-form-item label="key" :prop="'KYC.' + k + '.key'" :inline="true" label-width="50px"
          :rules="{ required: true, message: 'This filed is required', trigger: 'blur' }">
          <el-input v-model="v.key" placeholder="Please enter Key" :maxlength="1024" :disabled="v.disabled"></el-input>
        </el-form-item>
        <el-form-item label="value" :prop="'KYC.' + k + '.value'" :inline="true" label-width="50px"
          :rules="{ required: true, message: 'This filed is required', trigger: 'blur' }">
          <el-input v-model="v.value" placeholder="Please enter Key word" :maxlength="1024" :disabled="v.disabled"></el-input>
        </el-form-item>
        <div class="btn-kyc" :class="{'kyc-disabled': v.disabled}" @click="kycBtn(v, k, 'ruleKycForm')">{{v.disabled ? '-' : '+'}}</div>
      </div>
    </el-form>
  </div>
</template>
<script>
export default {
  name: 'KYCSetting',
  data () {
    return {
      ruleForm: {
        identityAddress: '',
        KYC: [
          {
            key: '',
            value: '',
            disabled: false
          }
        ]
      },
      rules: {
        identityAddress: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    validateFrom () {
      let params = {
        identityAddress: this.ruleForm.identityAddress,
        KYC: {}
      };
      let edited = this.ruleForm.KYC.filter(v => v.disabled);
      edited.forEach(v => {
        params.KYC[v.key] = v.value;
      });
      params.KYC = JSON.stringify(params.KYC);
      let validCode = {
        valid: false,
        ruleForm: params
      };
      this.$refs['ruleForm'].validate(valid => {
        if (edited.length === 0) {
          this.$refs['ruleKycForm'].validate(validKyc => {
            if (valid && validKyc) {
              validCode.valid = true;
            }
          });
        } else {
          if (valid) {
            validCode.valid = true;
          }
        }
      });
      
      return validCode;
    },
    kycBtn (v, k, from) {
      if (!v.disabled) {
        this.$refs[from].validate(valid => {
          if (valid) {
            let obj = JSON.parse(JSON.stringify(v));
            obj.disabled = true;
            this.ruleForm.KYC.push(obj);
            v.key = '';
            v.value = '';
          }
        });
      } else {
        this.ruleForm.KYC.splice(k, 1);
      }
    }
  }
}
</script>
<style scoped lang="scss">
.KYC-setting {
  margin-top: 40px;
  .title {
    color: #333333;
    font-size: 18px;
    font-weight: 500;
    margin-bottom: 25px;
  }
  .kyc-box {
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: relative;
    margin-bottom: 22px;
    .btn-kyc {
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
    .kyc-disabled {
      background-color: #DC7171;
    }
  }
}
</style>
<style lang="scss">
.el-form-item[inline=true] {
  display: inline-block;
  margin-bottom: 0px;
  .el-form-item__content {
    width: 210px;
  }
}
</style>