<template>
  <div class="deapp-store">
    <ul class="menu-box">
      <li :class="{active: storeMenu === k}" @click="changeMenu(v, k)" v-for="(v, k) in menuList" :key="k">{{v.name}}</li>
    </ul>
    <div class="store-content">
      <router-view />
    </div>
  </div>
</template>
<script>
import { mapGetters } from 'vuex';

export default {
  name: 'DeappStore',
  data () {
    return {
      menuList: [
        {
          name: 'My DAPP',
          path: 'my',
          pathName: 'My'
        }, {
          name: 'DAPP Store',
          path: 'library',
          pathName: 'Library'
        }
      ]
    }
  },
  computed: {
    ...mapGetters(['storeMenu'])
  },
  methods: {
    changeMenu (v, k) {
      this.$store.commit('changeStoreMenu', k);
      this.$router.push({ name: v.pathName });
    }
  }
}
</script>>
<style scoped lang="scss">
.deapp-store {
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
  .store-content {
    float: left;
    width: 710px;
    background-color: #fff;
    padding: 40px 35px 20px 35px;
  }
}
.deapp-store::after {
  height: 0;
  width: 0;
  content: '';
  display: block;
  clear: both;
}
</style>