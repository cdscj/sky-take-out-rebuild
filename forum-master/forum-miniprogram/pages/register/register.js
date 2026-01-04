// 注册/忘记密码页面JS
import api from '../../utils/api.js'

Page({
  data: {
    isReset: false, // 是否是重置密码
    username: '',
    code: '',
    password: '',
    confirmPassword: '',
    sendingCode: false, // 是否正在发送验证码
    countDown: 60, // 倒计时时间
    timer: null // 计时器
  },

  onLoad: function (options) {
    // 判断是否是重置密码页面
    this.setData({
      isReset: options.type === 'reset'
    })
  },

  onUnload: function () {
    // 清理计时器
    if (this.data.timer) {
      clearInterval(this.data.timer)
    }
  },

  // 输入手机号/邮箱
  onUsernameInput: function (e) {
    this.setData({
      username: e.detail.value
    })
  },

  // 输入验证码
  onCodeInput: function (e) {
    this.setData({
      code: e.detail.value
    })
  },

  // 输入密码
  onPasswordInput: function (e) {
    this.setData({
      password: e.detail.value
    })
  },

  // 确认密码
  onConfirmPasswordInput: function (e) {
    this.setData({
      confirmPassword: e.detail.value
    })
  },

  // 验证手机号/邮箱格式
  validateUsername: function (username) {
    const phoneReg = /^1[3-9]\d{9}$/
    const emailReg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
    return phoneReg.test(username) || emailReg.test(username)
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

    if (!this.validateUsername(this.data.username)) {
      wx.showToast({
        title: '手机号/邮箱格式不正确',
        icon: 'none'
      })
      return false
    }

    if (!this.data.code) {
      wx.showToast({
        title: '请输入验证码',
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

    if (!this.data.isReset && this.data.password !== this.data.confirmPassword) {
      wx.showToast({
        title: '两次输入的密码不一致',
        icon: 'none'
      })
      return false
    }

    return true
  },

  // 发送验证码倒计时
  startCountDown: function () {
    let countDown = 60
    this.setData({
      sendingCode: true,
      countDown: countDown
    })

    const timer = setInterval(() => {
      countDown--
      this.setData({
        countDown: countDown
      })

      if (countDown <= 0) {
        clearInterval(timer)
        this.setData({
          sendingCode: false
        })
      }
    }, 1000)

    this.setData({ timer: timer })
  },

  // 获取验证码
  onGetCode: function () {
    if (this.data.sendingCode) return

    if (!this.data.username) {
      wx.showToast({
        title: '请输入手机号/邮箱',
        icon: 'none'
      })
      return
    }

    if (!this.validateUsername(this.data.username)) {
      wx.showToast({
        title: '手机号/邮箱格式不正确',
        icon: 'none'
      })
      return
    }

    this.startCountDown()

    api.getRegisterCode(this.data.username).then(res => {
      if (res.code !== 200) {
        wx.showToast({
          title: res.msg || '发送失败',
          icon: 'none'
        })
        // 停止倒计时
        clearInterval(this.data.timer)
        this.setData({
          sendingCode: false
        })
      } else {
        wx.showToast({
          title: '验证码发送成功',
          icon: 'success'
        })
      }
    }).catch(err => {
      console.error('发送验证码失败', err)
      wx.showToast({
        title: '发送失败，请稍后重试',
        icon: 'none'
      })
      // 停止倒计时
      clearInterval(this.data.timer)
      this.setData({
        sendingCode: false
      })
    })
  },

  // 提交注册/重置密码
  onSubmit: function () {
    if (!this.validateForm()) return

    const params = {
      username: this.data.username,
      password: this.data.password,
      code: this.data.code
    }

    wx.showLoading({ title: '处理中...' })

    if (this.data.isReset) {
      // 重置密码
      api.resetPwd(params).then(res => {
        wx.hideLoading()
        if (res.code === 200) {
          wx.showToast({
            title: '密码重置成功',
            icon: 'success'
          })
          // 跳转到登录页
          setTimeout(() => {
            wx.navigateTo({
              url: '/pages/login/login'
            })
          }, 1500)
        } else {
          wx.showToast({
            title: res.msg || '重置失败',
            icon: 'none'
          })
        }
      }).catch(err => {
        wx.hideLoading()
        console.error('重置密码失败', err)
        wx.showToast({
          title: '网络错误，请稍后重试',
          icon: 'none'
        })
      })
    } else {
      // 注册
      api.register(params).then(res => {
        wx.hideLoading()
        if (res.code === 200) {
          wx.showToast({
            title: '注册成功',
            icon: 'success'
          })
          // 跳转到登录页
          setTimeout(() => {
            wx.navigateTo({
              url: '/pages/login/login'
            })
          }, 1500)
        } else {
          wx.showToast({
            title: res.msg || '注册失败',
            icon: 'none'
          })
        }
      }).catch(err => {
        wx.hideLoading()
        console.error('注册失败', err)
        wx.showToast({
          title: '网络错误，请稍后重试',
          icon: 'none'
        })
      })
    }
  },

  // 跳转到登录页
  onLoginTap: function () {
    wx.navigateTo({
      url: '/pages/login/login'
    })
  }
})