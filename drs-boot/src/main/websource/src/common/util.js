/**
 * Chinese
 */
export const ZH_CN = 'zh-CN';

/**
 * English
 */
export const EN_GB = 'en-GB';

/**
 * localStorage language Key
 */
export const LOCAL_STORAGE_LANG = '_lang';

/**
 * request tips
 */
export const notify = {
  any: 'ANY',
  success: 'SUCCESS',
  error: 'ERROR'
};

// getLanguage default English
export const getLang = () => {
  let localStorageLang = localStorage.getItem(LOCAL_STORAGE_LANG);
  let lang = EN_GB;
  if (!localStorageLang) {
    lang = navigator.language;
  }
  return lang;
}