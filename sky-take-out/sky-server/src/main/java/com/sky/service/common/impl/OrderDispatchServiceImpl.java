package com.sky.service.common.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.sky.exception.BaseException;
import com.sky.exception.CourierNotExistException;
import com.sky.exception.MerchantNotFoundException;
import com.sky.mapper.admin.MerchantMapper;
import com.sky.mapper.rider.RiderMapper;
import com.sky.mapper.rider.RiderTradeOrderMapper;
import com.sky.mapper.user.OrderMapper;
import com.sky.pojo.*;
import com.sky.service.common.OrderDispatchService;
import com.sky.utils.MapUtils;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderDispatchServiceImpl implements OrderDispatchService {

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private RiderMapper courierMapper;

    @Autowired
    private RiderTradeOrderMapper courierTradeOrderMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 派单
     *
     * @return
     */
    @Override
    @Transactional
    public Courier dispatch(Orders orders, Long tradeNo, Long courierId) {
        // 判断是否转单
        if (ObjectUtil.isNotEmpty(tradeNo)){
            orders = orderMapper.getByTradeNo(tradeNo);
            if (ObjectUtil.isEmpty(orders)){
                throw new BaseException("运单或订单不存在");
            }
        }
        // 1.选择骑手
        // 1.1 优先查找商家十公里内的骑手-多个
        List<Courier> courierList = findCourierNearMerchant(1L);

        // 转单剔除当前骑手
        courierList = courierList.stream().filter(courier -> {
            return ObjectUtil.notEqual(courier.getId(), courierId);
        }).collect(Collectors.toList());

        // 1.2 在上面的骑手中查找订单最少的骑手 key id total 订单数
        List<Long> courierIds = courierList.stream().map(courier -> courier.getId()).collect(Collectors.toList());
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"););
        // 查询骑手得今日订单数
        List<CourierTodayTotalOrder> courierTodayTotalOrders = courierMapper.findTodayCourierOrderNumByCourierId(courierIds, today);
        // 1.3 找出最小订单相近的骑手（3单之内） 10 - 10 11 12 13
        Long min = courierTodayTotalOrders.stream().min(Comparator.comparing(CourierTodayTotalOrder::getTotal)).get().getTotal();
        List<Long> newMinOrderNumCourierIds = courierTodayTotalOrders.stream().filter(courierTodayTotalOrder -> {
                    return courierTodayTotalOrder.getTotal() < (min + 3);
                })
                .map(courierTodayTotalOrder -> {
                    return courierTodayTotalOrder.getCourierId();
                }).collect(Collectors.toList());
        // 1.4 骑手的待接单和已结单不能超过十二个
        List<Long> courierIds2 = courierMapper.findCourierOrderingLess12ByIds(newMinOrderNumCourierIds, today);

        // 1.5 找不到的话，从十公里内的骑手随机挑选一个
        if (CollUtil.isEmpty(courierIds2)) {
            Random random = new Random();
            int index = random.nextInt(courierList.size());
            return courierList.get(index);
        }
        // 1.6 找到所有满足条件的骑手按照评分派单
        courierList = courierList.stream().filter(courier -> courierIds2.contains(courier.getId()))
                .sorted(Comparator.comparing(Courier::getScore).reversed()).collect(Collectors.toList());

        Courier courier = courierList.get(0);
        if (ObjectUtil.isEmpty(courier)){
            throw new CourierNotExistException("没有找到骑手");
        }

        // 7.骑手订单表添加数据     -- 此处后期需要优化业务 （骑手拒单时，需要重新分配骑手）
        saveTradeOrder(orders, courier, tradeNo);
        // 3.通知骑手
        // notifyCourier(courier.getId(), orders.getNumber());


        return courierList.get(0);
    }

    private void notifyCourier(Long courierId, String number) {
        Map<String, Object> map = new HashMap<>();
        map.put("tradeNo", number);
        map.put("code", 1);
        map.put("msg", "您有新的待接单,请及时查收！");
        String message = JSONUtil.toJsonStr(map);

        webSocketServer.sendRiderInfo(message, courierId);
    }


    // 添加运单数据入库
    private void saveTradeOrder(Orders orders, Courier courier, Long tradeNo) {

        // 判断是否是转单操作
        if (ObjectUtil.isNotEmpty(tradeNo)){
            courierTradeOrderMapper.updateCourierByTradeNo(tradeNo, courier.getId());
            return;
        }


        // 获取运费
        Merchant merchant = merchantMapper.selectById(orders.getShopId());
        //计算提成
        /**
         * - 1 公里内                  3块    取5成
         * - 超出1公里部分 少于3公里部分  3-5   取6成
         * - 超出3公里部分  0.7         >5    取7成
         *    公里数换算成配送费
         */
        double deliverFee = orders.getDeliveryFee().doubleValue();
        BigDecimal courierCost = null;

        if (deliverFee < 3) {
            courierCost = BigDecimal.valueOf(1.5);
        } else if (3 < deliverFee && deliverFee <= 5) {
            courierCost = BigDecimal.valueOf(1.5);
            BigDecimal otherCost = BigDecimal.valueOf(deliverFee - 3).multiply(BigDecimal.valueOf(0.6));
            courierCost = courierCost.add(otherCost);
        } else if (deliverFee > 5) {
            courierCost = BigDecimal.valueOf(1.5);
            BigDecimal otherCost = BigDecimal.valueOf(1.2);
            BigDecimal otherCost1 = BigDecimal.valueOf(deliverFee - 5).multiply(BigDecimal.valueOf(0.7));
            courierCost = courierCost.add(otherCost1).add(otherCost);

        }

        CourierTradeOrder tradeOrder = CourierTradeOrder.builder()
                .totalAmount(orders.getAmount())
                .courierId(courier.getId())
                .orderNumber(orders.getNumber())
                .waybillStatus(Orders.PAID) //1 ： 待接单
                .tradeCreateTime(LocalDateTime.now())
                .shopAddress(merchant.getShopAddress())
                .remark(orders.getRemark())
                .customerAddress(orders.getAddress())  //客户地址
                .customerExpectedDeliveryTime(orders.getEstimatedDeliveryTime()) //预计送达时间
                .customerName(orders.getConsignee())
                .shopName(merchant.getShopName())
                .telephone(orders.getPhone())
                .toBeMealTime(orders.getMealTime())
                .income(courierCost)
                .build();

        courierTradeOrderMapper.save(tradeOrder);
    }

    // 优先查找商家十公里内的骑手-多个
    public List<Courier> findCourierNearMerchant(Long shopId) {
        // 获取商家的信息
        Merchant merchant = merchantMapper.selectById(shopId);
        if (merchant == null) {
            throw new MerchantNotFoundException("找不到商家");
        }

        // 获取商家的经纬度信息
        String shop_lnglat = MapUtils.addressToLnglat(merchant.getShopAddress());

        if (StrUtil.isEmpty(shop_lnglat)){
            throw new BaseException("找不到上商家");
        }
        /*BigDecimal shopLng = merchant.getShopLng();
        BigDecimal shopLat = merchant.getShopLat();
        if (shopLng == null || shopLat == null) {
            throw new IllegalArgumentException("商家缺少经纬度信息");
        }*/

        //查询所有的在职骑手
        List<Courier> courierList = courierMapper.findAllOnJob();
        if(courierList==null||courierList.size()==0){
            return null;
        }
        // 判断该骑手当前位置距离商家是否十公里之内
        List<Courier> res = courierList.stream().filter(courier -> {
            double distance = MapUtils.calculateDistance(shop_lnglat, courier.getCurLocation());
            return distance <= 10000.0;
        }).collect(Collectors.toList());

        /*List<Courier> couriers = courierMapper.selectNearbyCouriers(
                shopLng.doubleValue(),
                shopLat.doubleValue(),
                10000  // 10公里 = 10000米
        );*/

        return res;
    }
}
