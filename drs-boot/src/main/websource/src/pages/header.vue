<template>
  <div class="home-header">
    <div class="header-item">
      <div class="header-left">
        <div class="logo" @click="$router.push({ name: 'Home' })">
          <img src="../assets/img/logo.png" alt="logo">
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
              <img src="../assets/img/user.png" alt="logo">
              <span>userd-2j</span>
            </li>
            <li>
              <img src="../assets/img/logout.png" alt="logo">
              <span>Log out</span>
            </li>
          </ul>
          <img src="../assets/img/userInfo.png" alt="head" slot="reference">
        </el-popover>
        <span @click="$router.push({ name: 'DRSConfig' })">DRS Configuration</span>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'HomeHeader',
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
<style scoped lang="scss">
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
        cursor: pointer;
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
</style>
<style lang="scss">
.header-user-pop {
  .user-info-box {
    li {
      display: flex;
      align-items: center;
      img {
        width: 20px;
        height: 20px;
        margin-right: 10px;
      }
    }
    
  }
}
</style>