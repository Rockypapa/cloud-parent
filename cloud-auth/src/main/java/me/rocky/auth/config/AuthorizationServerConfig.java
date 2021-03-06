package me.rocky.auth.config;

import com.alibaba.fastjson.JSON;

import me.rocky.auth.domain.User;
import me.rocky.auth.filter.CustomClientCredentialsTokenEndpointFilter;
import me.rocky.auth.service.JdbcClientDetailsServiceImpl;
import me.rocky.common.result.Result;
import me.rocky.common.result.ResultCode;
import me.rocky.constants.AuthConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/2/26 16:40
 * @log
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * 自定义认证异常响应数据
	 * @return
	 */
	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return (request, response, e) -> {
			response.setStatus(HttpStatus.OK.value());
			response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Cache-Control", "no-cache");
			Result result = Result.failed(ResultCode.CLIENT_AUTHENTICATION_FAILED);
			response.getWriter().print(JSON.toJSONString(result));
			response.getWriter().flush();
		};
	}


	/**
	 * 使用非对称加密算法对token签名
	 */
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setKeyPair(keyPair());
		return converter;
	}

	/**
	 * 从classpath下的密钥库中获取密钥对(公钥+私钥)
	 */
	@Bean
	public KeyPair keyPair() {
		KeyStoreKeyFactory factory = new KeyStoreKeyFactory(
				new ClassPathResource("youlai.jks"), "123456".toCharArray());
		KeyPair keyPair = factory.getKeyPair(
				"youlai", "123456".toCharArray());
		return keyPair;
	}

	/**
	 * JWT内容增强
	 */
	@Bean
	public TokenEnhancer tokenEnhancer() {
		return (accessToken, authentication) -> {
			Map<String, Object> map = new HashMap<>(2);
			User user = (User) authentication.getUserAuthentication().getPrincipal();
			map.put(AuthConstants.JWT_USER_ID_KEY, user.getId());
			map.put(AuthConstants.CLIENT_ID_KEY, user.getClientId());
			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(map);
			return accessToken;
		};
	}

	/**
	 * 配置客户端详情(数据库)
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		JdbcClientDetailsServiceImpl jdbcClientDetailsService = new JdbcClientDetailsServiceImpl(dataSource);
		jdbcClientDetailsService.setFindClientDetailsSql(AuthConstants.FIND_CLIENT_DETAILS_SQL);
		jdbcClientDetailsService.setSelectClientDetailsSql(AuthConstants.SELECT_CLIENT_DETAILS_SQL);
		clients.withClientDetails(jdbcClientDetailsService);
	}


	/**
	 * 配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
     */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
		tokenEnhancers.add(tokenEnhancer());
		tokenEnhancers.add(jwtAccessTokenConverter());
		tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

		endpoints

				.authenticationManager(authenticationManager)
				.accessTokenConverter(jwtAccessTokenConverter())
				.tokenEnhancer(tokenEnhancerChain)
				.userDetailsService(userDetailsService)
				// refresh token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
				//      1 重复使用：access token过期刷新时， refresh token过期时间未改变，仍以初次生成的时间为准
				//      2 非重复使用：access token过期刷新时， refresh token过期时间延续，在refresh token有效期内刷新便永不失效达到无需再次登录的目的
				.reuseRefreshTokens(true);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) {
		/*security.allowFormAuthenticationForClients();*/
		//重写filter实现客户端自定义异常处理
		CustomClientCredentialsTokenEndpointFilter endpointFilter = new CustomClientCredentialsTokenEndpointFilter(security);
		endpointFilter.afterPropertiesSet();
		endpointFilter.setAuthenticationEntryPoint(authenticationEntryPoint());
		security.addTokenEndpointAuthenticationFilter(endpointFilter);

		security.authenticationEntryPoint(authenticationEntryPoint())
				.tokenKeyAccess("isAuthenticated()")
				.checkTokenAccess("permitAll()");
	}
}
