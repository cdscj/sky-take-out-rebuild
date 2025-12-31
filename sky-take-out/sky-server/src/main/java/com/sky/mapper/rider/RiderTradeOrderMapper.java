package com.sky.mapper.rider;

import com.sky.pojo.CourierTradeOrder;
import com.sky.vo.OrderDetailVO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface RiderTradeOrderMapper {


    /**
     * 插入运单
     * @param tradeOrder
     */
    @Insert("INSERT INTO tb_courier_trade_order VALUES(NULL,#{totalAmount},#{courierId},#{orderNumber},#{waybillStatus},#{tradeCreateTime},#{tradeCompleteTime},#{tradeCancelTime},#{overTime},#{shopAddress},#{remark},#{customerAddress},#{customerExpectedDeliveryTime},#{customerName},#{shopName},#{toBeMealTime},#{telephone},#{income})")
    void save(CourierTradeOrder tradeOrder);

    /**
     * 根据骑手id和配送状态查询运单
     * @param courierId
     * @param status
     * @return
     */
    @Select("select * from tb_courier_trade_order where courier_id = #{courierId} and waybill_status = #{status}")
    List<CourierTradeOrder> selectByCourierIdAndStatus(@Param("courierId") Long courierId, @Param("status") Integer status);

    /**
     * 根据运单号查询运单
     * @param tradeNo
     * @return
     */
    @Select("select * from tb_courier_trade_order where trade_no = #{tradeNo}")
    CourierTradeOrder selectByTradeNo(Long tradeNo);

    /**
     * 根据运单号修改订单状态
     * @param tradeNo
     * @param status
     */
    @Update("update tb_courier_trade_order set waybill_status = #{status} where trade_no = #{tradeNo}")
    void updateStatusByTradeNo(@Param("tradeNo") Long tradeNo, @Param("status") Integer status);


    /**
     * 根据订单号修改订单状态
     * @param orderNumber
     * @param status
     */
    @Update("update tb_courier_trade_order set waybill_status = #{status} where order_number = #{orderNumber}")
    void updateStatusByOrderNumber(@Param("orderNumber") String orderNumber, @Param("status") Integer status);
    /**
     * 根据订单号查询运单
     * @param orderNumber
     * @return
     */
    @Select("select * from tb_courier_trade_order where order_number = #{orderNumber}")
    CourierTradeOrder selectByOrderNumber(String orderNumber);

    /**
     * 查询运单菜品详情
     * @param tradeNo
     * @return
     */
    List<OrderDetailVO> confirmPickUpList(Long tradeNo);

    /**
     * 根据运单号修改状态和完成时间
     * @param tradeNo
     * @param status
     * @param completeTime
     */
    @Update("update tb_courier_trade_order set waybill_status = #{status}, trade_complete_time = #{completeTime} where trade_no = #{tradeNo}")
    void updateStatusAndCompleteTimeByTradeNo(@Param("tradeNo") Long tradeNo, @Param("status") Integer status, @Param("completeTime") String completeTime);

    /**
     * 根据运单号修改骑手
     * @param tradeNo
     * @param courierId
     */
    @Update("update tb_courier_trade_order set courier_id = #{courierId} where trade_no = #{tradeNo}")
    void updateCourierByTradeNo(@Param("tradeNo") Long tradeNo, @Param("courierId") Long courierId);

    /**
     *  根据日期和运单状态和骑手查询今日运单
     * @param today
     * @param status
     * @param courierId
     * @return
     */
    List<CourierTradeOrder> queryWaybillsByDate(@Param("today") String today, @Param("status") Integer status, @Param("courierId") Long courierId);

    /**
     * 根据日期获取获取完成的订单数量
     * @param firstDayOfMonth
     * @param lastDayOfMonth
     * @param status
     * @param courierId
     * @return
     */
    Integer getTradeOrderCountByDateAndCount(@Param("courierId") Long courierId, @Param("firstDayOfMonth") LocalDate firstDayOfMonth, @Param("lastDayOfMonth") LocalDate lastDayOfMonth, @Param("status") Integer status);

    /**
     * 根据日期获取平均配送时间
     * @param courierId
     * @param firstDayOfMonth
     * @param lastDayOfMonth
     * @return
     */
    Double getAvgOfCompletedTime(@Param("courierId") Long courierId, @Param("firstDayOfMonth") LocalDate firstDayOfMonth, @Param("lastDayOfMonth") LocalDate lastDayOfMonth);

    /**
     * 获取一段时间内的所有按时完成的订单数
     * @param courierId
     * @param firstDayOfMonth
     * @param lastDayOfMonth
     * @return
     */
    Integer getTradeOrderOnTimeRate(@Param("courierId") Long courierId, @Param("firstDayOfMonth") LocalDate firstDayOfMonth, @Param("lastDayOfMonth") LocalDate lastDayOfMonth);

    /**
     * 获取日期内的每天的订单完成量
     *
     * @param courierId
     * @param firstDayOfMonth
     * @param lastDayOfMonth
     * @param dateList
     * @param status
     * @return
     */
    Map<String, Object> selectEveryDayOrdersCountByDateAndStatus(@Param("courierId") Long courierId, @Param("firstDayOfMonth") LocalDate firstDayOfMonth, @Param("lastDayOfMonth") LocalDate lastDayOfMonth, @Param("dateList") List<String> dateList, @Param("status") Integer status);
}
