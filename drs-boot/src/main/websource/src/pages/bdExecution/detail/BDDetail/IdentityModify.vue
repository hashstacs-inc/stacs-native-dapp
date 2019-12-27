<template>
  <div class="identity-modify">
    <p class="title">Special Information</p>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
      label-width="150px" class="general-form" label-position="left">
      <el-form-item label="Property" prop="property">
        <el-input v-model="ruleForm.property" placeholder="Please enter with json" :maxlength="1024"></el-input>
      </el-form-item>
      <el-form-item label="Address" prop="address">
        <el-input v-model="ruleForm.address" placeholder="Please enter new identity address" :maxlength="40"></el-input>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
export default {
  name: 'IdentityModify',
  data () {
    return {
      ruleForm: {
        property: '',
        address: ''
      },
      rules: {
        address: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    validateForm () {
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
.identity-modify {
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