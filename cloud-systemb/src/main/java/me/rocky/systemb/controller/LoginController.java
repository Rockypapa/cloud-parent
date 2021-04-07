package me.rocky.systemb.controller;

import me.rocky.common.result.Result;
import me.rocky.constants.AuthConstants;
import me.rocky.model.AuthMemberDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/3/25 15:20
 * @log
 */
@RestController
public class LoginController {
	@GetMapping("/managerLogin")
	public Result<AuthMemberDto> managerLogin(@RequestParam(value = "username") String username){
		AuthMemberDto dto = new AuthMemberDto();
		dto.setId(1L)
				.setAvatar("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2099798253,42116547&fm=26&gp=0.jpg")
				.setClientId(AuthConstants.ADMIN_CLIENT_ID)
				.setNickname("snake")
				.setStatus(true)
				.setUsername(username)
				.setPassword("$2a$10$r9nDINxC8UfTxYooMGblruFkvLEzVQPmh.Wiv5CGzXfX8qbYAepgC")
				.setRoles(Arrays.asList("enterprise","user","ROLE_read","read"));
		return Result.success(dto);
	}
}
