<template>
  <div class="system-property">
    <p class="title">Special Information</p>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
      label-width="150px" class="general-form" label-position="left">
      <el-form-item label="Proprty Name" prop="key">
        <el-input v-model="ruleForm.key" placeholder="Please enter proprty name"></el-input>
      </el-form-item>
      <el-form-item label="Proprty Information" prop="value">
        <el-input v-model="ruleForm.value" placeholder="Please enter property information"></el-input>
      </el-form-item>
      <el-form-item label="Descision" prop="desc">
        <el-input v-model="ruleForm.desc" placeholder="Please enter property descision"></el-input>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
export default {
  name: 'SystemProperty',
  data () {
    return {
      ruleForm: {
        key: '',
        value: '',
        desc: ''
      },
      rules: {
        key: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        value: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        desc: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
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
.system-property {
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