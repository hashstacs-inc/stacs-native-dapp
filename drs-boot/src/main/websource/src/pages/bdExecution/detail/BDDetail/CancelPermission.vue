<template>
  <div class="cancel-permission">
    <p class="title">Special Information</p>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
      label-width="150px" class="general-form" label-position="left">
      <el-form-item label="Identity Address" prop="identityAddress">
        <el-input v-model="ruleForm.identityAddress" placeholder="Please enter the  identity address" :maxlength="40"></el-input>
      </el-form-item>
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
  name: 'CancelPermission',
  data () {
    return {
      ruleForm: {
        identityAddress: '',
        permissionNames: ''
      },
      permissionNameList: [
        {
          name: '11111'
        }, {
          name: '22222'
        }
      ],
      rules: {
        identityAddress: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        permissionNames: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ]
      }
    }
  }
}
</script>
<style scoped lang="scss">
.cancel-permission {
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