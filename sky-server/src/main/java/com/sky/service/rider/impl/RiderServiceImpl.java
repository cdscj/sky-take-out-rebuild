package com.sky.service.rider.impl;

import com.sky.entity.Orders;
import com.sky.entity.RiderLocation;
import com.sky.mapper.rider.RiderMapper;
import com.sky.service.rider.RiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RiderServiceImpl implements RiderService {

    @Autowired
    private RiderMapper riderMapper;

    /**
     * 获取待接单订单列表
     * @return 待接单订单列表
     */
    @Override
    public List<Orders> getPendingOrders() {
        return riderMapper.getPendingOrders(Orders.TO_BE_CONFIRMED);
    }

    /**
     * 骑手接单
     * @param orderId 订单ID
     * @param riderId 骑手ID
     * @return 是否接单成功
     */
    @Override
    public boolean acceptOrder(Long orderId, Long riderId) {
        // 将订单状态从待接单(2)更新为已接单(3)，并设置骑手ID
        int rows = riderMapper.updateOrderStatusAndRider(orderId, riderId, Orders.DELIVERY_IN_PROGRESS, Orders.TO_BE_CONFIRMED);
        return rows > 0;
    }

    /**
     * 骑手抢单
     * @param orderId 订单ID
     * @param riderId 骑手ID
     * @return 是否抢单成功
     */
    @Override
    public boolean grabOrder(Long orderId, Long riderId) {
        // 抢单和接单的逻辑相同，都是将订单状态从待接单(2)更新为已接单(3)，并设置骑手ID
        int rows = riderMapper.updateOrderStatusAndRider(orderId, riderId, Orders.DELIVERY_IN_PROGRESS, Orders.TO_BE_CONFIRMED);
        return rows > 0;
    }

    /**
     * 骑手确认送达
     * @param orderId 订单ID
     * @param riderId 骑手ID
     * @return 是否确认成功
     */
    @Override
    public boolean confirmDelivery(Long orderId, Long riderId) {
        // 获取当前时间作为送达时间
        LocalDateTime deliveryTime = LocalDateTime.now();
        // 将订单状态更新为已送达(5)，并设置送达时间
        int rows = riderMapper.updateOrderToDelivered(orderId, riderId, Orders.COMPLETED, deliveryTime);
        return rows > 0;
    }

    /**
     * 更新骑手位置
     * @param riderLocation 骑手位置信息
     * @return 是否更新成功
     */
    @Override
    public boolean updateRiderLocation(RiderLocation riderLocation) {
        // 设置更新时间为当前时间
        riderLocation.setUpdateTime(LocalDateTime.now());
        // 更新或插入骑手位置
        int rows = riderMapper.updateRiderLocation(riderLocation);
        return rows > 0;
    }

    /**
     * 获取骑手当前位置
     * @param riderId 骑手ID
     * @return 骑手位置信息
     */
    @Override
    public RiderLocation getRiderLocation(Long riderId) {
        return riderMapper.getRiderLocation(riderId);
    }

    /**
     * 获取所有骑手的最新位置
     * @return 所有骑手的最新位置列表
     */
    @Override
    public List<RiderLocation> getAllRiderLocations() {
        return riderMapper.getAllRiderLocations();
    }

    @Override
    public List<Long> getAllRiderIds() {
        return riderMapper.getAllRiderIds();
    }

    @Override
    public List<Orders> getRiderOrderHistory(Long riderId) {
        return riderMapper.getRiderOrderHistory(riderId);
    }

    @Override
    public Orders getOrderByNumber(String orderNumber) {
        return riderMapper.getOrderByNumber(orderNumber);
    }
}