// 主题详情页JS文件
import api from '../../../utils/api.js'

Page({
  data: {
    topicId: '', // 主题ID
    topicDetail: {}, // 主题详情
    replies: [], // 回复列表
    replyContent: '', // 回复内容
    loading: false, // 是否正在加载
    replyLoading: false // 回复提交中
  },

  onLoad: function (options) {
    // 获取主题ID
    const topicId = options.id
    this.setData({
      topicId: topicId
    })
    
    // 加载主题详情
    this.loadTopicDetail()
    
    // 加载回复列表
    this.loadReplies()
  },

  // 加载主题详情
  loadTopicDetail: function () {
    this.setData({ loading: true })
    
    api.getTopicDetail(this.data.topicId).then(res => {
      if (res.code === 200) {
        this.setData({
          topicDetail: res.data
        })
      } else {
        wx.showToast({
          title: '加载失败',
          icon: 'none'
        })
      }
    }).catch(err => {
      console.error('加载主题详情失败', err)
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      })
    }).finally(() => {
      this.setData({ loading: false })
    })
  },

  // 加载回复列表
  loadReplies: function () {
    api.getTopicReplies(this.data.topicId).then(res => {
      if (res.code === 200) {
        this.setData({
          replies: res.data
        })
      } else {
        wx.showToast({
          title: '加载回复失败',
          icon: 'none'
        })
      }
    }).catch(err => {
      console.error('加载回复列表失败', err)
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      })
    })
  },

  // 关注/取消关注主题
  onFollowToggle: function () {
    const action = this.data.topicDetail.follow === 1 ? 'unfollow' : 'follow'
    const method = action === 'follow' ? 'followTopic' : 'unfollowTopic'
    
    api[method]({ topicId: this.data.topicId }).then(res => {
      if (res.code === 200) {
        wx.showToast({
          title: action === 'follow' ? '关注成功' : '取消关注成功',
          icon: 'success'
        })
        
        // 更新关注状态
        this.setData({
          'topicDetail.follow': action === 'follow' ? 1 : 0
        })
      } else {
        wx.showToast({
          title: res.msg || (action === 'follow' ? '关注失败' : '取消关注失败'),
          icon: 'none'
        })
      }
    }).catch(err => {
      console.error(`${action}主题失败`, err)
      wx.showToast({
        title: action === 'follow' ? '关注失败' : '取消关注失败',
        icon: 'none'
      })
    })
  },

  // 点赞/取消点赞回复
  onPraiseToggle: function (e) {
    const replyId = e.currentTarget.dataset.id
    const index = e.currentTarget.dataset.index
    const isPraised = e.currentTarget.dataset.praise
    
    const method = isPraised ? 'unPraiseReply' : 'praiseReply'
    
    api[method]({ replyId: replyId }).then(res => {
      if (res.code === 200) {
        wx.showToast({
          title: isPraised ? '取消点赞成功' : '点赞成功',
          icon: 'success'
        })
        
        // 更新点赞状态和数量
        const replies = [...this.data.replies]
        replies[index].praise = isPraised ? 0 : 1
        replies[index].praiseNum = isPraised ? replies[index].praiseNum - 1 : replies[index].praiseNum + 1
        
        this.setData({
          replies: replies
        })
      } else {
        wx.showToast({
          title: res.msg || (isPraised ? '取消点赞失败' : '点赞失败'),
          icon: 'none'
        })
      }
    }).catch(err => {
      console.error(`${isPraised ? '取消点赞' : '点赞'}回复失败`, err)
      wx.showToast({
        title: isPraised ? '取消点赞失败' : '点赞失败',
        icon: 'none'
      })
    })
  },

  // 监听回复内容输入
  onReplyInput: function (e) {
    this.setData({
      replyContent: e.detail.value
    })
  },

  // 提交回复
  onSubmitReply: function () {
    const content = this.data.replyContent.trim()
    
    if (!content) {
      wx.showToast({
        title: '回复内容不能为空',
        icon: 'none'
      })
      return
    }
    
    this.setData({ replyLoading: true })
    
    const data = {
      topicId: this.data.topicId,
      replyContent: content
    }
    
    api.replyTopic(data).then(res => {
      if (res.code === 200) {
        wx.showToast({
          title: '回复成功',
          icon: 'success'
        })
        
        // 清空回复内容
        this.setData({
          replyContent: ''
        })
        
        // 重新加载回复列表
        this.loadReplies()
      } else {
        wx.showToast({
          title: res.msg || '回复失败',
          icon: 'none'
        })
      }
    }).catch(err => {
      console.error('提交回复失败', err)
      wx.showToast({
        title: '回复失败',
        icon: 'none'
      })
    }).finally(() => {
      this.setData({ replyLoading: false })
    })
  },

  // 查看用户信息
  onUserTap: function (e) {
    wx.showToast({
      title: '该功能暂未实现',
      icon: 'none'
    })
  },
  
  // 分享功能
  onShare: function () {
    wx.showToast({
      title: '该功能暂未实现',
      icon: 'none'
    })
  },
  
  // 展开更多回复
  onExpandReplies: function (e) {
    wx.showToast({
      title: '该功能暂未实现',
      icon: 'none'
    })
  },
  
  // 回复特定用户
  onReplyTo: function (e) {
    wx.showToast({
      title: '该功能暂未实现',
      icon: 'none'
    })
  }
})
