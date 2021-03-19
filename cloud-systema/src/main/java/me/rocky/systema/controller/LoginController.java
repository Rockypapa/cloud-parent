package me.rocky.systema.controller;


import me.rocky.common.result.Result;
import me.rocky.constants.AuthConstants;
import me.rocky.model.AuthMemberDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/2/26 15:53
 * @log
 */
@RestController
public class LoginController {

	@GetMapping("/getUserByOpenId/{openid}")
	public Result<AuthMemberDto> getUserByOpenId(@PathVariable(value = "openid") String openid){
		AuthMemberDto dto = new AuthMemberDto();
		dto.setId(1L)
			.setAvatar("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2099798253,42116547&fm=26&gp=0.jpg")
			.setClientId(AuthConstants.WEAPP_CLIENT_ID)
			.setNickname("nike")
			.setStatus(true)
			.setUsername("oI_ym5MXrDmQaD_v-yIggHWXi6SM")
			.setPassword("$2a$10$hUjxi.I9YGYYJPfUzZ8LAuSrSxR.9k88UqiKxWymKb/A7eFO9qqLO")
			.setRoles(Arrays.asList("user","enterprise"));
		return Result.success(dto);
	}
}
