package com.sky.controller;

import com.sky.result.Result;
import com.sky.utils.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 地图相关接口
 */
@RestController
@RequestMapping("/map")
@Slf4j
public class MapController {

    /**
     * 计算骑行路线
     * @param params 包含起点坐标和终点地址的参数
     * @return 路线信息
     */
    @PostMapping("/calculate-route")
    public Result<Map<String, Object>> calculateRoute(@RequestBody Map<String, Object> params) {
        try {
            // 从请求参数中获取起点坐标和终点地址
            double originLatitude = Double.parseDouble(params.get("originLatitude").toString());
            double originLongitude = Double.parseDouble(params.get("originLongitude").toString());
            String destinationAddress = params.get("destinationAddress").toString();

            // 调用地图工具类计算路线
            Map<String, Object> routeInfo = MapUtils.calculateBicyclingRoute(originLatitude, originLongitude, destinationAddress);

            return Result.success(routeInfo);
        } catch (Exception e) {
            log.error("路线计算失败: {}", e.getMessage());
            return Result.error("路线计算失败");
        }
    }
}
