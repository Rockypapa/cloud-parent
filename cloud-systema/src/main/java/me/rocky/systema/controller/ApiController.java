package me.rocky.systema.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/3/19 15:09
 * @log
 */
@RestController
@RequestMapping("/api")
public class ApiController {

	@GetMapping("/b")
	public String b(){
		return "2312312";
	}
}
