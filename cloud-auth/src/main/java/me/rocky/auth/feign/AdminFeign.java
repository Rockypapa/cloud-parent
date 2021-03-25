package me.rocky.auth.feign;

import me.rocky.common.result.Result;
import me.rocky.model.AuthMemberDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/3/25 15:26
 * @log
 */
@FeignClient("cloud-systemb")
public interface AdminFeign {
	@GetMapping("/managerLogin")
	Result<AuthMemberDto> managerLogin(@RequestParam(value = "username") String username);
}
