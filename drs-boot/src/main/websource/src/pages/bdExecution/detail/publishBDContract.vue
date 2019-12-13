<template>
  <div class="publish-BD-contract">
    <p class="title">Publish BD Contract</p>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
      label-width="150px" class="general-form" label-position="left">
      <el-form-item label="BD Name" prop="BDName">
        <el-input v-model="ruleForm.BDName" disabled></el-input>
      </el-form-item>
      <el-form-item label="Operation Address" prop="operationAddress">
        <el-input v-model="ruleForm.operationAddress" :maxlength="40"></el-input>
      </el-form-item>
      <el-form-item label="Contract Address" prop="contractAddress">
        <el-input v-model="ruleForm.contractAddress" :maxlength="40"></el-input>
      </el-form-item>
      <el-form-item label="Contract Name" prop="contractName">
        <el-input v-model="ruleForm.contractName" :maxlength="64"></el-input>
      </el-form-item>
      <el-form-item label="Extension" prop="extension">
        <el-input v-model="ruleForm.extension" :maxlength="1024"></el-input>
      </el-form-item>
      <el-form-item label="Contractor" prop="contractor">
        <el-input v-model="ruleForm.contractor"></el-input>
      </el-form-item>
      <el-form-item label="Source Code" prop="sourceCode">
        <el-input v-model="ruleForm.sourceCode" type="textarea" :autosize="{ minRows: 4}"></el-input>
      </el-form-item>
      <el-form-item label="Init Args" prop="initArgs">
        <el-input v-model="ruleForm.initArgs"></el-input>
      </el-form-item>
    </el-form>
    <div class="submit-btn">
      <p @click="submitContract">Submit</p>
    </div>
  </div>
</template>
<script>
export default {
  name: 'PublishBDContract',
  data () {
    return {
      ruleForm: {
        BDName: '',
        operationAddress: '',
        contractAddress: '',
        contractName: '',
        extension: '',
        contractor: '',
        sourceCode: '',
        initArgs: ''
      },
      rules: {
        BDName: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        operationAddress: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        contractName: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        contractor: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        sourceCode: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        initArgs: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ]
      }
    }
  },
  created () {
    this.$store.commit('changeBdMenu', this.$route.meta.menu);
  },
  methods: {
    submitContract () {

    }
  }
}
</script>
<style scoped lang="scss">
.publish-BD-contract {
  width: 710px;
  background-color: #fff;
  padding: 40px 40px;
  padding-right: 100px;
  float: left;
  .title {
    color: #333333;
    font-size: 18px;
    font-weight: 500;
    margin-bottom: 25px;
  }
  .submit-btn {
    padding-left: 65px;
    p {
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
</style>