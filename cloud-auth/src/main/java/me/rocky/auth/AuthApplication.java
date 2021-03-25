package me.rocky.auth;

import me.rocky.auth.feign.AdminFeign;
import me.rocky.auth.feign.MemberFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/2/26 16:34
 * @log
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = {MemberFeign.class, AdminFeign.class})
public class AuthApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class);
	}
}
