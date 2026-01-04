package com.sky.service.rider;

import com.sky.entity.Orders;
import com.sky.entity.RiderLocation;
import java.util.List;

public interface RiderService {
    /**
     * 获取待接单订单列表
     * @return 待接单订单列表
     */
    List<Orders> getPendingOrders();

    /**
     * 骑手接单
     * @param orderId 订单ID
     * @param riderId 骑手ID
     * @return 是否接单成功
     */
    boolean acceptOrder(Long orderId, Long riderId);

    /**
     * 骑手抢单
     * @param orderId 订单ID
     * @param riderId 骑手ID
     * @return 是否抢单成功
     */
    boolean grabOrder(Long orderId, Long riderId);

    /**
     * 骑手确认送达
     * @param orderId 订单ID
     * @param riderId 骑手ID
     * @return 是否确认成功
     */
    boolean confirmDelivery(Long orderId, Long riderId);

    /**
     * 更新骑手位置
     * @param riderLocation 骑手位置信息
     * @return 是否更新成功
     */
    boolean updateRiderLocation(RiderLocation riderLocation);

    /**
     * 获取骑手当前位置
     * @param riderId 骑手ID
     * @return 骑手位置信息
     */
    RiderLocation getRiderLocation(Long riderId);

    /**
     * 获取所有骑手的最新位置
     * @return 所有骑手的最新位置列表
     */
    List<RiderLocation> getAllRiderLocations();

    /**
     * 获取所有骑手用户ID列表
     * @return 骑手用户ID列表
     */
    List<Long> getAllRiderIds();

    /**
     * 获取骑手历史订单列表
     * @param riderId 骑手ID
     * @return 骑手历史订单列表
     */
    List<Orders> getRiderOrderHistory(Long riderId);

    /**
     * 根据订单号查询订单
     * @param orderNumber 订单号
     * @return 订单信息
     */
    Orders getOrderByNumber(String orderNumber);
}
