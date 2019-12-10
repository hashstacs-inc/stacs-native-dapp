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
          <el-option :label="v.bdCode" :value="v.bdCode" v-for="(v, k) in BDCodeList" :key="k"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Action Type" prop="actionType">
        <el-select v-model="ruleForm.actionType">
          <el-option :label="v.name" :value="v.name" v-for="(v, k) in actionTypeList" :key="k"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
// import { BDOptionInfo } from '@/api/storeApi';

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
          name: 'Froze'
        }, {
          name: 'Unfroze'
        }
      ],
      BDCodeList: []
    }
  },
  methods: {
    async getDBCodes () {
      // let data = await BDOptionInfo();
      let data = {
        code: '000000',
        msg: 'SUCCESS',
        data: [
          {
            "bdCode":"SysBD",
            "bdType":"system",
            "bdVersion":"1.0",
            "functionName":"BD_PUBLISH",
            "initPermission":"SLAVE",
            "initPolicy":"INIT_BD"
          }
        ]
      }
      this.BDCodeList = [];
      this.BDCodeList.push(...data.data);
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