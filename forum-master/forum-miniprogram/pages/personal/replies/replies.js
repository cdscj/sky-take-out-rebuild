// 我的回复页JS文件
import api from '../../../utils/api.js'

Page({
  data: {
    topics: [], // 主题列表（包含我的回复）
    page: 1, // 当前页码
    pageSize: 10, // 每页数量
    hasMore: true, // 是否有更多数据
    loading: false, // 是否正在加载
    empty: false // 是否为空数据
  },

  onLoad: function () {
    // 页面加载时获取我的回复
    this.loadReplies()
  },

  // 加载我的回复列表
  loadReplies: function () {
    if (this.data.loading || !this.data.hasMore) return

    this.setData({ loading: true })

    const params = {
      orderIn: 1, // 最新回复排序
      type: 2, // 我的回复主题
      page: this.data.page,
      pageSize: this.data.pageSize
    }

    api.getTopicList(params).then(res => {
      if (res.code === 200) {
        const newTopics = res.rows || res.data || []
        const topics = this.data.page === 1 ? newTopics : [...this.data.topics, ...newTopics]
        
        this.setData({
          topics: topics,
          hasMore: newTopics.length === this.data.pageSize,
          page: this.data.page + 1,
          loading: false,
          empty: this.data.page === 1 && newTopics.length === 0
        })
      } else {
        this.setData({ loading: false, empty: this.data.page === 1 })
        wx.showToast({
          title: '加载失败',
          icon: 'none'
        })
      }
    }).catch(err => {
      console.error('加载我的回复失败', err)
      this.setData({ loading: false, empty: this.data.page === 1 })
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      })
    })
  },

  // 点击主题，跳转到主题详情
  onTopicTap: function (e) {
    const topicId = e.currentTarget.dataset.id
    wx.navigateTo({
      url: '/pages/topic/detail/detail?id=' + topicId
    })
  },

  // 页面上拉触底事件的处理函数
  onReachBottom: function () {
    this.loadReplies()
  },

  // 页面下拉刷新
  onPullDownRefresh: function () {
    this.setData({
      page: 1,
      topics: [],
      hasMore: true
    })
    this.loadReplies()
    wx.stopPullDownRefresh()
  }
})