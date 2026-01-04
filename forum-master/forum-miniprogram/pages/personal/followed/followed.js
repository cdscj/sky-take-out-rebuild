// 我关注的页JS文件
import api from '../../../utils/api.js'

Page({
  data: {
    followedTopics: [], // 关注的主题列表
    page: 1, // 当前页码
    pageSize: 10, // 每页数量
    hasMore: true, // 是否有更多数据
    loading: false, // 是否正在加载
    empty: false // 是否为空数据
  },

  onLoad: function () {
    // 页面加载时获取我关注的主题
    this.loadFollowedTopics()
  },

  // 加载我关注的主题列表
  loadFollowedTopics: function () {
    if (this.data.loading || !this.data.hasMore) return

    this.setData({ loading: true })

    // 目前使用模拟数据，后续替换为真实API调用
    setTimeout(() => {
      const newTopics = [] // 模拟空数据
      const followedTopics = this.data.page === 1 ? newTopics : [...this.data.followedTopics, ...newTopics]
      
      this.setData({
        followedTopics: followedTopics,
        hasMore: newTopics.length === this.data.pageSize,
        page: this.data.page + 1,
        loading: false,
        empty: this.data.page === 1 && newTopics.length === 0
      })
    }, 1000)
  },

  // 点击主题，跳转到主题详情
  onTopicTap: function (e) {
    const topicId = e.currentTarget.dataset.id
    wx.navigateTo({
      url: '/pages/topic/detail/detail?id=' + topicId
    })
  },

  // 取消关注
  onCancelFollow: function (e) {
    const index = e.currentTarget.dataset.index
    const followedTopics = [...this.data.followedTopics]
    followedTopics.splice(index, 1)
    this.setData({
      followedTopics: followedTopics,
      empty: followedTopics.length === 0
    })
    wx.showToast({
      title: '取消关注成功',
      icon: 'success'
    })
  },

  // 页面上拉触底事件的处理函数
  onReachBottom: function () {
    this.loadFollowedTopics()
  },

  // 页面下拉刷新
  onPullDownRefresh: function () {
    this.setData({
      page: 1,
      followedTopics: [],
      hasMore: true
    })
    this.loadFollowedTopics()
    wx.stopPullDownRefresh()
  }
})