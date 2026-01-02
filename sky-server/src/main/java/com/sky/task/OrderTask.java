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


    private final OrderMapper orderMapper;
    private final RiderService riderService;

    public OrderTask(OrderMapper orderMapper, RiderService riderService) {
        this.orderMapper = orderMapper;
        this.riderService = riderService;
    }

    /**
     * 处理订单超时方法
     */
    @Scheduled(cron = "0 * * * * ?") //每分钟触发一次
    public void processTimeoutOrder() {
        log.info("定时处理超时订单:{}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);

        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT,time);

        if(ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);//已取消
                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }


    /**
     * 处理一直处于派送中的订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("定时处理处于派送中的订单:{}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);

        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS,time);
        if(ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);//派送完成
                orderMapper.update(orders);
            }
        }

    }

    /**
     * 超时自动分配骑手
     */
    @Scheduled(cron = "0 * * * * ?") //每分钟触发一次
    public void autoAssignRider() {
        log.info("定时自动分配骑手:{}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusMinutes(-10);

        // 查询超过10分钟还未被接单的订单
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.TO_BE_CONFIRMED, time);

        if(ordersList != null && ordersList.size() > 0) {
            // 获取所有骑手ID列表
            List<Long> riderIds = riderService.getAllRiderIds();
            if(riderIds != null && riderIds.size() > 0) {
                Random random = new Random();
                for (Orders orders : ordersList) {
                    // 随机选择一个骑手
                    Long riderId = riderIds.get(random.nextInt(riderIds.size()));
                    
                    // 更新订单状态为已接单并设置骑手ID
                    Orders updateOrder = new Orders();
                    updateOrder.setId(orders.getId());
                    updateOrder.setStatus(Orders.CONFIRMED);
                    updateOrder.setRiderId(riderId);
                    
                    orderMapper.update(updateOrder);
                    log.info("订单{}已自动分配给骑手{}", orders.getId(), riderId);
                }
            } else {
                log.warn("没有可用的骑手进行自动分配");
            }
        }
    }
}
