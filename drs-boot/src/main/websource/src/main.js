import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import { getLang } from '@/common/util';
import 'element-ui/lib/theme-chalk/index.css';
import Element from 'element-ui';
import { sync } from 'vuex-router-sync';
import i18n from './lang';
import '@/assets/scss/common.scss';
import mixin from '@/common/mixin';

Vue.config.productionTip = false;
// console.log(process.env)
Vue.mixin(mixin);
Vue.use(Element, { locale: getLang() });
sync(store, router);

new Vue({
  router,
  store,
  i18n,
  render: h => h(App)
}).$mount('#app');
