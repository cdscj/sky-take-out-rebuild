package com.sky.service.common;

import com.sky.pojo.Courier;
import com.sky.pojo.Orders;

public interface OrderDispatchService {

    /**
     * 派单
     * @return
     */
    Courier dispatch(Orders orders, Long tradeNo, Long courierId);
}
