<template>
  <div class="identity-bD-manage">
    <p class="title">Special Information</p>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
      label-width="150px" class="general-form" label-position="left">
      <el-form-item label="Target Address" prop="targetAddress">
        <el-input v-model="ruleForm.targetAddress" placeholder="Please enter target address" :maxlength="40"></el-input>
      </el-form-item>
      <el-form-item label="BD Codes" prop="BDCodes">
        <el-select v-model="ruleForm.BDCodes" multiple filterable placeholder="Please selec BD Code">
          <el-option :label="v.name" :value="v.code" v-for="(v, k) in BDCodeList" :key="k"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Action Type" prop="actionType">
        <el-select v-model="ruleForm.actionType">
          <el-option :label="v.name" :value="v.id" v-for="(v, k) in actionTypeList" :key="k"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { BDOptionInfo } from '@/api/storeApi';

export default {
  name: 'IdentityBDManage',
  data () {
    return {
      ruleForm: {
        targetAddress: '',
        BDCodes: '',
        actionType: ''
      },
      rules: {
        targetAddress: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        BDCodes: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ],
        actionType: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ]
      },
      actionTypeList: [
        {
          name: 'Froze',
          id: 'froze'
        }, {
          name: 'Unfroze',
          id: 'unfroze'
        }
      ],
      BDCodeList: []
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
    },
    async getDBCodes () {
      let params = {
        bdCode: ''
      }
      let data = await BDOptionInfo(params);
      this.BDCodeList = JSON.parse(JSON.stringify(data.data));
    }
  },
  created () {
    this.getDBCodes();
  }
}
</script>
<style scoped lang="scss">
.identity-bD-manage {
  margin-top: 40px;
  .title {
    color: #333333;
    font-size: 18px;
    font-weight: 500;
    margin-bottom: 25px;
  }
}
</style>