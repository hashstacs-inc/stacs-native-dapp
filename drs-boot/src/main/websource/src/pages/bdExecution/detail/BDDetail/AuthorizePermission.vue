<template>
  <div class="authorize-permission">
    <p class="title">Special Information</p>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
      label-width="150px" class="general-form" label-position="left">
      <el-form-item label="Identity Address" prop="identityAddress">
        <el-input v-model="ruleForm.identityAddress" placeholder="Please enter the  identity address" :maxlength="40"></el-input>
      </el-form-item>
      <el-form-item label="Permission Names" prop="permissionNames">
        <el-select v-model="ruleForm.permissionNames" placeholder="Please select domian IDs" multiple filterable>
          <el-option :label="v.permissionName" :value="v.permissionName" v-for="(v, k) in permissionNameList" :key="k"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Identity Type" prop="identityType">
        <el-select v-model="ruleForm.identityType">
          <el-option :label="v.name" :value="v.name" v-for="(v, k) in identityTypeList" :key="k"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { getPermissionList } from '@/api/storeApi';

export default {
  name: 'AuthorizePermission',
  data () {
    return {
      // 表单数据
      ruleForm: {
        identityAddress: '',
        permissionNames: [],
        identityType: ''
      },
      identityTypeList: [{
        name: 'node'
      }, {
        name: 'user'
      }, {
        name: 'domain'
      }],
      permissionNameList: [],
      rules: {
        identityAddress: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        permissionNames: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ],
        identityType: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ]
      }
    }
  },
  created () {
    this.getPermission();
  },
  methods: {
    async getPermission () {
      let data = await getPermissionList();
      this.permissionNameList = JSON.parse(JSON.stringify(data.data));
    },
    // 效验表单，返回数据
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
.authorize-permission {
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