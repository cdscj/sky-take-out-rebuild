package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.shoturi")
@Data
public class ShortUrlProperties {
    private String domainName;
    private String shortUrlPrefix;
    private String realResource;
}
