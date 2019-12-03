import axios from 'axios';
import { getLang, notify } from '@/common/util';

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
  config.data = Object.assign({ lang }, config.data.data);
  return config;
});

// 拦截notify: ANY(全部提示) SUCCESS(成功提示) ERROR(失败提示)
requestObj.interceptors.response.use(response => {
  if (response.data.code === '1000' && (response.config.notify === notify.any || response.config.notify === notify.success)) {
    window.APP.$notify.success({message: '操作成功！'});
  } else if ((response.data.code !== '2005' && response.data.code !== '1000') && (response.config.notify === notify.any || response.config.notify === notify.error)) {
    window.APP.$notify.error({message: response.data.msg});
  }
  return response;
});

// 拦截slient = true 不用写catch 否则需要自己处理catch 统一返回 response对象
requestObj.interceptors.response.use(response => {
  if (response.data.code !== '1000' && !response.config.slient) {
    return Promise.reject(response);
  } else {
    return response;
  }
});