package com.sky.service.common.impl;

import cn.hutool.core.util.ObjectUtil;
import com.sky.exception.CourierNotExistException;
import com.sky.exception.TradeOrderIsCompletedException;
import com.sky.mapper.common.ShotUriMapper;
import com.sky.mapper.rider.RiderMapper;
import com.sky.mapper.rider.RiderTradeOrderMapper;
import com.sky.pojo.Courier;
import com.sky.pojo.CourierTradeOrder;
import com.sky.result.Result;
import com.sky.service.common.CommonService;
import com.sky.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private RiderTradeOrderMapper courierTradeOrderMapper;
    @Autowired
    private RiderMapper courierMapper;
    @Autowired
    private ShotUriMapper shotUriMapper;
    /**
     * 查询位置
     * @param tradeNo
     * @param courierId
     * @param expire
     * @return
     */
    @Override
    public Result loadRouteInfo(Long tradeNo, Long courierId, LocalDateTime expire) {

        // 判断订单是否已经完成，如果已经完成，那么都不允许查看
        CourierTradeOrder courierTradeOrder = courierTradeOrderMapper.selectByTradeNo(tradeNo);
        if (ObjectUtil.isEmpty(courierTradeOrder)){
            throw new TradeOrderIsCompletedException("找不到运单");
        }
        if (courierTradeOrder.getTradeCompleteTime() != null){
            throw new TradeOrderIsCompletedException("订单已结束");
        }
        // 客户位置的经纬度信息
        String customerAddress = courierTradeOrder.getCustomerAddress();
        String customerLngLat = MapUtils.addressToLnglat(customerAddress);
        // 店铺位置的经纬度信息
        String shopLngLat = MapUtils.addressToLnglat(courierTradeOrder.getShopAddress());
        // 骑手位置
        Courier courier = courierMapper.selectById(courierId);
        if (ObjectUtil.isEmpty(courier)){
            throw new CourierNotExistException("骑手不存在");
        }
        String riderLngLat = courier.getCurLocation();

        String locationStr = customerLngLat+"_"+shopLngLat+"_"+riderLngLat;

        return Result.success(locationStr);
    }

    @Override
    public String findUrlByCode(String code) {
        String fullUrl = shotUriMapper.selectFullUrlByCode(code);
        return fullUrl;
    }
}
