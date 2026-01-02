// pages/map-detail/map-detail.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    currentLocation: null, // 当前位置
    orderId: null, // 订单ID
    orderAddress: null, // 订单地址
    startAddress: '我的位置', // 起点地址
    endAddress: '', // 终点地址
    distance: '', // 距离
    duration: '', // 预计时间
    markers: [], // 地图标记
    polyline: [] // 路线
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    // 获取URL参数
    this.setData({
      orderId: options.orderId,
      orderAddress: decodeURIComponent(options.orderAddress),
      endAddress: decodeURIComponent(options.orderAddress)
    });
    // 获取当前位置
    this.getCurrentLocation();
  },

  /**
   * 获取当前位置
   */
  getCurrentLocation() {
    wx.getLocation({
      type: 'gcj02',
      success: (res) => {
        this.setData({
          currentLocation: {
            latitude: res.latitude,
            longitude: res.longitude
          }
        });
        // 计算路线
        this.calculateRoute();
      },
      fail: (err) => {
        wx.showToast({
          title: '获取位置失败',
          icon: 'none'
        });
      }
    });
  },

  /**
   * 计算路线
   */
  calculateRoute() {
    if (!this.data.currentLocation || !this.data.orderAddress) return;

    wx.request({
      url: 'http://localhost:8080/map/calculate-route',
      method: 'POST',
      data: {
        originLatitude: this.data.currentLocation.latitude,
        originLongitude: this.data.currentLocation.longitude,
        destinationAddress: this.data.orderAddress
      },
      success: (res) => {
        if (res.data.code === 1) {
          const routeData = res.data.data;
          this.updateMap(routeData);
        } else {
          wx.showToast({
            title: '路线计算失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        wx.showToast({
          title: '网络请求失败',
          icon: 'none'
        });
      }
    });
  },

  /**
   * 更新地图显示
   */
  updateMap(routeData) {
    // 设置路线信息
    this.setData({
      distance: routeData.distance,
      duration: routeData.duration
    });

    // 创建起点和终点标记
    const markers = [
      {
        id: 1,
        latitude: this.data.currentLocation.latitude,
        longitude: this.data.currentLocation.longitude,
        iconPath: '/static/start.png',
        width: 30,
        height: 30,
        title: '我的位置'
      },
      {
        id: 2,
        latitude: routeData.destinationLatitude,
        longitude: routeData.destinationLongitude,
        iconPath: '/static/end.png',
        width: 30,
        height: 30,
        title: this.data.orderAddress
      }
    ];

    // 创建路线
    const polyline = [
      {
        points: routeData.points,
        color: '#00BFFF',
        width: 5,
        dottedLine: false
      }
    ];

    this.setData({
      markers: markers,
      polyline: polyline
    });
  },

  /**
   * 返回上一页
   */
  goBack() {
    wx.navigateBack();
  }
})