/**
 * 简体中文常量
 */
export const ZH_CN = 'zh-CN';

/**
 * 英文常量
 */
export const EN_GB = 'en-GB';

/**
 * 存储在localStorage的语言变量KEY
 */
export const LOCAL_STORAGE_LANG = '_lang';

/**
 * 请求响应拦截信息提示
 */
export const notify = {
  any: 'ANY',
  success: 'SUCCESS',
  error: 'ERROR'
};

export const getLang = () => {
  let localStorageLang = localStorage.getItem(LOCAL_STORAGE_LANG);
  let lang = ZH_CN;
  if (!localStorageLang) {
    lang = navigator.language;
  } else {
    lang = localStorageLang;
  }
  return lang;
}