package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.user.OrderMapper;
import com.sky.service.rider.RiderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;


/**
 * 自定义定时任务类
 */
@Component
@Slf4j
public class OrderTask {

    private static final int BATCH_LIMIT = 500;

    private final OrderMapper orderMapper;
    private final RiderService riderService;

    public OrderTask(OrderMapper orderMapper, RiderService riderService) {
        this.orderMapper = orderMapper;
        this.riderService = riderService;
    }

    /**
     * 处理订单超时——批量取消（单条 SQL）
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder() {
        try {
            LocalDateTime deadline = LocalDateTime.now().plusMinutes(-15);
            int count = orderMapper.batchCancelTimeoutOrders(
                    Orders.PENDING_PAYMENT, Orders.CANCELLED,
                    "订单超时，自动取消", deadline);
            if (count > 0) {
                log.info("批量取消超时订单 {} 笔", count);
            }
        } catch (Exception e) {
            log.error("批量取消超时订单异常", e);
        }
    }

    /**
     * 处理一直处于派送中的订单——批量完成
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder() {
        try {
            LocalDateTime deadline = LocalDateTime.now().plusMinutes(-60);
            int count = orderMapper.batchCompleteDeliveryOrders(
                    Orders.DELIVERY_IN_PROGRESS, Orders.COMPLETED, deadline);
            if (count > 0) {
                log.info("批量完成超时配送订单 {} 笔", count);
            }
        } catch (Exception e) {
            log.error("批量完成超时配送订单异常", e);
        }
    }

    /**
     * 超时自动分配骑手
     */
    @Scheduled(cron = "0 * * * * ?")
    public void autoAssignRider() {
        try {
            LocalDateTime deadline = LocalDateTime.now().plusMinutes(-10);
            List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(
                    Orders.TO_BE_CONFIRMED, deadline);

            if (ordersList != null && !ordersList.isEmpty()) {
                List<Long> riderIds = riderService.getAllRiderIds();
                if (riderIds != null && !riderIds.isEmpty()) {
                    Random random = new Random();
                    for (Orders orders : ordersList) {
                        Long riderId = riderIds.get(random.nextInt(riderIds.size()));
                        orderMapper.updateStatusAndRider(
                                orders.getId(), Orders.CONFIRMED, riderId);
                    }
                    log.info("自动分配骑手完成，处理订单 {} 笔", ordersList.size());
                } else {
                    log.warn("没有可用的骑手进行自动分配");
                }
            }
        } catch (Exception e) {
            log.error("自动分配骑手异常", e);
        }
    }
}
