package com.sky.mapper.rider;

import com.sky.entity.Orders;
import com.sky.entity.RiderLocation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Insert;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RiderMapper {
    /**
     * 获取待接单订单列表
     * @return 待接单订单列表
     */
    @Select("select * from orders where status = #{status} order by order_time desc")
    List<Orders> getPendingOrders(Integer status);

    /**
     * 更新订单状态为已接单并设置骑手ID
     * @param orderId 订单ID
     * @param riderId 骑手ID
     * @param newStatus 新状态
     * @param oldStatus 旧状态
     * @return 更新影响的行数
     */
    @Update("update orders set status = #{newStatus}, rider_id = #{riderId} where id = #{orderId} and status = #{oldStatus}")
    int updateOrderStatusAndRider(@Param("orderId") Long orderId, @Param("riderId") Long riderId, 
                                 @Param("newStatus") Integer newStatus, @Param("oldStatus") Integer oldStatus);

    /**
     * 更新订单状态为已送达并设置送达时间
     * @param orderId 订单ID
     * @param riderId 骑手ID
     * @param status 新状态
     * @param deliveryTime 送达时间
     * @return 更新影响的行数
     */
    @Update("update orders set status = #{status}, delivery_time = #{deliveryTime} where id = #{orderId} and rider_id = #{riderId}")
    int updateOrderToDelivered(@Param("orderId") Long orderId, @Param("riderId") Long riderId, 
                              @Param("status") Integer status, @Param("deliveryTime") LocalDateTime deliveryTime);

    /**
     * 更新或插入骑手位置
     * @param riderLocation 骑手位置信息
     * @return 更新影响的行数
     */
    @Insert("INSERT INTO rider_location (rider_id, latitude, longitude, update_time) VALUES (#{riderId}, #{latitude}, #{longitude}, #{updateTime}) ON DUPLICATE KEY UPDATE latitude = #{latitude}, longitude = #{longitude}, update_time = #{updateTime}")
    int updateRiderLocation(RiderLocation riderLocation);

    /**
     * 获取骑手当前位置
     * @param riderId 骑手ID
     * @return 骑手位置信息
     */
    @Select("SELECT * FROM rider_location WHERE rider_id = #{riderId}")
    RiderLocation getRiderLocation(@Param("riderId") Long riderId);

    /**
     * 获取所有骑手的最新位置
     * @return 所有骑手的最新位置列表
     */
    @Select("SELECT * FROM rider_location ORDER BY update_time DESC")
    List<RiderLocation> getAllRiderLocations();

    /**
     * 获取所有骑手用户ID列表
     * @return 骑手用户ID列表
     */
    @Select("SELECT id FROM user")
    List<Long> getAllRiderIds();

    /**
     * 获取骑手历史订单列表
     * @param riderId 骑手ID
     * @return 骑手历史订单列表
     */
    @Select("select * from orders where rider_id = #{riderId} order by order_time desc")
    List<Orders> getRiderOrderHistory(Long riderId);

    /**
     * 根据订单号查询订单
     * @param orderNumber 订单号
     * @return 订单信息
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getOrderByNumber(String orderNumber);
}
