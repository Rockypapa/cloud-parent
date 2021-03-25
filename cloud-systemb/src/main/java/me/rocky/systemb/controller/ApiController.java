package me.rocky.systemb.controller;

import me.rocky.common.result.Result;
import me.rocky.systemb.feign.WeappFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/3/25 16:01
 * @log
 */
@RestController
@RequestMapping("/api")
public class ApiController {
	@Autowired
	private WeappFeign weappFeign;
	@GetMapping("/getSystemAData")
	public Result<?> getData(@RequestParam("val")String val){
		return 	weappFeign.getData(val);
	}
}
