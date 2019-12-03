module.exports = {
  lintOnSave: true,
  devServer: {
    open: true,
    host: '0.0.0.0', // 允许外部ip访问
    overlay: {
      warnings: true,
      errors: true
    },
    proxy: {
      '/api': {
        target: 'http://www.baidu.com/api',
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
}