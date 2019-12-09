<template>
  <div class="home-page">
    <div class="home-header">
      <div class="header-item">
        <div class="header-left">
          <div class="logo">
            <img src="../assets/logo.png" alt="logo">
          </div>
          <ul class="option-list">
            <li :class="{'active-menu': $route.path.indexOf(v.path) !== -1}" v-for="(v, k) in menuList" :key="k" @click="changeTab(v)">{{v.name}}</li>
          </ul>
        </div>
        <div class="header-right">
          <el-popover
            placement="top"
            width="160"
            popper-class="header-user-pop"
            trigger="hover">
            <ul class="user-info-box">
              <li>
                <i class="el-icon-user"></i>
                <span>userd-2j</span>
              </li>
              <li>
                <i class="el-icon-more-outline"></i>
                <span>Log out</span>
              </li>
            </ul>
            <img src="../assets/logo.png" alt="head" slot="reference">
          </el-popover>
          <span>DRS Configuration</span>
        </div>
      </div>
    </div>
    <router-view />
  </div>
</template>
<script>

export default {
  name: 'Home',
  data () {
    return {
      menuList: [
        {
          name: 'DAPP Store',
          path: 'store',
          pathName: 'Store'
        }, {
          name: 'BD Execution',
          path: 'BD',
          pathName: 'BD'
        }
      ]
    }
  },
  methods: {
    changeTab (v) {
      this.$router.push({name: v.pathName});
    }
  }
}
</script>
<style lang="scss" scoped>
.home-page {
  min-height: 100%;
  background-color: #F2F5FC;
  padding-bottom: 50px;
  .home-header {
    width: 100%;
    height: 60px;
    background-color: #fff;
    box-shadow: 1px 0px 5px 0px rgba(1,110,255,0.13);
    .header-item {
      max-width: 900px;
      margin: 0 auto;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .header-left {
        display: flex;
        align-items: center;
        .logo {
          width: 110px;
          height: 34px;
          background-color: #ccc;
          img {
            width: 100%;
            height: 100%;
          }
        }
        .option-list {
          margin-left: 65px;
          display: flex;
          justify-content: space-between;
          align-items: center;
          width: 245px;
          li {
            cursor: pointer;
            color: #666666;
            font-size: 16px;
            float: left;
            font-weight: 500;
          }
          .active-menu {
            color: #2146A9;
          }
        }
      }
      .header-right {
        display: flex;
        align-items: center;
        img {
          width: 24px;
          height: 24px;
          border-radius: 50%;
          cursor: pointer;
          margin-right: 15px;
        }
        span {
          cursor: pointer;
          color: #666666;
          font-size: 16px;
        }
      }
    }
  }
}
</style>
<style lang="scss">
.header-user-pop {
  width: auto !important;
  padding: 0 !important;
  .user-info-box {
    li {
      height: 40px;
      line-height: 40px;
      border-bottom: 1px solid rgba(21,23,32,0.08);
      padding: 0 15px;
      transition: all .2s;
      cursor: pointer;
      i {
        margin-right: 15px;
      }
      & + li {
        border: 0;
      }
      &:hover {
        background-color: rgba(248,250,255,1);;
      }
    }
  }
}
</style>