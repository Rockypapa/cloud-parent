package me.rocky.systema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/2/26 15:48
 * @log
 */
@EnableDiscoveryClient
@SpringBootApplication
public class SystemApplicationA {
	public static void main(String[] args) {
		SpringApplication.run(SystemApplicationA.class);
	}

}
