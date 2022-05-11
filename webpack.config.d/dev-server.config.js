(() => {
  const devServer = config.devServer || {}
  const withProxy = {
    ...devServer,
    proxy: {
      '/api': 'http://localhost:8080'
    }
  }
  config.devServer = withProxy
})();