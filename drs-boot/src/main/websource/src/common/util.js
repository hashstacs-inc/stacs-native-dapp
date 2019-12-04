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

// 获取语言环境，默认英文
export const getLang = () => {
  let localStorageLang = localStorage.getItem(LOCAL_STORAGE_LANG);
  let lang = EN_GB;
  if (!localStorageLang) {
    lang = navigator.language;
  }
  return lang;
}