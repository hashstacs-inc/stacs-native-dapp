import Vue from 'vue';
import VueI18n from 'vue-i18n';
import messages from './mergeLang';
import { getLang, EN_GB } from '@/common/util';
Vue.use(VueI18n);
//  默认显示英文
export default new VueI18n({
  locale: getLang(), messages, fallbackLocale: EN_GB
});