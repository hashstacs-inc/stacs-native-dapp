<template>
  <div class="permission-register">
    <p class="title">Special Information</p>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
      label-width="150px" class="general-form" label-position="left">
      <el-form-item label="Permission Name" prop="permissionName">
        <!-- <el-select v-model="ruleForm.permissionName" placeholder="Please select domian IDs" multiple filterable>
          <el-option :label="v.permissionName" :value="v.permissionIndex" v-for="(v, k) in permissionNameList" :key="k"></el-option>
        </el-select> -->
        <el-input v-model="ruleForm.permissionName"></el-input>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { getPermissionList } from '@/api/storeApi';

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
        permissionName: ''
      },
      rules: {
        permissionName: [
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