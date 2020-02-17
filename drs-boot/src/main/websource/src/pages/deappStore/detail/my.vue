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
          <img :src="v.icon" alt="logo">
        </div>
        <div class="doc-box">
          <p class="title">{{v.showName}}</p>
          <p class="detail">{{v.remark}}</p>
        </div>
        <div class="operation">
          <p class="text" @click="operationClick(v)" v-loading="v.loading">{{returnStatus(v.status)}}</p>
          <el-tooltip effect="dark" :content="v.errorText" placement="top-start" 
            popper-class="my-deapp-tips" :hide-after="0" v-if="v.errorText">
            <p class="error">Failed !</p>
          </el-tooltip>
          <p class="uninstall" @click="unInstall(v)" v-if="v.status === 'RUNNING'">Uninstall</p>
        </div>
      </div>
    </template>
    <template v-else>
      <div class="no-data">
        <img src="../../../assets/img/blank.png" alt="logo">
        <p @click="$router.push({ name: 'Library' })">DAPP Store</p>
      </div>
    </template>
    <el-dialog
      title="System"
      :visible.sync="configVisible"
      width="520px">
      <p>Do you want to use the default setup?</p>
      <p slot="footer" class="dialog-footer">
        <el-button type="primary" @click="configConfirm">YES</el-button>
        <el-button @click="configCancel">NO</el-button>
      </p>
    </el-dialog>
    <el-dialog
      title="System"
      :visible.sync="uninstallVisible"
      width="520px">
      <p>All configuration and data will be wipe out</p>
      <p>once the application is Uninstall.</p>
      <p>Please confirm to proceed.</p>
      <p slot="footer" class="dialog-footer">
        <el-button type="primary" @click="uninstallVisible = false">Back</el-button>
        <el-button @click="uninstallConfirm">Uninstall</el-button>
      </p>
    </el-dialog>
  </div>
</template>
<script>
import { getMyAppList, getDeappConfig, postDeappConfig, 
  startDeapp, installDeapp, uninstallApp } from '@/api/storeApi';
import { notify } from '@/common/util';

export default {
  name: 'My',
  data () {
    return {
      appList: [],
      copyAppList: [],
      configVisible: false,
      uninstallVisible: false,
      currentItem: {},
      currentUninstall: {},
      myAppInp: '',
      loading: false,
      installingTimer: null
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
    // getData
    async getList () {
      this.loading = true;
      let data = await getMyAppList({ slient: true });
      if (data.code === '000000' && data.data) {
        this.appList = JSON.parse(JSON.stringify(data.data));
        this.copyAppList = JSON.parse(JSON.stringify(data.data));
        let filterStatus = this.appList.filter(v => v.status === 'INSTALLING');
        filterStatus.forEach(v => {
          v['loading'] = true;
        });
        clearInterval(this.installingTimer)
        this.installingTimer = setInterval(async () => {
          let filterStatus = this.appList.filter(v => v.status === 'INSTALLING');
          filterStatus.forEach(v => {
            v['loading'] = true;
          });
          if (filterStatus.length > 0) {
            let res = await getMyAppList({ slient: true });
            filterStatus.forEach(v => {
              let cloneItem = res.data.filter(val => val.name === v.name);
              filterStatus.forEach(val => {
                if (val.name === cloneItem[0].name) {
                  Object.assign(val, cloneItem[0])
                  if (val.status === 'RUNNING') {
                    val.loading = false;
                  }
                }
              });
            });
          } else {
            clearInterval(this.installingTimer);
          }
        }, 4000);
        this.loading = false;
      } else {
        this.appList = [];
        this.copyAppList = [];
      }
      this.loading = false;
    },
    // status
    returnStatus (state) {
      /* eslint-disable */
      switch(state) {
        case 'DOWNLOADED':
          return 'Configuration';
          break;
        case 'INITIALIZED':
          return 'Install';
          break;
        case 'INSTALLING':
          return 'Installing';
          break;
        case 'RUNNING':
          return 'Open';
          break;
        case 'STOPPED':
          return 'Install';
          break;
      }
    },
    async uninstallConfirm () {
      this.loading = true;
      this.uninstallVisible = false;
      let params = {
        name: this.currentUninstall.name,
        data: {
          appName: this.currentUninstall.name
        },
        notify: notify.any,
        slient: true
      }
      let data = await uninstallApp(params);
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
      this.uninstallVisible = false;
      this.loading = false;
    },
    unInstall (v) {
      this.currentUninstall = v;
      this.uninstallVisible = true;
    },
    // default configuration
    async configConfirm () {
      this.configVisible = false;
      this.loading = true;
      // Downloaded
      // Initialize after configuration

      // Query default configuration
      let getConfig = {
        name: this.currentItem.name,
        notify: notify.error,
        slient: true
      }
      let defaultConfig = await getDeappConfig(getConfig);
      if (defaultConfig.code === '000000') {
        // Use default configuration
        let params = {
          name: this.currentItem.name,
          notify: notify.any,
          data: Object.assign({ appName: this.currentItem.name }, defaultConfig.data),
          slient: true
        }
        let postData = await postDeappConfig(params);
        if (postData.code === '000000') {
          // After successful configuration, initialize deapp
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
      this.loading = false;
    },
    // Custom configuration jump
    configCancel () {
      this.$store.commit('changeStoreMenu', 3);
      this.$router.push({name: 'AppConfig', query: { name: this.currentItem.name }});
    },
    // install
    async installApp (v) {
      this.$set(v, 'loading', true);
      this.$set(v, 'status', 'INSTALLING');
      let params = {
        name: v.name,
        slient: true,
        notify: notify.any,
        timeout: 0
      }
      let data = await installDeapp(params);
      this.$set(v, 'loading', false);
      if (data.code === '000000') {
        this.$set(v, 'status', 'RUNNING');
      } else if (data.code === '210011') {
        window.location.reload();
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
        // Configured, initialized
        this.installApp(v);
      } else if (v.status === 'RUNNING') {
        window.open(window.location.origin + '/' + v.name);
      }
    }
  },
  created () {
    this.getList();
    this.$store.commit('changeStoreMenu', this.$route.meta.menu);
  },
  beforeDestroy () {
    clearInterval(this.installingTimer);
  }
}
</script>
<style scoped lang="scss">
.my {
  min-height: 550px;
  padding: 40px 35px 20px 35px;
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
      overflow: hidden;
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
        padding: 0 5px;
        min-width: 85px;
        height: 25px;
        font-size: 12px;
        background-color: #053C8C;
        border-radius:2px;
        color: #fff;
        line-height: 25px;
        cursor: pointer;
        text-align: center;
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
      line-height: 40px;
      color: #2146A9;
      font-size: 14px;
      width: 140px;
      height: 40px;
      border-radius:1px;
      border:1px solid rgba(5,60,140,1);
      cursor: pointer;
      margin: 0 auto;
      margin-top: 10px;
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