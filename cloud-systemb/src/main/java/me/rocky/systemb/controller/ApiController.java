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
	@GetMapping("/testRole")
	public Result<?> testRole(){
		return Result.success("this is read role ");
	}

	@GetMapping("/testPermit")
	public Result<?> testPermit(){
		return Result.success("this is user  authority ");
	}

	@GetMapping("/testPermit1")
	public Result<?> testPermit1(){
		return Result.success("this is enterprise  authority ");
	}

	@GetMapping("/testPermit2")
	public Result<?> testPermit2(){
		return Result.success("this is read  authority ");
	}
}
