package me.rocky.systema.controller;

import me.rocky.base.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/3/19 15:08
 * @log
 */
@RequestMapping("/test")
@RestController
public class TestController {
	@GetMapping("/a")
	public String a(){
		System.out.println(BaseController.getRequest().getHeader("x-username"));
		return "this is role test";
	}

	@GetMapping("/b")
	public String b(){
		System.out.println(BaseController.getRequest().getHeader("x-username"));
		return "this is permission test";
	}
}
