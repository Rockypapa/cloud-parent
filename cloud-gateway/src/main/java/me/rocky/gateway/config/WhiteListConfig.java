package me.rocky.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 白名单配置
 */

@Configuration
@ConfigurationProperties(prefix = "system.whitelist")
public class WhiteListConfig {

    private String[] urls;

    public String[] getUrls() {
        return urls;
    }

    public WhiteListConfig setUrls(String[] urls) {
        this.urls = urls;
        return this;
    }
}
