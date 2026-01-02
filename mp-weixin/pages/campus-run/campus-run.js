// pages/campus-run/campus-run.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    riderList: [], // 骑手列表
    orderList: [], // 待接单列表
    currentLocation: { // 当前位置，默认值为北京天安门
      latitude: 39.908823,
      longitude: 116.397470,
      accuracy: 0,
      altitude: 0,
      speed: 0,
      heading: 0
    },
    showOrderDetail: false, // 是否显示订单详情
    selectedOrder: null, // 选中的订单
    polyline: [], // 路线数据
    markers: [], // 地图标记
    showManualLocationModal: false, // 是否显示手动设置位置弹窗
    customLatitude: '', // 自定义纬度
    customLongitude: '', // 自定义经度
    accuracy: 0, // 定位精度，单位：米
    locationAccuracyText: '未知' // 定位精度文本描述
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.getOrderList();
    this.getCurrentLocation();
    this.getRiderList();
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    // 页面显示时刷新数据和位置
    this.getOrderList();
    this.getRiderList();
    this.getCurrentLocation();
  },

  /**
   * 获取待接单列表
   */
  getOrderList() {
    // 调用接口获取待接单列表
    wx.request({
      url: 'http://localhost:8080/rider/pending-orders',
      success: (res) => {
        this.setData({
          orderList: res.data
        });
        this.updateMarkers();
        // 如果有当前位置和订单，计算到第一个订单的路线
        if (this.data.currentLocation && this.data.orderList.length > 0) {
          this.calculateRouteToOrder(this.data.orderList[0]);
        }
      }
    });
  },

  /**
   * 获取当前位置
   */
  getCurrentLocation() {
    wx.showLoading({
      title: '正在获取位置...',
      mask: true
    });
    
    // 直接调用getLocationWithHighAccuracy，由它来处理权限请求
    this.getLocationWithHighAccuracy();
  },

  /**
   * 请求位置权限
   */
  requestLocationPermission() {
    // 使用wx.getLocation直接触发权限请求，这是微信小程序最新规范要求的
    this.getLocationWithHighAccuracy();
  },

  /**
   * 高精度获取当前位置
   */
  getLocationWithHighAccuracy() {
    wx.getLocation({
      type: 'gcj02', // 使用GCJ-02坐标系统
      altitude: true, // 获取高度信息
      accuracy: 'best', // 高精度定位
      timeout: 10000, // 超时时间10秒
      success: (res) => {
        wx.hideLoading();
        this.setData({
          currentLocation: {
            latitude: res.latitude,
            longitude: res.longitude,
            altitude: res.altitude,
            accuracy: res.accuracy,
            speed: res.speed,
            heading: res.heading
          }
        });
        
        this.updateLocationAccuracyText(res.accuracy);
        // 如果有订单，计算到第一个订单的路线
        if (this.data.orderList.length > 0) {
          this.calculateRouteToOrder(this.data.orderList[0]);
        }
        
        wx.showToast({
          title: '已获取精确位置',
          icon: 'success'
        });
      },
      fail: (err) => {
        console.error('高精度获取位置失败:', err);
        wx.hideLoading();
        
        // 根据错误类型显示不同的提示
        let errorMsg = '位置获取失败';
        let showSettingPrompt = false;
        
        if (err.errCode === 12002) {
          errorMsg = '定位超时，请检查网络或稍后重试';
        } else if (err.errCode === 12003) {
          errorMsg = '定位失败，请检查GPS信号';
        } else if (err.errCode === 12004) {
          errorMsg = '手机系统位置开关未开启';
          showSettingPrompt = true;
        } else if (err.errCode === 12006) {
          errorMsg = '微信位置权限未开启';
          showSettingPrompt = true;
        }
        
        // 显示错误提示
        if (showSettingPrompt) {
          wx.showModal({
            title: '位置获取失败',
            content: errorMsg + '，请检查设置',
            confirmText: '去设置',
            cancelText: '使用默认位置',
            success: (modalRes) => {
              if (modalRes.confirm) {
                // 引导用户去设置页面
                wx.openSetting({
                  success: (openSettingRes) => {
                    if (openSettingRes.authSetting['scope.userLocation']) {
                      // 用户在设置页面开启了权限，重新获取位置
                      this.getCurrentLocation();
                    } else {
                      wx.showToast({
                        title: '将使用默认位置',
                        icon: 'none'
                      });
                      this.setDefaultLocation();
                    }
                  },
                  fail: (err) => {
                    console.error('打开设置页面失败:', err);
                    wx.showToast({
                      title: '将使用默认位置',
                      icon: 'none'
                    });
                    this.setDefaultLocation();
                  }
                });
              } else {
                // 用户选择使用默认位置
                this.setDefaultLocation();
              }
            }
          });
        } else {
          wx.showModal({
            title: '位置获取失败',
            content: errorMsg + '，是否使用默认位置？',
            confirmText: '使用默认位置',
            cancelText: '重试',
            success: (modalRes) => {
              if (modalRes.confirm) {
                // 使用默认位置
                this.setDefaultLocation();
              } else {
                // 重试获取位置
                this.getCurrentLocation();
              }
            }
          });
        }
      }
    });
  },

  /**
   * 通过IP获取位置
   */
  getLocationByIP() {
    wx.showLoading({
      title: '尝试IP定位...',
      mask: true
    });
    
    // 尝试使用多个IP定位服务，提高定位精度
    const locationPromises = [];
    
    // 百度地图IP定位API
    locationPromises.push(new Promise((resolve, reject) => {
      wx.request({
        url: 'https://api.map.baidu.com/location/ip?ak=720fd0502a808129060081304a3b87b3&coor=gcj02',
        timeout: 8000,
        success: (res) => {
          if (res.data.status === 0) {
            resolve({
              latitude: res.data.content.point.y,
              longitude: res.data.content.point.x,
              accuracy: res.data.content.address_detail.district ? 1000 : 5000,
              provider: 'baidu'
            });
          } else {
            reject('百度IP定位失败');
          }
        },
        fail: (err) => {
          reject('百度IP定位请求失败');
        }
      });
    }));
    
    // 腾讯位置服务IP定位API
    locationPromises.push(new Promise((resolve, reject) => {
      wx.request({
        url: 'https://apis.map.qq.com/ws/location/v1/ip?key=7L6BZ-3P63X-7O645-PJPOZ-LA3XQ-N6BQ5',
        timeout: 8000,
        success: (res) => {
          if (res.data.status === 0) {
            resolve({
              latitude: res.data.result.location.lat,
              longitude: res.data.result.location.lng,
              accuracy: res.data.result.ad_info ? 1000 : 5000,
              provider: 'tencent'
            });
          } else {
            reject('腾讯IP定位失败');
          }
        },
        fail: (err) => {
          reject('腾讯IP定位请求失败');
        }
      });
    }));
    
    // 等待所有IP定位请求完成
    Promise.allSettled(locationPromises).then((results) => {
      wx.hideLoading();
      
      // 过滤成功的定位结果
      const successfulResults = results.filter(result => result.status === 'fulfilled').map(result => result.value);
      
      if (successfulResults.length > 0) {
        // 计算平均位置
        let totalLat = 0;
        let totalLng = 0;
        let totalAccuracy = 0;
        
        for (const result of successfulResults) {
          totalLat += result.latitude;
          totalLng += result.longitude;
          totalAccuracy += result.accuracy;
        }
        
        const avgLat = totalLat / successfulResults.length;
        const avgLng = totalLng / successfulResults.length;
        const avgAccuracy = totalAccuracy / successfulResults.length;
        
        // 设置当前位置
        this.setData({
          currentLocation: {
            latitude: avgLat,
            longitude: avgLng,
            accuracy: avgAccuracy,
            altitude: 0,
            speed: 0,
            heading: 0
          }
        });
        
        this.updateLocationAccuracyText(avgAccuracy);
        
        wx.showToast({
          title: `已获取IP位置(来自${successfulResults.length}个服务)`,
          icon: 'success'
        });
        
        // 如果有订单，计算到第一个订单的路线
        if (this.data.orderList.length > 0) {
          this.calculateRouteToOrder(this.data.orderList[0]);
        }
      } else {
        // 所有IP定位服务都失败，显示详细错误
        console.error('所有IP定位服务都失败:', results);
        wx.showModal({
          title: 'IP定位失败',
          content: '无法通过IP获取位置，将使用默认位置',
          showCancel: false,
          success: () => {
            this.setDefaultLocation();
          }
        });
      }
    });
  },
  
  /**
   * 设置默认位置
   */
  setDefaultLocation() {
    this.setData({
      currentLocation: {
        latitude: 39.908823,
        longitude: 116.397470,
        accuracy: 5000,
        altitude: 0,
        speed: 0,
        heading: 0
      }
    });
    
    this.updateLocationAccuracyText(5000);
    
    wx.showToast({
      title: '已使用默认位置',
      icon: 'none'
    });
    
    // 如果有订单，计算到第一个订单的路线
    if (this.data.orderList.length > 0) {
      this.calculateRouteToOrder(this.data.orderList[0]);
    }
  },

  /**
   * 显示手动设置位置弹窗
   */
  showManualLocationModal() {
    this.setData({
      showManualLocationModal: true
    });
  },

  /**
   * 隐藏手动设置位置弹窗
   */
  hideManualLocationModal() {
    this.setData({
      showManualLocationModal: false
    });
  },

  /**
   * 设置手动选择的位置
   */
  setManualLocation(e) {
    const latitude = parseFloat(e.currentTarget.dataset.latitude);
    const longitude = parseFloat(e.currentTarget.dataset.longitude);
    const address = e.currentTarget.dataset.address;
    
    this.setData({
      currentLocation: {
        latitude: latitude,
        longitude: longitude,
        accuracy: 10,
        altitude: 0,
        speed: 0,
        heading: 0
      },
      showManualLocationModal: false
    });
    
    wx.showToast({
      title: `已设置位置为${address}`,
      icon: 'success'
    });
    
    // 如果有订单，计算到第一个订单的路线
    if (this.data.orderList.length > 0) {
      this.calculateRouteToOrder(this.data.orderList[0]);
    }
  },

  /**
   * 处理纬度输入
   */
  onLatitudeInput(e) {
    this.setData({
      customLatitude: e.detail.value
    });
  },

  /**
   * 处理经度输入
   */
  onLongitudeInput(e) {
    this.setData({
      customLongitude: e.detail.value
    });
  },

  /**
   * 确认自定义位置
   */
  confirmCustomLocation() {
    const latitude = parseFloat(this.data.customLatitude);
    const longitude = parseFloat(this.data.customLongitude);
    
    if (isNaN(latitude) || isNaN(longitude)) {
      wx.showModal({
        title: '输入错误',
        content: '请输入有效的经纬度',
        showCancel: false
      });
      return;
    }
    
    this.setData({
      currentLocation: {
        latitude: latitude,
        longitude: longitude,
        accuracy: 10,
        altitude: 0,
        speed: 0,
        heading: 0
      },
      showManualLocationModal: false
    });
    
    this.updateLocationAccuracyText(10);
    
    wx.showToast({
      title: '已设置自定义位置',
      icon: 'success'
    });
    
    // 如果有订单，计算到第一个订单的路线
    if (this.data.orderList.length > 0) {
      this.calculateRouteToOrder(this.data.orderList[0]);
    }
  },

  /**
   * 更新定位精度文本
   */
  updateLocationAccuracyText(accuracy) {
    let accuracyText = '未知';
    
    if (accuracy <= 10) {
      accuracyText = '精确 (≤10m)';
    } else if (accuracy <= 100) {
      accuracyText = '高 (≤100m)';
    } else if (accuracy <= 1000) {
      accuracyText = '中等 (≤1km)';
    } else if (accuracy <= 5000) {
      accuracyText = '低 (≤5km)';
    } else {
      accuracyText = '非常低 (>5km)';
    }
    
    this.setData({
      locationAccuracyText: accuracyText
    });
  },

  /**
   * 获取骑手列表
   */
  getRiderList() {
    // 调用接口获取所有骑手位置
    wx.request({
      url: 'http://localhost:8080/rider/all-locations',
      success: (res) => {
        this.setData({
          riderList: res.data
        });
        this.updateMarkers();
      }
    });
  },

  /**
   * 计算到订单的路线
   */
  calculateRouteToOrder(order) {
    if (!this.data.currentLocation) return;
    
    wx.request({
      url: 'http://localhost:8080/map/calculate-route',
      method: 'POST',
      data: {
        originLatitude: this.data.currentLocation.latitude,
        originLongitude: this.data.currentLocation.longitude,
        destinationAddress: order.address
      },
      success: (res) => {
        if (res.data.code === 1) {
          const routeData = res.data.data;
          this.drawRoute(routeData, order);
        } else {
          wx.showToast({
            title: '路线计算失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        console.error('路线计算请求失败:', err);
        // 如果后端API失败，使用模拟数据
        this.useMockRouteData(order);
      }
    });
  },

  /**
   * 绘制路线
   */
  drawRoute(routeData, order) {
    const polyline = [
      {
        points: routeData.points,
        color: '#00BFFF',
        width: 5,
        dottedLine: false
      }
    ];

    // 更新标记，添加起点和终点
    let markers = this.data.markers;
    // 移除旧的起点和终点标记
    markers = markers.filter(marker => !marker.id || !['start', 'end'].includes(marker.id));
    
    // 添加起点标记
    markers.push({
      id: 'start',
      latitude: this.data.currentLocation.latitude,
      longitude: this.data.currentLocation.longitude,
      iconPath: '/static/start.png',
      width: 30,
      height: 30,
      title: '我的位置'
    });
    
    // 添加终点标记
    markers.push({
      id: 'end',
      latitude: routeData.destinationLatitude,
      longitude: routeData.destinationLongitude,
      iconPath: '/static/end.png',
      width: 30,
      height: 30,
      title: order.address
    });

    this.setData({
      polyline: polyline,
      markers: markers
    });
  },

  /**
   * 使用模拟路线数据
   */
  useMockRouteData(order) {
    // 模拟路线数据，使用直线连接起点和终点
    const startLatitude = this.data.currentLocation.latitude;
    const startLongitude = this.data.currentLocation.longitude;
    // 使用固定的终点坐标作为模拟数据
    const endLatitude = 39.908823;
    const endLongitude = 116.397470;
    
    // 创建简单的直线路线
    const points = [];
    // 插入起点
    points.push({
      latitude: startLatitude,
      longitude: startLongitude
    });
    // 插入终点
    points.push({
      latitude: endLatitude,
      longitude: endLongitude
    });
    
    // 创建路线
    const polyline = [
      {
        points: points,
        color: '#00BFFF',
        width: 5,
        dottedLine: false
      }
    ];
    
    // 更新标记
    let markers = this.data.markers;
    // 移除旧的起点和终点标记
    markers = markers.filter(marker => !marker.id || !['start', 'end'].includes(marker.id));
    
    // 添加起点标记
    markers.push({
      id: 'start',
      latitude: startLatitude,
      longitude: startLongitude,
      iconPath: '/static/start.png',
      width: 30,
      height: 30,
      title: '我的位置'
    });
    
    // 添加终点标记
    markers.push({
      id: 'end',
      latitude: endLatitude,
      longitude: endLongitude,
      iconPath: '/static/end.png',
      width: 30,
      height: 30,
      title: order.address
    });

    this.setData({
      polyline: polyline,
      markers: markers
    });
    
    wx.showToast({
      title: '使用模拟路线数据',
      icon: 'none'
    });
  },

  /**
   * 更新地图标记
   */
  updateMarkers() {
    let markers = [];
    
    // 添加骑手标记
    this.data.riderList.forEach(rider => {
      markers.push({
        id: rider.riderId,
        latitude: rider.latitude,
        longitude: rider.longitude,
        iconPath: '/static/boy.png',
        width: 30,
        height: 30,
        title: `骑手${rider.riderId}`
      });
    });
    
    // 添加订单标记
    this.data.orderList.forEach(order => {
      // 添加订单地址标记
      markers.push({
        id: `order-${order.id}`,
        // 使用假数据作为经纬度，实际项目中应该从后端获取
        latitude: 39.908823,
        longitude: 116.397470,
        iconPath: '/static/address.png',
        width: 25,
        height: 25,
        title: `订单地址: ${order.address}`
      });
    });
    
    this.setData({
      markers: markers
    });
  },

  /**
   * 显示骑手列表
   */
  showRiderList() {
    // 可以添加显示骑手列表的逻辑
    wx.showToast({
      title: `共有${this.data.riderList.length}位骑手在线`,
      icon: 'none'
    });
  },

  /**
   * 查看订单详情
   */
  viewOrderDetail(e) {
    const orderId = e.currentTarget.dataset.orderId;
    // 调用接口获取订单详情
    wx.request({
      url: `http://localhost:8080/order/details/${orderId}`,
      success: (res) => {
        this.setData({
          selectedOrder: res.data,
          showOrderDetail: true
        });
      }
    });
  },

  /**
   * 关闭订单详情
   */
  closeOrderDetail() {
    this.setData({
      showOrderDetail: false,
      selectedOrder: null
    });
  },

  /**
   * 接单
   */
  acceptOrder(e) {
    const orderId = e.currentTarget.dataset.orderId;
    // 调用接口接单
    wx.request({
      url: `http://localhost:8080/rider/accept?orderId=${orderId}`,
      method: 'POST',
      header: {
        'content-type': 'application/json',
        // 需要在请求头中添加token
        'Authorization': `Bearer ${wx.getStorageSync('token')}`
      },
      success: (res) => {
        if (res.data) {
          wx.showToast({
            title: '接单成功',
            icon: 'success'
          });
          // 刷新订单列表
          this.getOrderList();
        } else {
          wx.showToast({
            title: '接单失败',
            icon: 'none'
          });
        }
      }
    });
  },

  /**
   * 抢单
   */
  grabOrder(e) {
    const orderId = e.currentTarget.dataset.orderId;
    // 调用接口抢单
    wx.request({
      url: `http://localhost:8080/rider/grab?orderId=${orderId}`,
      method: 'POST',
      header: {
        'content-type': 'application/json',
        // 需要在请求头中添加token
        'Authorization': `Bearer ${wx.getStorageSync('token')}`
      },
      success: (res) => {
        if (res.data) {
          wx.showToast({
            title: '抢单成功',
            icon: 'success'
          });
          // 刷新订单列表
          this.getOrderList();
        } else {
          wx.showToast({
            title: '抢单失败',
            icon: 'none'
          });
        }
      }
    });
  },

  /**
   * 地图标记点击事件
   */
  onMarkerTap(e) {
    const markerId = e.markerId;
    // 检查是否是订单标记
    if (typeof markerId === 'string' && markerId.startsWith('order-')) {
      const orderId = parseInt(markerId.split('-')[1]);
      const order = this.data.orderList.find(item => item.id === orderId);
      if (order) {
        // 跳转到地图详情页面
        wx.navigateTo({
          url: `/pages/map-detail/map-detail?orderId=${orderId}&orderAddress=${encodeURIComponent(order.address)}`
        });
      }
    }
  }
})