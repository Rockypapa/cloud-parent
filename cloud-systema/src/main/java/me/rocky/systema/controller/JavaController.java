package me.rocky.systema.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/3/19 16:36
 * @log
 */
@RequestMapping("/java")
@RestController
public class JavaController {
	@GetMapping("/c")
	public String c(){
		return "231231dasdasd";
	}
}
