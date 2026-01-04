// 我的收藏页JS文件
import api from '../../../utils/api.js'

Page({
  data: {
    favorites: [], // 收藏列表
    page: 1, // 当前页码
    pageSize: 10, // 每页数量
    hasMore: true, // 是否有更多数据
    loading: false, // 是否正在加载
    empty: false // 是否为空数据
  },

  onLoad: function () {
    // 页面加载时获取我的收藏
    this.loadFavorites()
  },

  // 加载我的收藏列表
  loadFavorites: function () {
    if (this.data.loading || !this.data.hasMore) return

    this.setData({ loading: true })

    // 目前使用模拟数据，后续替换为真实API调用
    setTimeout(() => {
      const newFavorites = [] // 模拟空数据
      const favorites = this.data.page === 1 ? newFavorites : [...this.data.favorites, ...newFavorites]
      
      this.setData({
        favorites: favorites,
        hasMore: newFavorites.length === this.data.pageSize,
        page: this.data.page + 1,
        loading: false,
        empty: this.data.page === 1 && newFavorites.length === 0
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

  // 取消收藏
  onCancelFavorite: function (e) {
    const index = e.currentTarget.dataset.index
    const favorites = [...this.data.favorites]
    favorites.splice(index, 1)
    this.setData({
      favorites: favorites,
      empty: favorites.length === 0
    })
    wx.showToast({
      title: '取消收藏成功',
      icon: 'success'
    })
  },

  // 页面上拉触底事件的处理函数
  onReachBottom: function () {
    this.loadFavorites()
  },

  // 页面下拉刷新
  onPullDownRefresh: function () {
    this.setData({
      page: 1,
      favorites: [],
      hasMore: true
    })
    this.loadFavorites()
    wx.stopPullDownRefresh()
  }
})