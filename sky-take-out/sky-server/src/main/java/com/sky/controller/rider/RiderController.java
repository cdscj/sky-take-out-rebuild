package com.sky.controller.rider;

import com.sky.dto.CourierTradeQueryDto;
import com.sky.dto.UserLoginDTO;
import com.sky.pojo.Courier;
import com.sky.result.Result;
import com.sky.service.common.OrderDispatchService;
import com.sky.service.rider.RiderService;
import com.sky.vo.CourierTradeOrderDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courier")
@Api(tags = "骑手接口")
public class RiderController {

    @Autowired
    private RiderService courierService;
    @Autowired
    private OrderDispatchService orderDispatchService;
    /**
     * 快递员登录
     * @param dto
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("骑手登录")
    public Result login(@RequestBody UserLoginDTO dto){

        return courierService.login(dto);
    }

    /**
     * 骑手接单
     * @param courierId
     * @param tradeNo
     * @return
     */
    @ApiOperation("骑手接单")
    @GetMapping("/receivingOrder")
    public Result receivingOrder(Long courierId, Long tradeNo){
        return courierService.receivingOrder(courierId, tradeNo);
    }

    /**
     * 订单转派
     * @param tradeNo
     * @param courierId
     * @return
     */
    @GetMapping("/transferOrderToTaskPool")
    @ApiOperation("订单转派")
    public Result<Courier> transferOrderToTaskPool(Long tradeNo, Long courierId){
        Courier courier = orderDispatchService.dispatch(null, tradeNo, courierId);
        return Result.success(courier);
    }

    /**
     * 根据id查找骑手
     * @param id
     * @return
     */
    @GetMapping("/findById")
    @ApiOperation("根据id查找骑手")
    public Result<Courier> findById(Long id){
        return courierService.findById(id);
    }

    /**
     * 骑手的月度统计
     * @param dto
     * @return
     */
    @GetMapping("/findCourierMonthTradeOrderInfo")
    @ApiOperation("骑手的月度统计")
    public Result<CourierTradeOrderDetailVO> findCourierMonthTradeOrderInfo(CourierTradeQueryDto dto){
        return courierService.findCourierMonthTradeOrderInfo(dto);
    }

}
