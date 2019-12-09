<template>
  <div class="bd-box">
    <div class="general-information">
      <p class="title">General Information</p>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
        label-width="150px" class="general-form" label-position="left">
        <el-form-item label="BD Name" prop="bDName">
          <el-select v-model="ruleForm.bDName">
            <el-option :label="v.name" :value="v.id" v-for="(v, k) in dbNameList" :key="k"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Function Name" prop="functionName">
          <el-select v-model="ruleForm.functionName" placeholder="Please select">
            <el-option :label="v.name" :value="v.id" v-for="(v, k) in functionNameList" :key="k"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Opeartion Address" prop="opeartionAddress">
          <el-input v-model="ruleForm.opeartionAddress" placeholder="Please enter address" :maxlength="40"></el-input>
        </el-form-item>
        <el-form-item label="Fee Currency" prop="feeCurrency">
          <el-input v-model="ruleForm.feeCurrency" disabled :maxlength="32"></el-input>
        </el-form-item>
        <el-form-item label="Fee Max Amount" prop="feeMaxAmount">
          <el-input v-model="ruleForm.feeMaxAmount" placeholder="Please enter fee max amount" :maxlength="18"></el-input>
        </el-form-item>
        <el-form-item class="general-submit">
          <div class="submit-btn">Submit</div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
export default {
  name: 'BD',
  watch: {
    'ruleForm.feeMaxAmount': {
      handler (n, o) {
        if (n.indexOf('.') !== -1) {
          let data = n.split('.');
          if (data[1].length > 8) {
            this.ruleForm.feeMaxAmount = o;
          }
        }
      },
      deep: true
    },
    'ruleForm.functionName': {
      handler (n) {
        this.ruleForm.feeCurrency = this.functionNameList.filter(v => v.id === n)[0].feeCurrency;
      },
      deep: true
    }
  },
  data () {
    return {
      ruleForm: {
        bDName: 0,
        functionName: '',
        opeartionAddress: '',
        feeCurrency: '',
        feeMaxAmount: ''
      },
      rules: {
        bDName: [
          { required: true, message: '请输入活动名称', trigger: 'blur' },
          { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        functionName: [
          { required: true, message: '请选择活动区域', trigger: 'change' }
        ],
        opeartionAddress: [
          { required: true, message: '请选择日期', trigger: 'change' }
        ],
        feeMaxAmount: [
          { required: true, message: '请至少选择一个活动性质', trigger: 'change' }
        ]
      },
      dbNameList: [
        {
          name: 'System BD',
          id: 0
        }, {
          name: 'User Customize BD',
          id: 1
        }
      ],
      functionNameList: [
        {
          name: 'Register Policy',
          id: 0,
          feeCurrency: '123'
        }, {
          name: 'Modify Policy',
          id: 1,
          feeCurrency: '456'
        }, {
          name: 'Identity Setting',
          id: 2,
          feeCurrency: '789'
        }
      ]
    }
  }
}
</script>
<style scoped lang="scss">
.bd-box {
  width: 710px;
  background-color: #fff;
  padding: 40px 30px;
  padding-right: 106px;
  float: left;
  .general-information {
    .title {
      color: #333333;
      font-size: 18px;
      font-weight:500;
    }
    .general-form {
      margin-top: 25px;
      .submit-btn {
        width: 250px;
        height: 40px;
        background-color: #053C8C;
        margin: 0 auto;
        text-align: center;
        line-height: 40px;
        color: #fff;
        font-size: 18px;
        cursor: pointer;
        margin-top: 40px;
      }
    }
  }
}
</style>