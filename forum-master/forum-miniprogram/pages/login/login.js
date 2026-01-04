// 登录页面JS
import api from '../../utils/api.js'

Page({
  data: {
    username: '',
    password: '',
    rememberMe: false,
    passwordVisible: false,
    loading: false
  },

  onLoad: function () {
    // 从本地存储获取记住的密码
    const rememberMe = wx.getStorageSync('rememberMe')
    if (rememberMe) {
      const username = wx.getStorageSync('username')
      const password = wx.getStorageSync('password')
      this.setData({
        username: username,
        password: password,
        rememberMe: rememberMe
      })
    }
  },

  // 输入手机号/邮箱
  onUsernameInput: function (e) {
    this.setData({
      username: e.detail.value
    })
  },

  // 输入密码
  onPasswordInput: function (e) {
    this.setData({
      password: e.detail.value
    })
  },

  // 切换密码可见性
  togglePasswordVisibility: function () {
    this.setData({
      passwordVisible: !this.data.passwordVisible
    })
  },

  // 记住密码
  onRememberMeChange: function () {
    this.setData({
      rememberMe: !this.data.rememberMe
    })
  },

  // 表单验证
  validateForm: function () {
    if (!this.data.username) {
      wx.showToast({
        title: '请输入手机号/邮箱',
        icon: 'none'
      })
      return false
    }

    if (!this.data.password) {
      wx.showToast({
        title: '请输入密码',
        icon: 'none'
      })
      return false
    }

    return true
  },

  // 登录
  onLogin: function () {
    if (!this.validateForm()) return

    this.setData({ loading: true })

    const params = {
      username: this.data.username,
      password: this.data.password
    }

    api.login(params).then(res => {
      this.setData({ loading: false })
      
      if (res.code === 200) {
        // 登录成功，保存token和用户信息
        const token = res.token
        const app = getApp()
        
        app.globalData.token = token
        wx.setStorageSync('token', token)

        // 记住密码
        if (this.data.rememberMe) {
          wx.setStorageSync('username', this.data.username)
          wx.setStorageSync('password', this.data.password)
          wx.setStorageSync('rememberMe', true)
        } else {
          wx.removeStorageSync('username')
          wx.removeStorageSync('password')
          wx.removeStorageSync('rememberMe')
        }

        // 跳转到首页
        wx.switchTab({
          url: '/pages/index/index'
        })
      } else {
        wx.showToast({
          title: res.msg || '登录失败',
          icon: 'none'
        })
      }
    }).catch(err => {
      this.setData({ loading: false })
      console.error('登录失败', err)
      wx.showToast({
        title: '网络错误，请稍后重试',
        icon: 'none'
      })
    })
  },

  // 跳转到注册页面
  onRegisterTap: function () {
    wx.navigateTo({
      url: '/pages/register/register'
    })
  },

  // 跳转到忘记密码页面
  onResetPwdTap: function () {
    wx.navigateTo({
      url: '/pages/register/register?type=reset'
    })
  }
})