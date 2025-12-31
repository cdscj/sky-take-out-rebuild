package com.sky.service.rider;

import com.sky.dto.CourierTradeQueryDto;
import com.sky.dto.UserLoginDTO;
import com.sky.pojo.Courier;
import com.sky.result.Result;
import com.sky.vo.CourierTradeOrderDetailVO;

public interface RiderService {
    /**
     * 快递员登录
     * @param dto
     * @return
     */
    Result login(UserLoginDTO dto);

    /**
     * 骑手接单
     * @param courierId
     * @param tradeNo
     * @return
     */
    Result receivingOrder(Long courierId, Long tradeNo);


    /**
     * 根据id查找骑手
     * @param id
     * @return
     */
    Result<Courier> findById(Long id);

    /**
     * 骑手的月度统计
     * @param dto
     * @return
     */
    Result<CourierTradeOrderDetailVO> findCourierMonthTradeOrderInfo(CourierTradeQueryDto dto);
}
