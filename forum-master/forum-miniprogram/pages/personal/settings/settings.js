// 设置页JS文件
Page({
  data: {
    userInfo: {}, // 用户信息
    loading: false // 是否正在加载
  },

  onLoad: function () {
    // 页面加载时获取用户信息
    this.loadUserInfo()
  },

  onShow: function () {
    // 页面显示时重新获取用户信息，确保数据最新
    this.loadUserInfo()
  },

  // 加载用户信息
  loadUserInfo: function () {
    const userInfo = wx.getStorageSync('userInfo') || {}
    this.setData({
      userInfo: userInfo
    })
  },

  // 编辑个人资料
  onEditProfile: function () {
    wx.showToast({
      title: '该功能暂未实现',
      icon: 'none'
    })
  },

  // 修改密码
  onChangePassword: function () {
    wx.showToast({
      title: '该功能暂未实现',
      icon: 'none'
    })
  },

  // 隐私设置
  onPrivacySetting: function () {
    wx.showToast({
      title: '该功能暂未实现',
      icon: 'none'
    })
  },

  // 通知设置
  onNotificationSetting: function () {
    wx.showToast({
      title: '该功能暂未实现',
      icon: 'none'
    })
  },

  // 关于
  onAbout: function () {
    wx.navigateTo({
      url: '/pages/about/about'
    })
  },

  // 退出登录
  onLogout: function () {
    wx.showModal({
      title: '确认退出',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          // 清除token和用户信息
          wx.removeStorageSync('token')
          wx.removeStorageSync('userInfo')
          // 清除全局数据
          const app = getApp()
          app.globalData.token = null
          app.globalData.userInfo = null
          // 返回登录页
          wx.redirectTo({
            url: '/pages/login/login'
          })
          wx.showToast({
            title: '退出成功',
            icon: 'success'
          })
        }
      }
    })
  }
})