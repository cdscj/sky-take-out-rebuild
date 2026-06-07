package com.sky.mapper.user;


import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {


    /**
     * 插入订单数据
     * @param orders
     */
    void insert(Orders orders);


    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 分页条件查询并按下单时间排序
     * @param ordersPageQueryDTO
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);


    /**
     * 根据id查询订单
     * @param id
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);


    /**
     * 根据状态统计订单数量
     * @param status
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * 根据订单状态和下单时间查询订单
     * @param status
     * @param orderTime
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime} limit 500")
    List<Orders> getByStatusAndOrderTimeLT(@Param("status") Integer status, @Param("orderTime") LocalDateTime orderTime);

    /**
     * 批量取消超时订单（单条 SQL，避免逐条 UPDATE）
     */
    @Update("UPDATE orders SET status = #{newStatus}, cancel_reason = #{reason}, " +
            "cancel_time = NOW() WHERE status = #{oldStatus} AND order_time < #{time} LIMIT 500")
    int batchCancelTimeoutOrders(@Param("oldStatus") Integer oldStatus,
                                  @Param("newStatus") Integer newStatus,
                                  @Param("reason") String reason,
                                  @Param("time") LocalDateTime time);

    /**
     * 批量完成超时配送订单
     */
    @Update("UPDATE orders SET status = #{newStatus} " +
            "WHERE status = #{oldStatus} AND order_time < #{time} LIMIT 500")
    int batchCompleteDeliveryOrders(@Param("oldStatus") Integer oldStatus,
                                     @Param("newStatus") Integer newStatus,
                                     @Param("time") LocalDateTime time);

    /**
     * 更新订单状态并分配骑手
     */
    @Update("UPDATE orders SET status = #{status}, rider_id = #{riderId} WHERE id = #{id}")
    int updateStatusAndRider(@Param("id") Long id,
                              @Param("status") Integer status,
                              @Param("riderId") Long riderId);

    /**
     * 动态条件统计营业额数据
     * @param map
     * @return
     */
    Double sumByMap(Map map);

    /**
     * 动态条件统计订单数量
     * @param map
     * @return
     */
    Integer countByOrder(Map map);


    /**
     * 统计指定时间内的销量排名
     * @param begin
     * @param end
     * @return
     */
    List<GoodsSalesDTO> getSalesTop(LocalDateTime begin, LocalDateTime end);

    /**
     * 查询订单
     * @param map
     * @return
     */
    Integer countByMap(Map map);


}
