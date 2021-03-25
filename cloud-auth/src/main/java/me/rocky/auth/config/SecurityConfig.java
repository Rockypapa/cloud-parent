package me.rocky.auth.config;

import cn.hutool.core.codec.Base64;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/2/26 17:05
 * @log
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
			.and()
			.authorizeRequests().antMatchers("/getPublicKey","/oauth/logout").permitAll()
			.antMatchers("/webjars/**","/doc.html","/swagger-resources/**","/v2/api-docs").permitAll()
			.anyRequest().authenticated()
			.and()
			.csrf().disable();
	}

	/**
	 * 如果不配置SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户
	 */
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean("passwordEncoder")
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
//		System.out.println(new BCryptPasswordEncoder().encode("oI_ym5MXrDmQaD_v-yIggHWXi6SM"));
		System.out.println(Base64.encode("admin:123456"));
//		System.out.println(new BCryptPasswordEncoder().encode("123456"));

	}
}
