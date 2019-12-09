<template>
  <div class="bd-execution">
    <ul class="menu-box">
      <li :class="{active: $route.name === v.pathName || menuActive === k}" @click="changeMenu(v, k)" v-for="(v, k) in menuList" :key="k">{{v.name}}</li>
    </ul>
    <router-view />
  </div>
</template>
<script>

export default {
  name: 'DBExecution',
  data () {
    return {
      menuActive: 0,
      menuList: [
        {
          name: 'BD Execution',
          path: 'execution',
          pathName: 'Execution'
        }, {
          name: 'BD Execution History',
          path: 'history',
          pathName: 'History'
        }
      ]
    }
  },
  created () {
    if (this.$route.name === 'History') {
      this.menuActive = 1;
    }
  },
  methods: {
    changeMenu (v, k) {
      this.menuActive = k;
      this.$router.push({ name: v.pathName });
    }
  }
}
</script>>
<style scoped lang="scss">
.bd-execution {
  max-width: 900px;
  margin: 0 auto;
  margin-top: 20px;
  .menu-box {
    width: 190px;
    float: left;
    li {
      height: 55px;
      line-height: 55px;
      padding-left: 15px;
      cursor: pointer;
      font-weight: 500;
      & + li {
        height: 35px;
        line-height: 35px;
      }
    }
    .active {
      color: #053C8C;
    }
  }
}
.bd-execution::after {
  height: 0;
  width: 0;
  content: '';
  display: block;
  clear: both;
}
</style>