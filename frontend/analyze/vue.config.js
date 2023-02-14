module.exports = {
    devServer: {
        port: 8080,
        proxy: {
            '/api/v2': {
                target: 'https://api.515code.com/visual-system',
                secure: false,
                changeOrigin: true,
                pathRewrite: {
                    '^/api/v2': '/'
                }
            }
        }
    }
}