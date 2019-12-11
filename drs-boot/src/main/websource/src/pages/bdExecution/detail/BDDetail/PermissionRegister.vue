<template>
  <div class="permission-register">
    <p class="title">Special Information</p>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
      label-width="150px" class="general-form" label-position="left">
      <el-form-item label="Permission Names" prop="permissionNames">
        <el-select v-model="ruleForm.permissionNames" placeholder="Please select domian IDs" multiple filterable>
          <el-option :label="v.name" :value="v.name" v-for="(v, k) in permissionNameList" :key="k"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
export default {
  name: 'PermissionRegister',
  data () {
    return {
      permissionNameList: [
        {
          name: '111111'
        }, {
          name: '222222'
        }
      ],
      ruleForm: {
        permissionNames: ''
      },
      rules: {
        permissionNames: [
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
.permission-register {
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