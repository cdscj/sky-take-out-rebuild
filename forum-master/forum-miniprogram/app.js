App({
  onLaunch: function () {
    // 从本地存储获取token
    const token = wx.getStorageSync('token')
    if (token) {
      this.globalData.token = token
    }
    
    // 设置API基础URL
    this.globalData.baseUrl = 'http://localhost:8082/dev-api'
  },
  
  globalData: {
    userInfo: null,
    token: null,
    baseUrl: ''
  }
})