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
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);

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

    /**
     * 根据订单号修改订单状态
     */
    @Update("update orders set status = #{status}, number = #{orderNumber}")
    void updateStatusByNumber(@Param("orderNumber") String orderNumber,
                              @Param("status") Integer status);

    /**
     * 根据订单号修改送达时间和状态
     */
    @Update("update orders set delivery_time = #{completeTime}, status = #{status} where number = #{orderNumber}")
    void updateStatusAndDeliveryTimeByOrderNumber(@Param("orderNumber") String orderNumber,
                                                  @Param("completeTime") String completeTime,
                                                  @Param("status") Integer status);


    /**
     * 根据运单号查询订单
     *
     * @param tradeNo
     * @return
     */
    com.sky.pojo.Orders getByTradeNo(Long tradeNo);
}
