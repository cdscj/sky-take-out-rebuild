package com.sky.constant;

/**
 * 高德 & 百度地图 API 端点 URL 常量。
 * API Key 已迁移至 {@link com.sky.properties.MapProperties}，通过环境变量注入。
 */
public class MapConstant {
    public static final String GAODE_DISTANCE_URL = "https://restapi.amap.com/v3/distance";
    public static final String GAODE_GEO_URL = "https://restapi.amap.com/v3/geocode/geo";
    public static final String BAIDU_GEO_URL = "https://api.map.baidu.com/geocoding/v3/";
    public static final String GAODE_BICYCLING_ROUTE_PALN = "https://restapi.amap.com/v5/direction/bicycling";
    public static final String BAIDU_BICYCLING_ROUTE_PALN = "https://api.map.baidu.com/directionlite/v1/riding";
}
