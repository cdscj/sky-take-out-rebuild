// 个人中心页JS文件
import api from '../../utils/api.js'

Page({
  data: {
    userInfo: {}, // 用户信息
    stats: {}, // 统计信息
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
    const token = wx.getStorageSync('token')
    if (!token) {
      // 未登录状态，不加载用户信息
      this.setData({
        userInfo: {},
        stats: {}
      })
      return
    }
    
    this.setData({ loading: true })
    
    api.getUserInfo().then(res => {
      if (res.code === 200) {
        this.setData({
          userInfo: res.data
        })
        // 加载统计信息
        this.loadStats()
      } else {
        wx.showToast({
          title: '加载失败',
          icon: 'none'
        })
      }
    }).catch(err => {
      console.error('加载用户信息失败', err)
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      })
    }).finally(() => {
      this.setData({ loading: false })
    })
  },

  // 加载统计信息
  loadStats: function () {
    // 这里可以根据后端接口获取用户的统计信息
    // 目前使用模拟数据
    this.setData({
      stats: {
        topicCount: 5,
        replyCount: 23,
        favoriteCount: 12
      }
    })
  },

  // 编辑资料
  onEditProfile: function () {
    if (!this.data.userInfo.id) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      })
      return
    }
    wx.showToast({
      title: '该功能暂未实现',
      icon: 'none'
    })
  },

  // 我的主题
  onMyTopics: function () {
    if (!this.data.userInfo.id) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      })
      return
    }
    wx.navigateTo({
      url: '/pages/personal/topics/topics'
    })
  },

  // 我的回复
  onMyReplies: function () {
    if (!this.data.userInfo.id) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      })
      return
    }
    wx.navigateTo({
      url: '/pages/personal/replies/replies'
    })
  },

  // 我的收藏
  onMyFavorites: function () {
    if (!this.data.userInfo.id) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      })
      return
    }
    wx.navigateTo({
      url: '/pages/personal/favorites/favorites'
    })
  },

  // 我关注的
  onMyFollowed: function () {
    if (!this.data.userInfo.id) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      })
      return
    }
    wx.navigateTo({
      url: '/pages/personal/followed/followed'
    })
  },

  // 设置
  onSettings: function () {
    wx.navigateTo({
      url: '/pages/personal/settings/settings'
    })
  },

  // 关于
  onAbout: function () {
    wx.navigateTo({
      url: '/pages/about/about'
    })
  },

  // 去登录
  onLogin: function () {
    wx.navigateTo({
      url: '/pages/login/login'
    })
  },

  // 退出登录
  onLogout: function () {
    wx.showModal({
      title: '确认退出',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          // 清除token
          wx.removeStorageSync('token')
          // 清除全局数据
          const app = getApp()
          app.globalData.token = null
          // 刷新页面
          this.loadUserInfo()
          wx.showToast({
            title: '退出成功',
            icon: 'success'
          })
        }
      }
    })
  }
})
