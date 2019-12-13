<template>
  <div class="identity-setting">
    <p class="title">Special Information</p>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
      label-width="150px" class="general-form" label-position="left">
      <el-form-item label="IdentityType" prop="identityType">
        <el-select v-model="ruleForm.identityType">
          <el-option :label="v.name" :value="v.name" v-for="(v, k) in identityTypeList" :key="k"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="property" prop="property">
        <el-input v-model="ruleForm.property" placeholder="Please enter with json" :maxlength="1024"></el-input>
      </el-form-item>
      <el-form-item label="address" prop="address">
        <el-input v-model="ruleForm.address" placeholder="Please enter new identity address" :maxlength="40"></el-input>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
export default {
  name: 'IdentitySetting',
  data () {
    return {
      ruleForm: {
        property: '',
        address: '',
        identityType: ''
      },
      identityTypeList: [{
        name: 'node'
      }, {
        name: 'user'
      }, {
        name: 'domain'
      }],
      rules: {
        address: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        identityType: [
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
    }
  }
}
</script>
<style scoped lang="scss">
.identity-setting {
  margin-top: 40px;
  overflow: hidden;
  .title {
    color: #333333;
    font-size: 18px;
    font-weight: 500;
    margin-bottom: 25px;
  }
}
</style>