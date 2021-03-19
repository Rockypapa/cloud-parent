package me.rocky.auth.feign;

import me.rocky.common.result.Result;
import me.rocky.model.AuthMemberDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/3/18 16:16
 * @log
 */
@FeignClient("cloud-systema")
public interface MemberFeign {

	/**
	 * 通过远程调用获取用户信息
	 * @param openid
	 * @return
	 */
	@GetMapping("/getUserByOpenId/{openid}")
	Result<AuthMemberDto> getUserByOpenId(@PathVariable(value = "openid") String openid);
}
