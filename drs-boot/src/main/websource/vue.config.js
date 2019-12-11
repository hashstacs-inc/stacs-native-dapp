module.exports = {
  lintOnSave: true,
  devServer: {
    open: true,
    host: '0.0.0.0', // 允许外部ip访问
    port: '8081',
    // overlay: {
    //   warnings: true,
    //   errors: true
    // },
    proxy: {
      '/api': {
        // target: 'http://localhost:8080',
        // target: 'http://10.200.174.52:32359',
        target: 'http://10.200.174.52:30055',
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
}