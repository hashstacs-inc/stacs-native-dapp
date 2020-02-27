const TimeStamp = new Date().getTime();
const MiniCssExtractPlugin = require('mini-css-extract-plugin');

module.exports = {
  lintOnSave: true,
  devServer: {
    open: true,
    host: '0.0.0.0',
    port: '8081',
    // overlay: {
    //   warnings: true,
    //   errors: true
    // },
    proxy: {
      '/api': {
        // target: 'http://10.200.174.52:30010',
        target: 'http://10.200.174.117:6003',
        // target: 'http://10.200.174.52:30055',
        // target: 'http://10.200.174.101:8080',
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  },
  configureWebpack: {
    // Modify JS file name
    output: {
      filename: `js/[name].${TimeStamp}.js`,
      chunkFilename: `js/[name].${TimeStamp}.js`
    },
    plugins: [
      // Modify CSS file name
      new MiniCssExtractPlugin({
        filename: `css/[name].${TimeStamp}.css`
      })
    ]
  },
  chainWebpack: config => {
    config.module.rule('images')
    .use('url-loader')
    .tap(() => {
      return {
        limit: 4096,
        fallback: {
          loader: 'file-loader',
          options: {
            name: `img/[name].${TimeStamp}.[ext]`
          }
        }
      };
    });
  }
}