<template>
  <div class="store" v-loading="loading">
    <div class="search">
      <div class="title">Name</div>
      <input type="text" class="search-inp" placeholder="Search the dapp" @input="searchList" v-model="searchApp">
      <i class="el-icon-search"></i>
    </div>
    <ul class="app-list" v-if="appList.length > 0">
      <li v-for="(v, k) in appList" :key="k" :edge="k % 5 === 4">
        <p class="logo">
          <img :src="v.icon" alt="logo">
        </p>
        <el-tooltip effect="dark" :content="v.showName" placement="top-start">
          <p class="name">{{v.showName}}</p>
        </el-tooltip>
        <p class="operation" @click="handleClick(v)" v-loading="v.loading" :class="{'start': v.status === 'STOPPED' || v.status === 'STARTING'}">
          <span class="text">
            <!-- {{v.status === 'STOPPED' ? v.loading ? 'Starting' : returnStaus(v.status) : returnStaus(v.status)}} -->
            {{returnStaus(v.status)}}
          </span>
        </p>
        <p class="error">
          <el-popover
            placement="top-start"
            trigger="hover"
            v-if="v.errorText"
            :content="v.errorText">
            <span slot="reference">Failed !</span>
          </el-popover>
        </p>
      </li>
    </ul>
    <div v-else class="no-data">
      <img src="../../../assets/img/blank.png" alt="logo">
      <p>No Data</p>
    </div>
    <el-dialog
      title="Tips"
      :visible.sync="configVisible"
      width="520px">
      <p>Do you want to use the default setup?</p>
      <p slot="footer" class="dialog-footer">
        <el-button type="primary" @click="configConfirm">YES</el-button>
        <el-button @click="configCancel">NO</el-button>
      </p>
    </el-dialog>
  </div>
</template>
<script>
import { getAppList, downloadApp, installDeapp, 
  initDeapp, postDeappConfig, getDeappConfig, startDeapp } from '@/api/storeApi';
import { notify } from '@/common/util';

export default {
  name: 'Store',
  data () {
    return {
      appList: [],
      copyAppList: [],
      searchApp: '',
      configVisible: false,
      currentItem: {},
      loading: false,
      installingTimer: null
    }
  },
  created () {
    this.getAppLists();
    this.$store.commit('changeStoreMenu', this.$route.meta.menu);
  },
  beforeDestroy () {
    clearInterval(this.installingTimer);
  },
  methods: {
    searchList () {
      if (!this.searchApp) {
        this.appList = this.copyAppList;
      } else {
        let filter = [];
        this.copyAppList.forEach(v => {
          if (v.showName.indexOf(this.searchApp) !== -1) {
            filter.push(v);
          }
        });
        this.appList = filter;
      }
    },
    // return status
    returnStaus (str) {
      /* eslint-disable */
      switch (str) {
        case 'DOWNLOADED':
          return 'Configuration';
          break;
        case 'INITIALIZED':
          return 'Install';
          break;
        case 'RUNNING':
          return 'Open';
          break;
        case 'INSTALLING':
          return 'Installing';
          break;
        case 'STOPPED':
          return 'Start';
          break;
        case 'INSTALLERROR':
          return 'Install';
          break;
        case 'STARTING':
          return 'Starting';
          break;
        case 'UPGRADING':
          return 'Updating';
          break;
        case null:
          return 'Download';
          break;
      }
    },
    // default configuration
    async configConfirm () {
      this.configVisible = false;
      this.loading = true;
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
          let startData = await initDeapp(startParams);
          if (startData.code === '000000') {
            this.$set(this.currentItem, 'status', 'INITIALIZED');
            this.$alert('Configuration success.', 'System', {
              confirmButtonText: 'YES'
            });
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
    // download
    async downloadApp (v) {
      let params = {
        params: {
          filePath: v.downloadUrl
        },
        timeout: 0,
        notify: notify.any,
        slient: true
      }
      this.$set(v, 'loading', true);
      let data = await downloadApp(params);
      if (data.code === '000000') {
        // success
        this.$set(v, 'status', 'DOWNLOADED');
      } else {
        this.$set(v, 'errorText', data.msg);
      }
      this.$set(v, 'loading', false);
    },
    // install app
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
        this.$set(v, 'status', 'INITIALIZED');
      }
    },
    async handleClick (v) {
      this.$set(v, 'errorText', null);
      if (v.status === 'INSTALLING') return;
      if (!v.status && !v.loading) {
        // Status not downloaded
        this.downloadApp(v);
      } else if (v.status === 'DOWNLOADED' && !v.loading) {
        // Downloaded status
        this.configVisible = true;
        this.currentItem = v;
      } else if (v.status === 'INITIALIZED' && !v.loading || v.status === 'INSTALLERROR' && !v.loading) {
        // Configured, initialized
        this.installApp(v);
      } else if (v.status === 'RUNNING' && !v.loading) {
        window.open(window.location.origin + '/' + v.name);
      } else if (v.status === 'STOPPED') {
        this.startApp(v);
      }
    },
    async startApp (v) {
      v.loading = true;
      v.status = 'STARTING';
      let data = await startDeapp({
        name: v.name,
        notify: notify.any,
        slient: true,
        timeout: 0
      });
      if (data.code === '000000') {
        this.getAppLists();
        v.loading = false;
      } else {
        v.errorText = data.msg;
      }
    },
    // getList
    async getAppLists () {
      this.loading = true;
      let data = await getAppList({ slient: true });
      if (data.code === '000000') {
        data.data.forEach(v => {
          v['loading'] = false;
        });
        this.appList = JSON.parse(JSON.stringify(data.data));
        this.copyAppList = JSON.parse(JSON.stringify(data.data));
        let filterStatus = this.appList.filter(v => v.status === 'INSTALLING');
        let filterStarting = this.appList.filter(v => v.status === 'STARTING');
        let filterUpgrading = this.appList.filter(v => v.status === 'UPGRADING');
        let arr = filterStatus.concat(filterStarting);
        let resultArr = arr.concat(filterUpgrading);
        resultArr.forEach(v => {
          v['loading'] = true;
        });
        clearInterval(this.installingTimer);
        this.installingTimer = setInterval(async () => {
          if (resultArr.length > 0) {
            let res = await getAppList({ slient: true });
            resultArr.forEach((v, k) => {
              let cloneItem = res.data.filter(val => val.name === v.name);
              if (v.name === cloneItem[0].name) {
                Object.assign(v, cloneItem[0])
                if (v.status === 'RUNNING') {
                  v.loading = false;
                  resultArr.splice(k, 1);
                }
              }
            });
          } else {
            clearInterval(this.installingTimer);
          }
        }, 4000);
        this.loading = false;
      }
    }
  }
}
</script>
<style scoped lang="scss">
.store {
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
  .app-list {
    margin-top: 48px;
    display: flex;
    flex-wrap: wrap;
    li {
      width: 75px;
      height: 135px;
      margin-right: 65px;
      margin-bottom: 40px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: space-between;
      position: relative;
      text-align: center;
      .logo {
        width: 75px;
        height: 75px;
        background-color: #DADFF1;
        border-radius: 8px;
        overflow: hidden;
        img {
          width: 100%;
          height: 100%;
        }
      }
      .name {
        color: #333333;
        font-size: 12px;
        overflow: hidden;
        text-overflow: ellipsis;
        width: 100px;
        white-space: nowrap;
      }
      .operation {
        // width: 100%;
        height: 22px;
        background-color: #053C8C;
        position: relative;
        border-radius: 2px;
        text-align: center;
        line-height: 18px;
        padding: 0 5px;
        min-width: 85px;
        cursor: pointer;
        border: 1px solid transparent;
        .text {
          position: relative;
          z-index: 10;
          font-size: 12px;
          color: #fff;
        }
      }
      .start {
        background-color: #EAFFE5;
        border-color: #EAFFE5;
        .text {
          color: #65B03F;
        }
      }
      .error {
        color: #F17070;
        font-size: 12px;
        position: absolute;
        left: 50%;
        top: 102%;
        transform: translateX(-50%);
        min-width: 60px;
        text-align: center;
        cursor: default;
      }
    }
    li[edge] {
      margin-right: 0px;
    }
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
.store-deapp-tips {
  background-color: #fff !important;
  box-shadow:0px 0px 23px 1px rgba(1,110,255,0.13) !important;
  color: #616161 !important;
  .popper__arrow::after {
    border-top-color: #fff !important;
  }
}
</style>