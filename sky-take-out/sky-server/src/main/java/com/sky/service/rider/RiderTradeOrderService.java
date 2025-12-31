package com.sky.service.rider;

import com.sky.dto.CourierTradeQueryDto;
import com.sky.dto.PickUpDTO;
import com.sky.pojo.CourierTradeOrder;
import com.sky.result.Result;
import com.sky.vo.CourierTradeOrderVO;
import com.sky.vo.OrderDetailVO;

import java.util.List;

public interface RiderTradeOrderService {


    /**
     * 骑手待接单
     * @param courierTradeQueryDto
     * @return
     */
    List<CourierTradeOrderVO> queryPendingOrders(CourierTradeQueryDto courierTradeQueryDto);

    /**
     * 根据订单号查询运单
     * @param tradeNo
     * @return
     */
    Result<CourierTradeOrder> queryTradeOrderByTradeNo(String tradeNo);

    /**
     *查询运单菜品详情
     * @param tradeNo
     * @return
     */
    Result<List<OrderDetailVO>> confirmPickUpList(Long tradeNo);

    /**
     * 确认取货
     * @param pickUpDTO
     * @return
     */
    Result confirmPickUp(PickUpDTO pickUpDTO);

    /**
     * 骑手的今日历史订单查询
     * @param courierTradeQueryDto
     * @return
     */
    Result<List<CourierTradeOrderVO>> queryWaybillsByDate(CourierTradeQueryDto courierTradeQueryDto);
}
