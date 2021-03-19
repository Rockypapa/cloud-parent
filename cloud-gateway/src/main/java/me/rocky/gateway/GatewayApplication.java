package me.rocky.gateway;

import me.rocky.gateway.config.WhiteListConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/2/26 16:16
 * @log
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties({WhiteListConfig.class})
public class GatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class);
	}
}
