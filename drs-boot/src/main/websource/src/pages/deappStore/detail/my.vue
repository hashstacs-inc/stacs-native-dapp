<template>
  <div class="my" v-loading="loading">
    <div class="search">
      <div class="title">Name</div>
      <input type="text" class="search-inp" placeholder="Search the dapp" v-model="myAppInp" @input="searchList">
      <i class="el-icon-search"></i>
    </div>
    <template v-if="appList.length > 0">
      <div class="my-list" v-for="(v, k) in appList" :key="k">
        <div class="logo">
          <img :src="v.icon | formatIcon" alt="logo">
        </div>
        <div class="doc-box">
          <p class="title">{{v.showName}}</p>
          <p class="detail">{{v.remark}}</p>
          <!-- <p>介绍资料：原名:Gestures for Chrome(TM)汉化版.方便,快捷汉化版.方便,快捷汉化版.方便,快捷汉化版.方便,快捷汉化版.方便,快捷</p> -->
        </div>
        <div class="operation">
          <p class="text" @click="operationClick(v)" v-loading="v.loading">{{returnStatus(v.status)}}</p>
          <el-tooltip effect="dark" :content="v.errorText" placement="top-start" 
            popper-class="my-deapp-tips" :hide-after="0" v-if="v.errorText">
            <p class="error">Failed !</p>
          </el-tooltip>
          <p class="uninstall" @click="unInstall(v)">Stop</p>
        </div>
      </div>
    </template>
    <template v-else>
      <div class="no-data">
        <img src="../../../assets/img/blank.png" alt="logo">
        <p>NO data</p>
      </div>
    </template>
    <el-dialog
      title="System"
      :visible.sync="configVisible"
      width="520px">
      <p>Do you want to use the default etup?</p>
      <p slot="footer" class="dialog-footer">
        <el-button type="primary" @click="configConfirm">YES</el-button>
        <el-button @click="configCancel">NO</el-button>
      </p>
    </el-dialog>
    <el-dialog
      title="System"
      :visible.sync="stopVisible"
      width="520px">
      <p>All configuration and data will be wipe out</p>
      <p>once the application is stop.</p>
      <p>Please confirm to proceed.</p>
      <p slot="footer" class="dialog-footer">
        <el-button type="primary" @click="stopVisible = false">Back</el-button>
        <el-button @click="stopConfirm">Stop</el-button>
      </p>
    </el-dialog>
  </div>
</template>
<script>
import { getMyAppList, getDeappConfig, postDeappConfig, 
  startDeapp, installDeapp, stopApp } from '@/api/storeApi';
import { notify } from '@/common/util';

export default {
  name: 'My',
  data () {
    return {
      appList: [],
      copyAppList: [],
      configVisible: false,
      stopVisible: false,
      currentItem: {},
      currentStop: {},
      myAppInp: '',
      loading: false
    }
  },
  methods: {
    searchList () {
      if (!this.myAppInp) {
        this.appList = this.copyAppList;
      } else {
        let filter = [];
        this.copyAppList.forEach(v => {
          if (v.showName.indexOf(this.myAppInp) !== -1) {
            filter.push(v);
          }
        });
        this.appList = filter;
      }
    },
    async getList () {
      this.loading = true;
      let data = await getMyAppList();
      if (data.data) {
        this.appList = JSON.parse(JSON.stringify(data.data));
        this.copyAppList = JSON.parse(JSON.stringify(data.data));
      }
      this.loading = false;
    },
    returnStatus (state) {
      /* eslint-disable */
      switch(state) {
        case 'DOWNLOADED':
          return 'Configuration';
          break;
        case 'INITIALIZED':
          return 'Install';
          break;
        case 'RUNNING':
          return 'Open';
          break;
        case 'STOPPED':
          return 'Install';
          break;
      }
    },
    async stopConfirm () {
      let params = {
        name: this.currentStop.name,
        data: {
          appName: this.currentStop.name
        },
        notify: notify.any,
        slient: true
      }
      let data = await stopApp(params);
      if (data.code === '000000') {
        this.$alert('Successfully uninstalled!', 'System', {
          confirmButtonText: 'Confirm'
        }).then(() => {
          this.getList();
        });
      } else {
        this.$alert('Uninstall failed, please try again!', 'System', {
          confirmButtonText: 'Confirm'
        });
      }
      this.stopVisible = false;
    },
    unInstall (v) {
      this.currentStop = v;
      this.stopVisible = true;
    },
    async configConfirm () {
      // 已下载状态
      // 使用默认配置，后初始化app

      // 查询默认配置
      let getConfig = {
        name: this.currentItem.name,
        notify: notify.error,
        slient: true
      }
      let defaultConfig = await getDeappConfig(getConfig);
      if (defaultConfig.code === '000000') {
        // 使用默认配置
        let params = {
          name: this.currentItem.name,
          notify: notify.any,
          data: Object.assign({ appName: this.currentItem.name }, defaultConfig.data),
          slient: true
        }
        let postData = await postDeappConfig(params);
        if (postData.code === '000000') {
          // 配置成功后，初始化deapp
          let startParams = {
            name: this.currentItem.name,
            slient: true
          }
          let startData = await startDeapp(startParams);
          if (startData.code === '000000') {
            this.$set(this.currentItem, 'status', 'INITIALIZED');
          } else {
            this.$set(this.currentItem, 'errorText', startData.msg);
          }
        } else {
          this.$set(this.currentItem, 'errorText', postData.msg);
        }
      } else {
        this.$set(this.currentItem, 'errorText', defaultConfig.msg);
      }
      this.configVisible = false;
    },
    configCancel () {
      this.$store.commit('changeStoreMenu', 3);
      this.$router.push({name: 'AppConfig', query: { name: this.currentItem.name }});
    },
    async installApp (v) {
      this.$set(v, 'loading', true);
      let params = {
        name: v.name,
        slient: true,
        notify: notify.success,
        timeout: 0
      }
      let data = await installDeapp(params);
      this.$set(v, 'loading', false);
      if (data.code === '000000') {
        this.$set(v, 'status', 'RUNNING');
      } else {
        this.$set(v, 'errorText', data.msg);
      }
    },
    operationClick (v) {
      this.currentItem = v;
      this.$set(v, 'errorText', null);
      if (this.currentItem.status === 'DOWNLOADED') {
        this.configVisible = true;
      } else if (this.currentItem.status === 'INITIALIZED' || this.currentItem.status === 'STOPPED') {
        // 已配置,已初始化
        this.installApp(v);
      } else if (v.status === 'RUNNING') {
        window.open(window.location.origin + '/' + v.name);
      }
    }
  },
  created () {
    this.getList();
  }
}
</script>
<style scoped lang="scss">
.my {
  min-height: 550px;
  .search {
    display: flex;
    height: 30px;
    align-items: center;
    position: relative;
    .title {
      margin-right: 20px;
      color: #333333;
      font-size: 16px;
      font-weight: 500;
    }
    .search-inp {
      width: 430px;
      height: 100%;
      border: 1px solid #E7E7E7;
      padding-left: 30px;
      padding-right: 10px;
      outline: none;
      font-size: 14px;
      font-weight: 400;
      line-height: 30px;
      &::-webkit-input-placeholder { /* WebKit browsers */
        color: #C6C6C6;
        font-size: 14px;
      }
      &::-moz-placeholder { /* Mozilla Firefox 19+ */
        color: #C6C6C6;
        font-size: 14px;
      }
      &::-ms-input-placeholder { /* Internet Explorer 10+ */
        color: #C6C6C6;
        font-size: 14px;
      }
    }
    .el-icon-search {
      position: absolute;
      left: 72px;
      color: #616161;
    }
  }
  .my-list {
    height: 75px;
    width: 100%;
    margin-top: 48px;
    .logo {
      width: 75px;
      height: 75px;
      background-color: #DADFF1;
      border-radius:8px;
      float: left;
      margin-right: 20px;
      img {
        width: 100%;
        height: 100%;
      }
    }
    .doc-box {
      float: left;
      height: 100%;
      padding-top: 5px;
      .title {
        color: #333333;
        font-size: 14px;
      }
      .detail {
        color: #666666;
        font-size: 12px;
        margin-top: 5px;
        width: 400px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        margin-top: 10px;
        & + p {
          color: #666666;
          font-size: 12px;
          margin-top: 5px;
          width: 400px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }
    .operation {
      height: 100%;
      float: right;
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      align-items: center;
      .text {
        padding: 0 10px;
        height: 25px;
        font-size: 12px;
        background-color: #053C8C;
        border-radius:2px;
        color: #fff;
        line-height: 25px;
        cursor: pointer;
      }
      .error {
        color: #F17070;
        font-size: 12px;
        cursor: default;
      }
      .uninstall {
        color: #999999;
        font-size: 12px;
        cursor: pointer;
      }
    }
  }
  .my-list::after {
    content: '';
    height: 0;
    width: 0;
    display: block;
    clear: both;
  }
  .no-data {
    padding-top: 60px;
    text-align: center;
    p {
      text-align: center;
      color: #999999;
      font-size: 12px;
    }
  }
}
</style>
<style lang="scss">
.my-deapp-tips {
  background-color: #fff !important;
  box-shadow:0px 0px 23px 1px rgba(1,110,255,0.13) !important;
  color: #616161 !important;
  .popper__arrow::after {
    border-top-color: #fff !important;
  }
}
</style>