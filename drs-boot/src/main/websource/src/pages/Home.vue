<template>
  <div class="home-page">
    <Header />
    <router-view />
    <div style="height: 60px;"></div>
    <Footer />
  </div>
</template>
<script>
import Header from './header';
import Footer from './footer';

export default {
  name: 'Home',
  components: {
    Header,
    Footer
  }
}
</script>
<style lang="scss" scoped>
.home-page {
  min-height: 100%;
  background-color: #F2F5FC;
  position: relative;
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