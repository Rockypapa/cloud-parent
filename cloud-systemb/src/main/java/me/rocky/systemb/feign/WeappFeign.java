package me.rocky.systemb.feign;

import me.rocky.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/3/25 16:10
 * @log
 */
@FeignClient("cloud-systema")
public interface WeappFeign {
	@GetMapping("/api/getData")
	Result<?> getData(@RequestParam("val")String val);
}
