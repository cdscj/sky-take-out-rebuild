package com.sky.service.common;

import com.sky.result.Result;

import java.time.LocalDateTime;

public interface CommonService {

    /**
     * 查询位置
     * @param tradeNo
     * @param courierId
     * @param expire
     * @return
     */
    Result loadRouteInfo(Long tradeNo, Long courierId, LocalDateTime expire);

    String findUrlByCode(String code);
}
