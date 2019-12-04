import home from './home';

const merge = (...modules) => {
  let result = {}
  if (Array.isArray(modules)) {
    modules.forEach(md => {
      for (let lang in md) {
        result[lang] = Object.assign({}, result[lang], md[lang]);
      }
    });
  }
  return result;
}
export default merge(home);