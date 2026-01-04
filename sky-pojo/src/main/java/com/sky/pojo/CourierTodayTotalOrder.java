package com.sky.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourierTodayTotalOrder {
    private Long courierId;
    private Long total;
}
