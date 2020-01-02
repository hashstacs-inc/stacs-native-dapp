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

// Request interception
requestObj.interceptors.request.use(config => {
  let lang = getLang();
  config.data = Object.assign({ lang }, config.data);
  config = Object.assign(config, config.data);
  return config;
}, error => {
  return Promise.reject(error);
});

// intercept notify: ANY(All hints) SUCCESS(Successful hints) ERROR(Failure prompt)
requestObj.interceptors.response.use(response => {
  if (response.data.code === '000000' && (response.config.notify === notify.any || response.config.notify === notify.success)) {
    Vue.prototype.$notify.success({message: 'Operation Success'});
  } else if ((response.data.code !== '000000') && (response.config.notify === notify.any || response.config.notify === notify.error)) {
    Vue.prototype.$notify.error({message: response.data.msg});
  } else if ((response.data.code !== '000000') && !response.config.notify) {
    Vue.prototype.$notify.error({message: response.data.msg});
  }
  return response;
}, error => {
  Vue.prototype.$notify.error({ message: error });
});

// intercept slient = true
requestObj.interceptors.response.use(response => {
  if (response.data.code !== '000000' && !response.config.slient) {
    return Promise.reject(response);
  } else {
    return response;
  }
});

export default requestObj;