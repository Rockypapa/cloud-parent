package me.rocky.gateway.config;


import me.rocky.common.result.ResultCode;
import me.rocky.constants.AuthConstants;
import me.rocky.gateway.security.AuthenticationManager;
//import me.rocky.gateway.security.AuthorizationManager;
import me.rocky.gateway.security.SecurityContextRepository;
import me.rocky.gateway.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import reactor.core.publisher.Mono;

/**
 * 资源服务器配置
 */

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class ResourceServerConfig {

    @Autowired
    private WhiteListConfig whiteListConfig;

    @Autowired
    @Qualifier(value = "authenticationManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

//    @Autowired
//    private AuthorizationManager authorizationManager;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
        http.oauth2ResourceServer().authenticationEntryPoint(authenticationEntryPoint());
        http.
                // 这两行要不要都无所谓，从效果上来看就是个打印日志的类
                securityContextRepository(securityContextRepository)
                .authenticationManager(authenticationManager)

                .authorizeExchange()
                .pathMatchers(whiteListConfig.getUrls()).permitAll()
                .pathMatchers("/a/test/a").hasAnyRole("user")
                .pathMatchers("/a/test/b").hasAnyAuthority("read")
                .pathMatchers("/a/api/**").hasAnyRole("enterprise")
                .pathMatchers("/a/java/**").hasAnyRole("java")
                .pathMatchers("/b/api/testRole").hasAnyRole("read")
                .pathMatchers("/b/api/testPermit").hasAnyAuthority("user")
                .pathMatchers("/b/api/testPermit1").hasAnyAuthority("enterprise")
                .pathMatchers("/b/api/testPermit2").hasAnyAuthority("read")
                // 如果使用authorizationManager 则弃用上面的 securityContextRepository authenticationManager
//                .anyExchange().access(authorizationManager)
                .anyExchange().authenticated()
                .and()
                .exceptionHandling()
                // 处理未授权
                .accessDeniedHandler(accessDeniedHandler())
                //处理未认证
                .authenticationEntryPoint(authenticationEntryPoint())
                .and().csrf().disable();
        return http.build();
    }

    /**
     * 未授权
     *
     * @return
     */
    @Bean
    ServerAccessDeniedHandler accessDeniedHandler() {
        return (exchange, denied) -> {
            Mono<Void> mono = Mono.defer(() -> Mono.just(exchange.getResponse()))
                    .flatMap(response -> WebUtils.writeFailedToResponse(response, ResultCode.ACCESS_UNAUTHORIZED));

            return mono;
        };
    }

    /**
     * token无效或者已过期自定义响应
     */
    @Bean
    ServerAuthenticationEntryPoint authenticationEntryPoint() {
        return (exchange, e) -> {
            Mono<Void> mono = Mono.defer(() -> Mono.just(exchange.getResponse()))
                    .flatMap(response -> WebUtils.writeFailedToResponse(response,ResultCode.TOKEN_INVALID_OR_EXPIRED));
            return mono;
        };
    }

    /**
     * @return
     * @link https://blog.csdn.net/qq_24230139/article/details/105091273
     * ServerHttpSecurity没有将jwt中authorities的负载部分当做Authentication
     * 需要把jwt的Claim中的authorities加入
     * 方案：重新定义R 权限管理器，默认转换器JwtGrantedAuthoritiesConverter
     * 该bean的作用是在获取了token以及里面的权限之后
     */
    @Bean
    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AuthConstants.AUTHORITY_EMPTY_PREFIX);
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(AuthConstants.JWT_AUTHORITIES_KEY);
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
