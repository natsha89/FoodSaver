const { defineConfig } = require('@vue/cli-service');
const webpack = require('webpack');  // Import webpack

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 3000, // Set the port to 3000
  },
  configureWebpack: {
    plugins: [
      new webpack.DefinePlugin({
        __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: JSON.stringify(true)  // Define the feature flag
      })
    ]
  }
});
