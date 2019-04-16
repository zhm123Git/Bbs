package com.github.pig.admin.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by yyn on 2018/11/26.
 */
@Configuration
@ConfigurationProperties(prefix = "weixin", ignoreUnknownFields = false)
@PropertySource("classpath:config/weixin.properties")
@Data
@Component
public class WeiXinProperties {
    private String appid;
    private String secret;
    private String grantType;
}
