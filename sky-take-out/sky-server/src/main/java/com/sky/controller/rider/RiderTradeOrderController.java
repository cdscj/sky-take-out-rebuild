package com.sky.controller.rider;

import com.sky.dto.CourierTradeQueryDto;
import com.sky.dto.PickUpDTO;
import com.sky.pojo.CourierTradeOrder;
import com.sky.result.Result;
import com.sky.service.rider.RiderTradeOrderService;
import com.sky.vo.CourierTradeOrderVO;
import com.sky.vo.OrderDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trade")
@Api(tags = "骑手接单抢单接口")
public class RiderTradeOrderController {

    @Autowired
    private RiderTradeOrderService courierTradeOrderService;

    /**
     * 骑手待接单
     *
     * @param courierTradeQueryDto
     * @return
     */
    @GetMapping("/queryPendingOrders")
    @ApiOperation("骑手待接单")
    public Result<List<CourierTradeOrderVO>> queryPendingOrders(CourierTradeQueryDto courierTradeQueryDto) {
        List<CourierTradeOrderVO> courierTradeOrderVOS = courierTradeOrderService.queryPendingOrders(courierTradeQueryDto);
        return Result.success(courierTradeOrderVOS);
    }

    /**
     * 待取货查询
     * @param courierTradeQueryDto
     * @return
     */
    @GetMapping("/queryTobePickedUpTradeOrders")
    @ApiOperation("待取货查询")
    public Result<List<CourierTradeOrderVO>> queryTobePickedUpTradeOrders(CourierTradeQueryDto courierTradeQueryDto) {
        List<CourierTradeOrderVO> courierTradeOrderVOS = courierTradeOrderService.queryPendingOrders(courierTradeQueryDto);
        return Result.success(courierTradeOrderVOS);
    }

    /**
     * 待送达查询
     * @param courierTradeQueryDto
     * @return
     */
    @GetMapping("/toBedeliveredList")
    @ApiOperation("待送达查询")
    public Result<List<CourierTradeOrderVO>> toBedeliveredList(CourierTradeQueryDto courierTradeQueryDto) {
        List<CourierTradeOrderVO> courierTradeOrderVOS = courierTradeOrderService.queryPendingOrders(courierTradeQueryDto);
        return Result.success(courierTradeOrderVOS);
    }

    /**
     * 已取消查询
     * @param courierTradeQueryDto
     * @return
     */
    @GetMapping("/queryCanceldTradeOrderList")
    @ApiOperation("已取消查询")
    public Result<List<CourierTradeOrderVO>> queryCanceldTradeOrderList(CourierTradeQueryDto courierTradeQueryDto) {
        List<CourierTradeOrderVO> courierTradeOrderVOS = courierTradeOrderService.queryPendingOrders(courierTradeQueryDto);
        return Result.success(courierTradeOrderVOS);
    }

    /**
     * 根据订单号查询运单
     * @param tradeNo
     * @return
     */
    @GetMapping("/queryTradeOrderByTradeNo")
    @ApiOperation("根据订单号查询运单")
    public Result<CourierTradeOrder> queryTradeOrderByTradeNo(String tradeNo){
        return courierTradeOrderService.queryTradeOrderByTradeNo(tradeNo);
    }

    /**
     * 查询运单菜品详情
     * @return
     */
    @GetMapping("/confirmPickUpList")
    @ApiOperation("查询运单菜品详情")
    public Result<List<OrderDetailVO>> confirmPickUpList(Long tradeNo){
        return courierTradeOrderService.confirmPickUpList(tradeNo);
    }

    /**
     * 确认取货
     * @param pickUpDTO
     * @return
     */
    @PutMapping("/confirmPickUp")
    @ApiOperation("确认取货")
    public Result confirmPickUp(@RequestBody PickUpDTO pickUpDTO){
        return courierTradeOrderService.confirmPickUp(pickUpDTO);
    }

    /**
     * 骑手的今日历史订单查询
     * @param courierTradeQueryDto
     * @return
     */
    @PostMapping("/queryWaybillsByDate")
    @ApiOperation("骑手的今日历史订单查询")
    public Result<List<CourierTradeOrderVO>> queryWaybillsByDate(CourierTradeQueryDto courierTradeQueryDto){
        return courierTradeOrderService.queryWaybillsByDate(courierTradeQueryDto);
    }
}
