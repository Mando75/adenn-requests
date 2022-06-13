(() => {
  const devServer = config.devServer || {}
  config.devServer = {
    ...devServer,
    proxy: {
      '/api': {
        target: {
          host: "0.0.0.0",
          protocol: "http:",
          port: 8080
        },
        pathRewrite: {
          '^/api': '/api'
        }
      }
    },
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "GET, POST, PUT, DELETE, PATCH, OPTIONS",
      "Access-Control-Allow-Headers": "X-Requested-With, content-type, Authorization"
    },
    historyApiFallback: true,
    open: false,
  }
  config.devtool = undefined
})();