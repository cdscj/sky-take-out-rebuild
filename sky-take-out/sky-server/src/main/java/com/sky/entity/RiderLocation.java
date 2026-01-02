package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 骑手位置实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiderLocation {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 骑手ID
     */
    private Long riderId;
    /**
     * 纬度
     */
    private Double latitude;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}