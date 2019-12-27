<template>
  <div class="set-fee-rule">
    <p class="title">Special Information</p>
    <el-form :model="ruleForm" ref="ruleForm">
      <div v-for="(v, k) in ruleForm.rules" :key="k" class="rule-box">
        <el-form-item label="Policy ID" :prop="'rules.' + k + '.policyId'" :inline="true" label-width="90px"
          :rules="{ required: true, message: 'This filed is required', trigger: 'blur' }">
          <el-select v-model="v.policyId" v-if="!v.disabled" placeholder="Please select">
            <el-option :label="v.policyName" :value="v.policyId" v-for="(v, k) in policyIDList" :key="k"></el-option>
          </el-select>
          <el-input v-model="v.policyId" :disabled="v.disabled" v-else></el-input>
        </el-form-item>
        <el-form-item label="Amount" :prop="'rules.' + k + '.amount'" :inline="true" label-width="90px"
          :rules="{ required: true, message: 'This filed is required', trigger: 'blur' }">
          <el-input v-model="v.amount" placeholder="Please enter fee amount" :maxlength="18" :disabled="v.disabled"></el-input>
        </el-form-item>
        <div class="btn-rule" :class="{'rule-disabled': v.disabled}" @click="ruleBtn(v, k, 'ruleForm')">{{v.disabled ? '-' : '+'}}</div>
      </div>
    </el-form>
  </div>
</template>
<script>
import { getPolicyList } from '@/api/storeApi';

export default {
  name: 'SetFeeRule',
  data () {
    return {
      ruleForm: {
        rules: [
          {
            policyId: '',
            amount: '',
            disabled: false
          }
        ]
      },
      policyIDList: []
    }
  },
  methods: {
    validateForm () {
      let cloneRules = JSON.parse(JSON.stringify(this.ruleForm.rules));
      let edited = cloneRules.filter(v => v.disabled);
      let params = edited.map(v => {
        delete v.disabled;
        return v;
      });
      let validCode = {
        valid: false,
        ruleForm: { rules: params }
      };
      if (edited.length === 0) {
        this.$refs['ruleForm'].validate(valid => {
          validCode.valid = valid;
        });
      } else {
        validCode.valid = true;
      }
      return validCode;
    },
    ruleBtn (v, k, from) {
      if (!v.disabled) {
        this.$refs[from].validate(valid => {
          if (valid) {
            let obj = JSON.parse(JSON.stringify(v));
            obj.disabled = true;
            this.ruleForm.rules.push(obj);
            v.policyId = '';
            v.amount = '';
          }
        });
      } else {
        this.ruleForm.rules.splice(k, 1);
      }
    },
    async getPolicy () {
      let data = await getPolicyList();
      this.policyIDList = JSON.parse(JSON.stringify(data.data));
    }
  },
  created () {
    this.getPolicy();
  }
}
</script>
<style scoped lang="scss">
.set-fee-rule {
  margin-top: 40px;
  .title {
    color: #333333;
    font-size: 18px;
    font-weight: 500;
    margin-bottom: 25px;
  }
  .rule-box {
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: relative;
    margin-bottom: 22px;
    .btn-rule {
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
    .rule-disabled {
      background-color: #DC7171;
    }
  }
}
</style>
<style lang="scss">
.set-fee-rule {
  .el-form-item[inline=true] {
    display: inline-block;
    margin-bottom: 0px;
    .el-form-item__content {
      width: 200px;
    }
  }
}

</style>