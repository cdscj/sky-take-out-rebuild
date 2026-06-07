package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.map")
@Data
public class MapProperties {

    /** 高德地图 API Key */
    private String gaodeApiKey;

    /** 百度地图 AK */
    private String baiduAk;

}
