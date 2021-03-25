package me.rocky.systemb;

import me.rocky.systemb.feign.WeappFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/2/26 16:11
 * @log
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = {WeappFeign.class})
public class SystemApplicationB {
	public static void main(String[] args) {
		SpringApplication.run(SystemApplicationB.class);
	}
}
