// 首页JS文件
import api from '../../utils/api.js'

Page({
  data: {
    topics: [], // 主题列表
    labels: [], // 标签列表
    currentLabelId: null, // 当前选中的标签ID
    currentSort: 1, // 当前排序方式
    sortOptions: [
      { id: 1, name: '最新回复', value: 1 },
      { id: 2, name: '热门主题', value: 2 },
      { id: 3, name: '新鲜出炉', value: 3 },
      { id: 4, name: '精华主题', value: 4 }
    ],
    page: 1, // 当前页码
    pageSize: 10, // 每页数量
    hasMore: true, // 是否有更多数据
    searchKeyword: '', // 搜索关键词
    labelKeyword: '', // 标签关键词
    loading: false, // 是否正在加载
    empty: false // 是否为空数据
  },

  onLoad: function () {
    // 初始化数据
    this.loadLabels()
    this.loadTopics()
  },

  // 加载标签列表
  loadLabels: function () {
    api.getLabelList({}).then(res => {
      if (res.code === 200) {
        // 处理不同的数据结构，确保labels是数组
        const labels = Array.isArray(res.data) ? res.data : (res.data.list || [])
        this.setData({
          labels: labels
        })
      }
    }).catch(err => {
      console.error('加载标签失败', err)
    })
  },

  // 加载主题列表
  loadTopics: function () {
    if (this.data.loading || !this.data.hasMore) return

    this.setData({ loading: true })

    // 合并搜索关键词和标签关键词
    let searchName = this.data.searchKeyword
    if (this.data.labelKeyword) {
      searchName = searchName ? `${searchName} ${this.data.labelKeyword}` : this.data.labelKeyword
    }

    const params = {
      orderIn: this.data.currentSort,
      name: searchName,
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
      console.error('加载主题失败', err)
      this.setData({ loading: false, empty: this.data.page === 1 })
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      })
    })
  },

  // 标签输入处理
  onLabelInput: function (e) {
    this.setData({
      labelKeyword: e.detail.value,
      page: 1,
      topics: [],
      hasMore: true
    })
    this.loadTopics()
  },

  // 切换排序
  onSortChange: function (e) {
    const sortId = e.currentTarget.dataset.id
    this.setData({
      currentSort: sortId,
      page: 1,
      topics: [],
      hasMore: true
    })
    this.loadTopics()
  },

  // 搜索输入
  onSearchInput: function (e) {
    this.setData({
      searchKeyword: e.detail.value
    })
  },

  // 执行搜索
  onSearch: function () {
    this.setData({
      page: 1,
      topics: [],
      hasMore: true
    })
    this.loadTopics()
  },

  // 点击主题
  onTopicTap: function (e) {
    const topicId = e.currentTarget.dataset.id
    wx.navigateTo({
      url: '/pages/topic/detail/detail?id=' + topicId
    })
  },

  // 加载更多
  loadMore: function () {
    this.loadTopics()
  },

  // 页面上拉触底事件的处理函数
  onReachBottom: function () {
    this.loadMore()
  },

  // 页面下拉刷新
  onPullDownRefresh: function () {
    this.setData({
      page: 1,
      topics: [],
      hasMore: true
    })
    this.loadTopics()
    this.loadLabels()
    wx.stopPullDownRefresh()
  }
})