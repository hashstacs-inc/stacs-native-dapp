<template>
  <div class="store" v-loading="loading">
    <div class="search">
      <div class="title">Name</div>
      <input type="text" class="search-inp" placeholder="Search the dapp" @input="searchList" v-model="searchApp">
      <i class="el-icon-search"></i>
    </div>
    <ul class="app-list" v-if="appList.length > 0">
      <li v-for="(v, k) in appList" :key="k" :edge="v.edge">
        <p class="logo">
          <img :src="v.icon | formatIcon" alt="logo">
        </p>
        <p class="name">{{v.showName}}</p>
        <p class="operation" @click="handleClick(v)" v-loading="v.loading">
          <span class="text">{{returnStaus(v.status)}}</span>
        </p>
        <el-tooltip effect="dark" :content="v.errorText" placement="top-start" v-if="v.errorText" popper-class="store-deapp-tips" :hide-after="0">
          <p class="error">Failed&nbsp;!</p>
        </el-tooltip>
      </li>
    </ul>
    <div v-else class="no-data">
      <img src="../../../assets/img/blank.png" alt="logo">
      <p>NO data</p>
    </div>
    <el-dialog
      title="Tips"
      :visible.sync="configVisible"
      width="520px">
      <p>Do you want to use the default etup?</p>
      <p slot="footer" class="dialog-footer">
        <el-button type="primary" @click="configConfirm">确 定</el-button>
        <el-button @click="configCancel">取 消</el-button>
      </p>
    </el-dialog>
  </div>
</template>
<script>
import { getAppList, downloadApp, installDeapp, 
  startDeapp, postDeappConfig, getDeappConfig } from '@/api/storeApi';
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
      loading: false
    }
  },
  created () {
    this.getAppLists();
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
        case 'STOPPED':
          return 'Install';
          break;
        case null:
          return 'Download';
          break;
      }
    },
    async configConfirm () {
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
        // 下载成功
        this.$set(v, 'status', 'DOWNLOADED');
      } else {
        this.$set(v, 'errorText', data.msg);
      }
      this.$set(v, 'loading', false);
    },
    async installApp (v) {
      this.$set(v, 'loading', true);
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
      } else {
        this.$set(v, 'errorText', data.msg);
      }
    },
    async handleClick (v) {
      this.$set(v, 'errorText', null);
      if (!v.status) {
        // 未下载状态
        this.downloadApp(v);
      } else if (v.status === 'DOWNLOADED') {
        // 已下载状态
        this.configVisible = true;
        this.currentItem = v;
      } else if (v.status === 'INITIALIZED' || v.status === 'STOPPED') {
        // 已配置,已初始化
        this.installApp(v);
      } else if (v.status === 'RUNNING') {
        window.open(window.location.origin + '/' + v.name);
      }
    },
    async getAppLists () {
      this.loading = true;
      let data = await getAppList();
      data.data.forEach(v => {
        v['loading'] = false;
      });
      this.appList = JSON.parse(JSON.stringify(data.data));
      this.copyAppList = JSON.parse(JSON.stringify(data.data));
      this.loading = false;
    }
  }
}
</script>
<style scoped lang="scss">
.store {
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
      .logo {
        width: 75px;
        height: 75px;
        border-radius: 8px;
        background-color: #DADFF1;
        img {
          width: 100%;
          height: 100%;
        }
      }
      .name {
        color: #333333;
        font-size: 12px;
      }
      .operation {
        // width: 100%;
        height: 22px;
        background-color: #053C8C;
        position: relative;
        border-radius: 2px;
        text-align: center;
        line-height: 20px;
        padding: 0 3px;
        min-width: 75px;
        cursor: pointer;
        .text {
          position: relative;
          z-index: 10;
          font-size: 12px;
          color: #fff;
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