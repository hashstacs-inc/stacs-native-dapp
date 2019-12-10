<template>
  <div class="bd-box">
    <div class="general-information">
      <p class="title">General Information</p>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" 
        label-width="150px" class="general-form" label-position="left">
        <el-form-item label="BD Name" prop="bDName">
          <el-select v-model="ruleForm.bDName">
            <el-option :label="v.bdCode" :value="v.bdCode" v-for="(v, k) in dbNameList" :key="k"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Contract Name" prop="contractName" v-if="ruleForm.bDName !== 'SysBD'">
          <!-- <el-select v-model="ruleForm.contractName">
            <el-option :label="v.name" :value="v.name" v-for="(v, k) in contractNameList" :key="k"></el-option>
          </el-select> -->
          <el-input v-model="ruleForm.contractName" placeholder="Please enter address" :maxlength="40"></el-input>
        </el-form-item>
        <el-form-item label="Function Name" prop="functionName">
          <el-select v-model="ruleForm.functionName" placeholder="Please select"
            :disabled="ruleForm.bDName !== 'SysBD' ? ruleForm.contractName ? false : true: false">
            <el-option :label="v.name" :value="v.name" v-for="(v, k) in functionNameList" :key="k"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Opeartion Address" prop="opeartionAddress">
          <el-input v-model="ruleForm.opeartionAddress" placeholder="Please enter address" :maxlength="40"></el-input>
        </el-form-item>
        <el-form-item label="Fee Currency" prop="feeCurrency">
          <el-input v-model="ruleForm.feeCurrency" :maxlength="32"></el-input>
        </el-form-item>
        <el-form-item label="Fee Max Amount" prop="feeMaxAmount">
          <el-input v-model="ruleForm.feeMaxAmount" placeholder="Please enter fee max amount" :maxlength="18"></el-input>
        </el-form-item>
        <template v-if="ruleForm.bDName !== 'SysBD'">
          <p class="title" style="margin-bottom: 25px;">Special Information</p>
          <el-form-item label="Policy Name" prop="policyName">
            <el-input v-model="ruleForm.policyName" placeholder="Please enter policy name" :maxlength="64"></el-input>
          </el-form-item>
          <el-form-item label="Domain IDs" prop="domainIDs">
            <el-select v-model="ruleForm.domainIDs" placeholder="Please select domian IDs" multiple filterable>
              <el-option :label="v.desc" :value="v.domainId" v-for="(v, k) in domainIDList" :key="k"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Vote Pattern" prop="votePattern">
            <el-select v-model="ruleForm.votePattern" placeholder="Please select domian IDs">
              <el-option :label="v.name" :value="v.name" v-for="(v, k) in votePatternList" :key="k"></el-option>
            </el-select>
            <p class="vote-pattern-tips" v-if="ruleForm.votePattern === 'SYNC'">SYNC: The vote is processed</p>
            <p class="vote-pattern-tips" v-else>ASYNC: The vote is processed asynchronously.</p>
          </el-form-item>
          <el-form-item label="Callback Type" prop="callbackType">
            <el-select v-model="ruleForm.callbackType">
              <el-option :label="v.name" :value="v.name" v-for="(v, k) in callbackTypeList" :key="k"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Decision Type" prop="decisionType">
            <el-select v-model="ruleForm.decisionType">
              <el-option :label="v.name" :value="v.name" v-for="(v, k) in decisionTypeList" :key="k"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Require Auth IDs" prop="requireAuthIDs">
            <el-select v-model="ruleForm.requireAuthIDs" multiple filterable>
              <el-option :label="v.policyName" :value="v.policyId" v-for="(v, k) in requireAuthIDList" :key="k"></el-option>
            </el-select>
          </el-form-item>
        </template>
      </el-form>
    </div>
    <RegisterPolicy v-if="ruleForm.functionName === 'REGISTER_POLICY'" />
    <ModifyPolicy v-if="ruleForm.functionName === 'MODIFY_POLICY'" />
    <IdentitySetting v-if="ruleForm.functionName === 'IDENTITY_SETTING'" />
    <PermissionRegister v-if="ruleForm.functionName === 'PERMISSION_REGISTER'" />
    <AuthorizePermission v-if="ruleForm.functionName === 'AUTHORIZE_PERMISSION'" />
    <CancelPermission v-if="ruleForm.functionName === 'CANCEL_PERMISSION'" />
    <KYCSetting v-if="ruleForm.functionName === 'KYC_SETTING'" />
    <SaveAttestation v-if="ruleForm.functionName === 'SAVE_ATTESTATION'" />
    <SystemProperty v-if="ruleForm.functionName === 'SYSTEM_PROPERTY'" />
    <BDSpecification v-if="ruleForm.functionName === 'BD_PUBLISH'" />
    <SetFeeRule v-if="ruleForm.functionName === 'SET_FEE_RULE'" />
    <IdentityBDManage v-if="ruleForm.functionName === 'IDENTITY_BD_MANAGE'" />
    <div class="submit-btn">
      <p>Submit</p>
    </div>
  </div>
</template>
<script>
// import { BDOptionInfo, getDomainList, getPolicyList } from '@/api/storeApi';
import RegisterPolicy from './BDDetail/RegisterPolicy';
import ModifyPolicy from './BDDetail/ModifyPolicy';
import IdentitySetting from './BDDetail/IdentitySetting';
import PermissionRegister from './BDDetail/PermissionRegister';
import AuthorizePermission from './BDDetail/AuthorizePermission';
import CancelPermission from './BDDetail/CancelPermission';
import KYCSetting from './BDDetail/KYCSetting';
import SaveAttestation from './BDDetail/SaveAttestation';
import SystemProperty from './BDDetail/SystemProperty';
import BDSpecification from './BDDetail/BDSpecification';
import SetFeeRule from './BDDetail/SetFeeRule';
import IdentityBDManage from './BDDetail/IdentityBDManage';

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
      handler () {
        // this.ruleForm.feeCurrency = this.functionNameList.filter(v => v.id === n)[0].feeCurrency;
      },
      deep: true
    }
  },
  data () {
    return {
      ruleForm: {
        bDName: '',
        functionName: '',
        opeartionAddress: '',
        feeCurrency: '',
        feeMaxAmount: '',
        contractName: '',
        policyName: '',
        votePattern: 'SYNC',
        callbackType: 'ALL',
        decisionType: 'Full Vote',
        requireAuthIDs: ''
      },
      rules: {
        bDName: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        functionName: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ],
        opeartionAddress: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        feeMaxAmount: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        contractName: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ],
        policyName: [
          { required: true, message: 'This filed is required', trigger: 'blur' }
        ],
        domainIDs: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ],
        votePattern: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ],
        callbackType: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ],
        decisionType: [
          { required: true, message: 'This filed is required', trigger: 'change' }
        ]
      },
      dbNameList: [],
      functionNameList: [],
      contractNameList: [
        {
          name: 'qewe'
        }, {
          name: '22223232' 
        }
      ],
      domainIDList: [],
      votePatternList: [
        {
          name: 'ASYNC'
        }, {
          name: 'SYNC'
        }
      ],
      callbackTypeList: [
        {
          name: 'ALL'
        }, {
          name: 'SELF'
        }
      ],
      decisionTypeList: [
        {
          name: 'Full Vote'
        }, {
          name: 'One Vote'
        }, {
          name: 'Assign Num'
        }
      ],
      requireAuthIDList: []
    }
  },
  created () {
    this.getOption();
    this.getDomain();
    this.getPolicy();
  },
  methods: {
    async getPolicy () {
      // let data = await getPolicyList();
      let data = {
        data: [
          {"policyId":"policyId-0","policyName":"policy name-0"},
          {"policyId":"policyId-1","policyName":"policy name-1"},
          {"policyId":"policyId-2","policyName":"policy name-2"}
        ]
      }
      this.requireAuthIDList = [];
      this.requireAuthIDList.push(...data.data);
    },
    async getDomain () {
      // let data = await getDomainList();
      let data = {
        data: [
          {"desc":"test desc","domainId":"domainId-0"},
          {"desc":"test desc","domainId":"domainId-1"},
          {"desc":"test desc","domainId":"domainId-2"}
        ]
      }
      this.domainIDList = [];
      this.domainIDList.push(...data.data);
    },
    async getOption () {
      // let params = {
      //   name: ''
      // }
      // let data = await BDOptionInfo(params);
      let data = {
        "code":"000000",
        "data":[
            {
                "bdCode":"SysBD",
                "bdType":"system",
                "bdVersion":"1.0",
                "functionName":"BD_PUBLISH",
                "initPermission":"SLAVE",
                "initPolicy":"INIT_BD",
                "functions":[
                    {
                        "desc":"Identity设置",
                        "execPermission":"RS",
                        "execPolicy":"IDENTITY_SETTING",
                        "methodSign":"IDENTITY_SETTING",
                        "name":"IDENTITY_SETTING",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"BD发布",
                        "execPermission":"RS",
                        "execPolicy":"BD_PUBLISH",
                        "methodSign":"BD_PUBLISH",
                        "name":"BD_PUBLISH",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"Permission注册",
                        "execPermission":"RS",
                        "execPolicy":"PERMISSION_REGISTER",
                        "methodSign":"PERMISSION_REGISTER",
                        "name":"PERMISSION_REGISTER",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"Permission授权",
                        "execPermission":"RS",
                        "execPolicy":"AUTHORIZE_PERMISSION",
                        "methodSign":"AUTHORIZE_PERMISSION",
                        "name":"AUTHORIZE_PERMISSION",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"Permission撤销授权",
                        "execPermission":"RS",
                        "execPolicy":"CANCEL_PERMISSION",
                        "methodSign":"CANCEL_PERMISSION",
                        "name":"CANCEL_PERMISSION",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"注册policy",
                        "execPermission":"RS",
                        "execPolicy":"REGISTER_POLICY",
                        "methodSign":"REGISTER_POLICY",
                        "name":"REGISTER_POLICY",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"修改policy",
                        "execPermission":"RS",
                        "execPolicy":"MODIFY_POLICY",
                        "methodSign":"MODIFY_POLICY",
                        "name":"MODIFY_POLICY",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"注册RS",
                        "execPermission":"RS",
                        "execPolicy":"REGISTER_RS",
                        "methodSign":"REGISTER_RS",
                        "name":"REGISTER_RS",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"撤销RS",
                        "execPermission":"RS",
                        "execPolicy":"CANCEL_RS",
                        "methodSign":"CANCEL_RS",
                        "name":"CANCEL_RS",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"CA认证",
                        "execPermission":"RS",
                        "execPolicy":"CA_AUTH",
                        "methodSign":"CA_AUTH",
                        "name":"CA_AUTH",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"CA撤销",
                        "execPermission":"RS",
                        "execPolicy":"CA_CANCEL",
                        "methodSign":"CA_CANCEL",
                        "name":"CA_CANCEL",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"CA更新",
                        "execPermission":"RS",
                        "execPolicy":"CA_UPDATE",
                        "methodSign":"CA_UPDATE",
                        "name":"CA_UPDATE",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"节点加入",
                        "execPermission":"RS",
                        "execPolicy":"NODE_JOIN",
                        "methodSign":"NODE_JOIN",
                        "name":"NODE_JOIN",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"节点退出",
                        "execPermission":"RS",
                        "execPolicy":"NODE_LEAVE",
                        "methodSign":"NODE_LEAVE",
                        "name":"NODE_LEAVE",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"系统属性配置",
                        "execPermission":"RS",
                        "execPolicy":"SYSTEM_PROPERTY",
                        "methodSign":"SYSTEM_PROPERTY",
                        "name":"SYSTEM_PROPERTY",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"Identity BD 管理（froze/unfroze）",
                        "execPermission":"RS",
                        "execPolicy":"IDENTITY_BD_MANAGE",
                        "methodSign":"IDENTITY_BD_MANAGE",
                        "name":"IDENTITY_BD_MANAGE",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"identity kyc 设置",
                        "execPermission":"RS",
                        "execPolicy":"KYC_SETTING",
                        "methodSign":"KYC_SETTING",
                        "name":"KYC_SETTING",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"手续费设置：合约地址 & 收取地址",
                        "execPermission":"RS",
                        "execPolicy":"SET_FEE_CONFIG",
                        "methodSign":"SET_FEE_CONFIG",
                        "name":"SET_FEE_CONFIG",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"手续费费率配置",
                        "execPermission":"RS",
                        "execPolicy":"SET_FEE_RULE",
                        "methodSign":"SET_FEE_RULE",
                        "name":"SET_FEE_RULE",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"保存存证",
                        "execPermission":"RS",
                        "execPolicy":"SAVE_ATTESTATION",
                        "methodSign":"SAVE_ATTESTATION",
                        "name":"SAVE_ATTESTATION",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"打快照",
                        "execPermission":"RS",
                        "execPolicy":"BUILD_SNAPSHOT",
                        "methodSign":"BUILD_SNAPSHOT",
                        "name":"BUILD_SNAPSHOT",
                        "type":"SyetemAction"
                    }
                ]
            }, {
              "bdCode":"UserBD",
                "bdType":"system",
                "bdVersion":"1.0",
                "functionName":"BD_PUBLISH",
                "initPermission":"SLAVE",
                "initPolicy":"INIT_BD",
                "functions":[
                    {
                        "desc":"Identity设置",
                        "execPermission":"RS",
                        "execPolicy":"IDENTITY_SETTING",
                        "methodSign":"IDENTITY_SETTING",
                        "name":"IDENTITY_SETTING",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"BD发布",
                        "execPermission":"RS",
                        "execPolicy":"BD_PUBLISH",
                        "methodSign":"BD_PUBLISH",
                        "name":"BD_PUBLISH",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"Permission注册",
                        "execPermission":"RS",
                        "execPolicy":"PERMISSION_REGISTER",
                        "methodSign":"PERMISSION_REGISTER",
                        "name":"PERMISSION_REGISTER",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"Permission授权",
                        "execPermission":"RS",
                        "execPolicy":"AUTHORIZE_PERMISSION",
                        "methodSign":"AUTHORIZE_PERMISSION",
                        "name":"AUTHORIZE_PERMISSION",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"Permission撤销授权",
                        "execPermission":"RS",
                        "execPolicy":"CANCEL_PERMISSION",
                        "methodSign":"CANCEL_PERMISSION",
                        "name":"CANCEL_PERMISSION",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"注册policy",
                        "execPermission":"RS",
                        "execPolicy":"REGISTER_POLICY",
                        "methodSign":"REGISTER_POLICY",
                        "name":"REGISTER_POLICY",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"修改policy",
                        "execPermission":"RS",
                        "execPolicy":"MODIFY_POLICY",
                        "methodSign":"MODIFY_POLICY",
                        "name":"MODIFY_POLICY",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"注册RS",
                        "execPermission":"RS",
                        "execPolicy":"REGISTER_RS",
                        "methodSign":"REGISTER_RS",
                        "name":"REGISTER_RS",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"撤销RS",
                        "execPermission":"RS",
                        "execPolicy":"CANCEL_RS",
                        "methodSign":"CANCEL_RS",
                        "name":"CANCEL_RS",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"CA认证",
                        "execPermission":"RS",
                        "execPolicy":"CA_AUTH",
                        "methodSign":"CA_AUTH",
                        "name":"CA_AUTH",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"CA撤销",
                        "execPermission":"RS",
                        "execPolicy":"CA_CANCEL",
                        "methodSign":"CA_CANCEL",
                        "name":"CA_CANCEL",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"CA更新",
                        "execPermission":"RS",
                        "execPolicy":"CA_UPDATE",
                        "methodSign":"CA_UPDATE",
                        "name":"CA_UPDATE",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"节点加入",
                        "execPermission":"RS",
                        "execPolicy":"NODE_JOIN",
                        "methodSign":"NODE_JOIN",
                        "name":"NODE_JOIN",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"节点退出",
                        "execPermission":"RS",
                        "execPolicy":"NODE_LEAVE",
                        "methodSign":"NODE_LEAVE",
                        "name":"NODE_LEAVE",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"系统属性配置",
                        "execPermission":"RS",
                        "execPolicy":"SYSTEM_PROPERTY",
                        "methodSign":"SYSTEM_PROPERTY",
                        "name":"SYSTEM_PROPERTY",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"Identity BD 管理（froze/unfroze）",
                        "execPermission":"RS",
                        "execPolicy":"IDENTITY_BD_MANAGE",
                        "methodSign":"IDENTITY_BD_MANAGE",
                        "name":"IDENTITY_BD_MANAGE",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"identity kyc 设置",
                        "execPermission":"RS",
                        "execPolicy":"KYC_SETTING",
                        "methodSign":"KYC_SETTING",
                        "name":"KYC_SETTING",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"手续费设置：合约地址 & 收取地址",
                        "execPermission":"RS",
                        "execPolicy":"SET_FEE_CONFIG",
                        "methodSign":"SET_FEE_CONFIG",
                        "name":"SET_FEE_CONFIG",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"手续费费率配置",
                        "execPermission":"RS",
                        "execPolicy":"SET_FEE_RULE",
                        "methodSign":"SET_FEE_RULE",
                        "name":"SET_FEE_RULE",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"保存存证",
                        "execPermission":"RS",
                        "execPolicy":"SAVE_ATTESTATION",
                        "methodSign":"SAVE_ATTESTATION",
                        "name":"SAVE_ATTESTATION",
                        "type":"SyetemAction"
                    },
                    {
                        "desc":"打快照",
                        "execPermission":"RS",
                        "execPolicy":"BUILD_SNAPSHOT",
                        "methodSign":"BUILD_SNAPSHOT",
                        "name":"BUILD_SNAPSHOT",
                        "type":"SyetemAction"
                    }
                ]
            }
        ],
        "msg":"SUCCESS"
      }
      this.dbNameList = [];
      this.functionNameList = [];
      this.dbNameList.push(...data.data);
      this.functionNameList.push(...data.data[0].functions);
      this.ruleForm.bDName = this.dbNameList[0].bdCode;
      // this.ruleForm.functionName = this.dbNameList[0].functions[0].name;
      // console.log(data)
    }
  },
  components: {
    RegisterPolicy,
    ModifyPolicy,
    IdentitySetting,
    PermissionRegister,
    AuthorizePermission,
    CancelPermission,
    KYCSetting,
    SaveAttestation,
    SystemProperty,
    BDSpecification,
    SetFeeRule,
    IdentityBDManage
  }
}
</script>
<style scoped lang="scss">
.bd-box {
  width: 710px;
  background-color: #fff;
  padding: 40px 40px;
  padding-right: 100px;
  float: left;
  .general-information {
    .title {
      color: #333333;
      font-size: 18px;
      font-weight:500;
    }
    .general-form {
      margin-top: 25px;
    }
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
  .vote-pattern-tips {
    line-height: 25px;
    font-size: 12px;
    color: #333333;
    padding-left: 10px;
  }
}
</style>