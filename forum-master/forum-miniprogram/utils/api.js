// API请求工具类
const app = getApp()

// 请求拦截器
function requestInterceptor(config) {
  // 添加token到请求头
  const token = app.globalData.token || wx.getStorageSync('token')
  if (token) {
    config.header['Authorization'] = 'Bearer ' + token
  }
  return config
}

// 响应拦截器
function responseInterceptor(res) {
  if (res.data.code === 401) {
    // token过期或无效，跳转到登录页
    // 只在需要登录的页面才跳转，公共接口不跳转
    const currentPath = getCurrentPages()[getCurrentPages().length - 1].route
    if (!['pages/index/index', 'pages/topic/detail/detail'].includes(currentPath)) {
      wx.redirectTo({
        url: '/pages/login/login'
      })
    }
    return Promise.reject('登录已过期，请重新登录')
  }
  return res.data
}

// 封装请求方法
function request(url, method, data = {}) {
  return new Promise((resolve, reject) => {
    // 应用请求拦截器
    const config = requestInterceptor({
      url: app.globalData.baseUrl + url,
      method: method,
      data: data,
      header: {
        'Content-Type': 'application/json'
      }
    })

    wx.request({
      ...config,
      success: (res) => {
        // 应用响应拦截器
        try {
          const processedRes = responseInterceptor(res)
          resolve(processedRes)
        } catch (error) {
          reject(error)
        }
      },
      fail: (err) => {
        wx.showToast({
          title: '网络错误',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

// 导出API方法
export default {
  // 登录相关
  login: (data) => request('/cas/login', 'POST', data),
  
  // 注册相关
  register: (data) => request('/register/commit', 'POST', data),
  getRegisterCode: (username) => request('/register/registerSms?username=' + username, 'GET'),
  resetPwd: (data) => request('/register/resetPwd', 'POST', data),
  
  // 主题相关
  getTopicList: (params) => request('/forum/topic/list', 'GET', params),
  getTopicDetail: (topicId) => request('/forum/topic/detail?topicId=' + topicId, 'GET'),
  searchTopics: (name) => request('/forum/topic/searchList?name=' + name, 'GET'),
  publishTopic: (data) => request('/topic/publish', 'POST', data),
  amendTopic: (data) => request('/topic/amend', 'POST', data),
  cancelTopic: (id) => request('/topic/cancel/' + id, 'POST'),
  
  // 标签相关
  getLabelList: (data) => request('/forum/label/list', 'POST', data),
  
  // 回复相关
  getTopicReplies: (topicId) => request('/forum/reply/main/list?topicId=' + topicId, 'GET'),
  getReplyReplies: (replyId) => request('/forum/reply/list?replyId=' + replyId, 'GET'),
  replyTopic: (data) => request('/topic/reply', 'POST', data),
  praiseReply: (data) => request('/topic/reply/praise', 'POST', data),
  unPraiseReply: (data) => request('/topic/reply/unpraise', 'POST', data),
  reportReply: (data) => request('/topic/reply/report', 'POST', data),
  
  // 用户相关
  getUserInfo: () => request('/personal/userInfo', 'GET'),
  updateUser: (data) => request('/personal/updateUser', 'POST', data),
  changePwd: (data) => request('/personal/changePwd', 'POST', data),
  changeEmail: (data) => request('/personal/changeEmail', 'POST', data),
  sendEmail: (data) => request('/personal/sendEmail', 'POST', data),
  changePhone: (data) => request('/personal/changePhone', 'POST', data),
  sendSms: (data) => request('/personal/sendSms', 'POST', data),
  uploadAvatar: (filePath) => {
    return new Promise((resolve, reject) => {
      const token = app.globalData.token || wx.getStorageSync('token')
      wx.uploadFile({
        url: app.globalData.baseUrl + '/personal/upload/avatar',
        filePath: filePath,
        name: 'file',
        header: {
          'Authorization': 'Bearer ' + token
        },
        success: (res) => {
          const data = JSON.parse(res.data)
          resolve(data)
        },
        fail: (err) => {
          wx.showToast({
            title: '上传失败',
            icon: 'none'
          })
          reject(err)
        }
      })
    })
  },
  
  // 关注相关
  followTopic: (data) => request('/topic/follow', 'POST', data),
  unfollowTopic: (data) => request('/topic/unfollow', 'POST', data)
}