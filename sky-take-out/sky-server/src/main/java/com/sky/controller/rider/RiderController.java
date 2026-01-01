package com.sky.controller.rider;

import com.sky.context.BaseContext;
import com.sky.entity.Orders;
import com.sky.entity.RiderLocation;
import com.sky.service.rider.RiderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rider")
@Api(tags = "骑手接口")
public class RiderController {

    @Autowired
    private RiderService riderService;

    /**
     * 获取待接单订单列表
     * @return 待接单订单列表
     */
    @GetMapping("/pending-orders")
    @ApiOperation("获取待接单订单列表")
    public  List<Orders> getPendingOrders() {
        return riderService.getPendingOrders();
    }

    /**
     * 骑手接单
     * @param orderId 订单ID
     * @return 是否接单成功
     */
    @PostMapping("/accept")
    @ApiOperation("骑手接单")
    public boolean acceptOrder(@RequestParam Long orderId) {
        Long riderId = BaseContext.getCurrentId();
        return riderService.acceptOrder(orderId, riderId);
    }

    /**
     * 骑手抢单
     * @param orderId 订单ID
     * @return 是否抢单成功
     */
    @PostMapping("/grab")
    @ApiOperation("骑手抢单")
    public boolean grabOrder(@RequestParam Long orderId) {
        Long riderId = BaseContext.getCurrentId();
        return riderService.grabOrder(orderId, riderId);
    }

    /**
     * 骑手确认送达
     * @param orderId 订单ID
     * @return 是否确认成功
     */
    @PostMapping("/confirm-delivery")
    @ApiOperation("骑手确认送达")
    public boolean confirmDelivery(@RequestParam Long orderId) {
        Long riderId = BaseContext.getCurrentId();
        return riderService.confirmDelivery(orderId, riderId);
    }

    /**
     * 更新骑手位置
     * @param riderLocation 骑手位置信息
     * @return 是否更新成功
     */
    @PostMapping("/update-location")
    @ApiOperation("更新骑手位置")
    public boolean updateRiderLocation(@RequestBody RiderLocation riderLocation) {
        return riderService.updateRiderLocation(riderLocation);
    }

    /**
     * 获取骑手当前位置
     * @param riderId 骑手ID
     * @return 骑手位置信息
     */
    @GetMapping("/location/{riderId}")
    @ApiOperation("获取骑手当前位置")
    public RiderLocation getRiderLocation(@PathVariable Long riderId) {
        return riderService.getRiderLocation(riderId);
    }

    /**
     * 获取所有骑手的最新位置
     * @return 所有骑手的最新位置列表
     */
    @GetMapping("/all-locations")
    @ApiOperation("获取所有骑手最新位置")
    public List<RiderLocation> getAllRiderLocations() {
        return riderService.getAllRiderLocations();
    }

    @GetMapping("/order-history/{riderId}")
    @ApiOperation("获取骑手历史订单列表")
    public List<Orders> getRiderOrderHistory(@PathVariable Long riderId) {
        return riderService.getRiderOrderHistory(riderId);
    }

    /**
     * 根据订单号查询订单
     * @param orderNumber 订单号
     * @return 订单信息
     */
    @GetMapping("/order/query")
    @ApiOperation("根据订单号查询订单")
    public Orders getOrderByNumber(@RequestParam String orderNumber) {
        return riderService.getOrderByNumber(orderNumber);
    }
}
