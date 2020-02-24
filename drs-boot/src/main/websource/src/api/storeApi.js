import requestObj from './';

// download deapp
export const downloadApp = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'GET', url: '/dapp/download' }, { data: config }));
  return data;
}

// Initialization deapp
export const initDeapp = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'GET', url: '/dapp/init/' + config.name }, { data: config }));
  return data;
}

// install deapp
export const installDeapp = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'GET', url: '/dapp/install/' + config.name }, { data: config }));
  return data;
}

// start deapp
export const startDeapp = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'GET', url: '/dapp/start/' + config.name }, { data: config }));
  return data;
}

// stop deapp
export const stopDeapp = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'GET', url: '/dapp/stop/' + config.name }, { data: config }));
  return data;
}

// update deapp
export const upgradeDeapp = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'GET', url: '/dapp/upgrade/' + config.name }, { data: config }));
  return data;
}

// query storeList
export const getAppList = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'GET', url: '/dapp/queryAppStore' }, { data: config }));
  return data;
}

// myStore List
export const getMyAppList = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'GET', url: '/dapp/installList' }, { data: config }));
  return data;
}

// query deapp config
export const getDeappConfig = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'GET', url: '/dapp/config/query/' + config.name }, { data: config}));
  return data;
}

// submit deapp config
export const postDeappConfig = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'POST', url: '/dapp/config/' + config.name }, { data: config }));
  return data;
}

// uninstall deApp
export const uninstallApp = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'GET', url: '/dapp/uninstall/' + config.name }, { data: config }));
  return data;
}

// BD config
export const BDOptionInfo = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'GET', url: '/drs/bd/query/' + config.bdCode }, { data: config }));
  return data;
}

// PolicyList
export const getPolicyList = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'GET', url: '/drs/queryAllPolicy' }, { data: config }));
  return data;
}

// DomainList
export const getDomainList = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'GET', url: '/drs/queryAllDomain' }, { data: config }));
  return data;
}

// PermissionList
export const getPermissionList = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'GET', url: '/drs/queryPermissionList' }, { data: config }));
  return data;
}

// drs config
export const getSysConfig = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'GET', url: '/dapp/querySysConfig' }, { data: config }));
  return data;
}

// modify drs config
export const modifySysConfig = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'POST', url: '/dapp/sysConfig' }, { data: config }));
  return data;
}

// submit BD config
export const submitBDConfig = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'POST', url: '/bd/submit' }, { data: config }));
  return data;
}

// query contractList
export const getContractList = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'GET', url: '/drs/queryContract' }, { data: config }));
  return data;
}

// getContractParam
export const getContractParam = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'POST', url: '/drs/queryMethodParam' }, { data: config }));
  return data;
}

// getBDHistory
export const getBDConfigHistory = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'POST', url: '/drs/queryTxs' }, { data: config }));
  return data;
}

// getSign
export const getSignValue = async config => {
  const { data } = await requestObj.request(Object.assign({ method: 'POST', url: '/bd/getSignValue' }, { data: config }));
  return data;
}
