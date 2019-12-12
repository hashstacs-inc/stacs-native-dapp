import axios from 'axios';
import { getLang, notify } from '@/common/util';
import Vue from 'vue';

const requestObj = axios.create({
  baseURL: process.env.VUE_APP_BASE_URL,
  timeout: 5000,
  headers: {
    lang: getLang()
  }
});

// 请求拦截
requestObj.interceptors.request.use(config => {
  let lang = getLang();
  config.data = Object.assign({ lang }, config.data);
  config = Object.assign(config, config.data);
  return config;
}, error => {
  return Promise.reject(error);
});

// 拦截notify: ANY(全部提示) SUCCESS(成功提示) ERROR(失败提示)
requestObj.interceptors.response.use(response => {
  if (response.data.code === '000000' && (response.config.notify === notify.any || response.config.notify === notify.success)) {
    Vue.prototype.$notify.success({message: 'Operation Success'});
  } else if ((response.data.code !== '2005' && response.data.code !== '000000') && (response.config.notify === notify.any || response.config.notify === notify.error)) {
    Vue.prototype.$notify.error({message: response.data.msg});
  }
  return response;
}, error => {
  Vue.prototype.$notify.error({message: error});
});

// 拦截slient = true 不用写catch 否则需要自己处理catch 统一返回 response对象
requestObj.interceptors.response.use(response => {
  if (response.data.code !== '000000' && !response.config.slient) {
    return Promise.reject(response);
  } else {
    return response;
  }
});

export default requestObj;