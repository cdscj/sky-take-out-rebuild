package com.sky.service.rider.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.sky.dto.CourierTradeQueryDto;
import com.sky.dto.PickUpDTO;
import com.sky.exception.BaseException;
import com.sky.exception.CourierNotExistException;
import com.sky.exception.CourierTradeOrderNotExistException;
import com.sky.mapper.rider.RiderMapper;
import com.sky.mapper.rider.RiderTradeOrderMapper;
import com.sky.mapper.user.OrderMapper;
import com.sky.pojo.Courier;
import com.sky.pojo.CourierTradeOrder;
import com.sky.pojo.Orders;
import com.sky.result.Result;
import com.sky.service.rider.RiderTradeOrderService;
import com.sky.utils.MapUtils;
import com.sky.vo.CourierTradeOrderVO;
import com.sky.vo.OrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class RiderTradeOrderServiceImpl implements RiderTradeOrderService {
    @Autowired
    private RiderMapper courierMapper;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RiderTradeOrderMapper riderTradeOrderMapper;

    /**
     * 骑手待接单
     * @param courierTradeQueryDto
     * @return
     */
    @Override
    public List<CourierTradeOrderVO> queryPendingOrders(CourierTradeQueryDto courierTradeQueryDto) {
        // 1当前运单号，顾客期望送达时间，商家地址，顾客地址，商家与当前快递员的距离。商家和顾客的距离，当前时间和顾客期望的时间 ：分
        //1.查询数据库当前骑手的待接单信息
        Long courierId = courierTradeQueryDto.getCourierId();
        Integer status = courierTradeQueryDto.getStatus();
        List<CourierTradeOrder> courierTradeOrderList = riderTradeOrderMapper.selectByCourierIdAndStatus(courierId, status);

        List<CourierTradeOrderVO> courierTradeOrderVOList = new ArrayList<>();
        courierTradeOrderList.forEach(courierTradeOrder -> {
            getTradeOrderVoList(courierTradeOrder, courierTradeOrderVOList);
        });

        AtomicInteger num = new AtomicInteger(1);
        // 排序
        List<CourierTradeOrderVO> res = courierTradeOrderVOList.stream().sorted(Comparator.comparing(CourierTradeOrderVO::getShopAndCustomerDistance))
                .map(courierTradeOrderVO -> {
                    courierTradeOrderVO.setMarkSort(num.getAndIncrement());
                    return courierTradeOrderVO;
                }).collect(Collectors.toList());


        return res;
    }

    private void getTradeOrderVoList(CourierTradeOrder courierTradeOrder, List<CourierTradeOrderVO> courierTradeOrderVOList) {
        CourierTradeOrderVO courierTradeOrderVO = new CourierTradeOrderVO();
        BeanUtil.copyProperties(courierTradeOrder, courierTradeOrderVO);
        // 查询骑手的位置
        Courier courier = courierMapper.selectById(courierTradeOrder.getCourierId());
        if (ObjectUtil.isEmpty(courier)){
            throw new CourierNotExistException("找不到这个骑手");
        }
        //2.商家和骑手的距离
        String shopLocation = MapUtils.addressToLnglat(courierTradeOrder.getShopAddress());
        double shopAndCourierDistance = MapUtils.calculateDistance(shopLocation, courier.getCurLocation());
        courierTradeOrderVO.setShopAndCourierDistance(shopAndCourierDistance);
        //3.店铺和客户的距离
        if (ObjectUtil.isEmpty(courierTradeOrder)){
            throw new CourierTradeOrderNotExistException("运单不存在");
        }
        String customerAddress = MapUtils.addressToLnglat(courierTradeOrder.getCustomerAddress());
        if (StrUtil.isEmpty(customerAddress)){
            throw new BaseException("找不到顾客地址");
        }
        double customerAndShopDistance = MapUtils.calculateDistance(shopLocation, customerAddress);
        courierTradeOrderVO.setShopAndCustomerDistance(customerAndShopDistance);
        //4.当前时间和客户期望时间的差值
        long minutes = Duration.between(LocalDateTime.now(), courierTradeOrder.getCustomerExpectedDeliveryTime()).toMinutes();
        courierTradeOrderVO.setCurrentTimeAndCustomerExpectedTime(minutes);
        // 加入到list集合
        courierTradeOrderVOList.add(courierTradeOrderVO);
    }

    /**
     * 根据订单号查询运单
     * @param tradeNo
     * @return
     */
    @Override
    public Result<CourierTradeOrder> queryTradeOrderByTradeNo(String tradeNo) {
        CourierTradeOrder courierTradeOrder = riderTradeOrderMapper.selectByOrderNumber(tradeNo);
        return Result.success(courierTradeOrder);
    }

    /**
     * 查询运单菜品详情
     * @param tradeNo
     * @return
     */
    @Override
    public Result<List<OrderDetailVO>> confirmPickUpList(Long tradeNo) {
        List<OrderDetailVO> orderDetailVOS = riderTradeOrderMapper.confirmPickUpList(tradeNo);
        return Result.success(orderDetailVOS);
    }

    /**
     * 确认取货
     * @param pickUpDTO
     * @return
     */
    @Override
    @Transactional
    public Result confirmPickUp(PickUpDTO pickUpDTO) {
        // 修改运单状态
        CourierTradeOrder courierTradeOrder = riderTradeOrderMapper.selectByTradeNo(pickUpDTO.getTradeNo());
        if (ObjectUtil.isEmpty(courierTradeOrder)){
            throw new CourierNotExistException("运单不存在");
        }
        if (pickUpDTO.getStatus().equals(3)){
            riderTradeOrderMapper.updateStatusByTradeNo(pickUpDTO.getTradeNo(), CourierTradeOrder.CONFIRM_TRADE);
            // 修改订单状态
            orderMapper.updateStatusByNumber(courierTradeOrder.getOrderNumber(), Orders.TOBEDELIVERED);
        }
        if (pickUpDTO.getStatus().equals(4)){
            // 更新订单和运单状态和完成时间，将会redis中的bitmap置为false
            String completeTime = LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss");
            riderTradeOrderMapper.updateStatusAndCompleteTimeByTradeNo(pickUpDTO.getTradeNo(), CourierTradeOrder.TRADE_COMPLETE, completeTime);
            orderMapper.updateStatusAndDeliveryTimeByOrderNumber(courierTradeOrder.getOrderNumber(), completeTime, Orders.COMPLETE_ORDER);
        }

        return Result.success();
    }

    /**
     * 骑手的今日历史订单查询
     * @param dto
     * @return
     */
    @Override
    public Result<List<CourierTradeOrderVO>> queryWaybillsByDate(CourierTradeQueryDto dto) {
        if (ObjectUtil.isEmpty(dto)){
            throw new BaseException("订单参数为空");
        }
        // 根据日期和运单状态和骑手查询今日运单
        List<CourierTradeOrder> courierTradeOrderList = riderTradeOrderMapper.queryWaybillsByDate(dto.getDate(), dto.getStatus(), dto.getCourierId());

        // 组装成vo数据
        List<CourierTradeOrderVO> tradeOrderVOList = new ArrayList<>();

        courierTradeOrderList.forEach(tradeOrder -> {
            getTradeOrderVoList(tradeOrder, tradeOrderVOList);
        });

        return Result.success(tradeOrderVOList);
    }
}
