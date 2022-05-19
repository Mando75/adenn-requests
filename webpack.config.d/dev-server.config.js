(() => {
  const devServer = config.devServer || {}
  const customDevServer = {
    ...devServer,
    proxy: {
      '/api': 'http://localhost:8080'
    },
    historyApiFallback: true,
    open: false,
  }
  config.devServer = customDevServer
})();