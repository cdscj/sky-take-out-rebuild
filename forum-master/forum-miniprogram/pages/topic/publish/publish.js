// 发布主题页JS文件
import api from '../../../utils/api.js'

Page({
  data: {
    title: '', // 主题标题
    content: '', // 主题内容
    labelsText: '', // 输入的标签文本
    publishing: false // 是否正在发布
  },

  onLoad: function () {
    // 不需要加载标签列表，改为手动输入
  },

  // 监听标题输入
  onTitleInput: function (e) {
    this.setData({
      title: e.detail.value
    })
  },

  // 监听内容输入
  onContentInput: function (e) {
    this.setData({
      content: e.detail.value
    })
  },

  // 监听标签输入
  onLabelsInput: function (e) {
    this.setData({
      labelsText: e.detail.value
    })
  },

  // 验证表单
  validateForm: function () {
    const { title, content, labelsText } = this.data
    
    if (!title.trim()) {
      wx.showToast({
        title: '请输入主题标题',
        icon: 'none'
      })
      return false
    }
    
    if (!content.trim()) {
      wx.showToast({
        title: '请输入主题内容',
        icon: 'none'
      })
      return false
    }
    
    // 验证标签数量不超过3个
    const labels = labelsText.split(',').map(l => l.trim()).filter(l => l)
    if (labels.length === 0) {
      wx.showToast({
        title: '请至少输入一个标签',
        icon: 'none'
      })
      return false
    }
    
    if (labels.length > 3) {
      wx.showToast({
        title: '最多只能输入3个标签',
        icon: 'none'
      })
      return false
    }
    
    return true
  },

  // 发布主题
  onPublish: function () {
    if (!this.validateForm()) return
    
    this.setData({ publishing: true })
    
    // 处理标签文本，转换为标签ID数组（这里简化处理，实际应该调用后端API获取标签ID）
    const labels = this.data.labelsText.split(',').map(l => l.trim()).filter(l => l)
    // 由于后端可能需要标签ID，这里暂时使用1,2,3作为默认值
    const labelIds = labels.map((_, index) => index + 1)
    
    const data = {
      title: this.data.title.trim(),
      topicContent: this.data.content.trim(),
      labelIds: labelIds
    }
    
    api.publishTopic(data).then(res => {
      if (res.code === 200) {
        wx.showToast({
          title: '发布成功',
          icon: 'success'
        })
        
        // 跳转到主题详情页
        setTimeout(() => {
          wx.navigateTo({
            url: `/pages/topic/detail/detail?id=${res.data}`
          })
        }, 1500)
      } else {
        wx.showToast({
          title: res.msg || '发布失败',
          icon: 'none'
        })
      }
    }).catch(err => {
      console.error('发布主题失败', err)
      wx.showToast({
        title: '发布失败',
        icon: 'none'
      })
    }).finally(() => {
      this.setData({ publishing: false })
    })
  }
})
