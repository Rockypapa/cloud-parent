package me.rocky.gateway.security;

import cn.hutool.core.convert.Convert;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import me.rocky.constants.AuthConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import java.security.PublicKey;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 鉴权管理器
 * AuthorizationManager 和 AuthenticationManager 二选一，我使用的是AuthenticationManager，直接在ResourceServerConfig上配置路径所需权限
 * AuthorizationManager可配合数据库与缓存对接口进行动态鉴权，数据如pathPermissionMap
 */
@Component
public class AuthorizationManager implements   ReactiveAuthorizationManager<AuthorizationContext> {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationManager.class);

    private final Map<String,Object> pathPermissionMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("GET_/a/test/a",Arrays.asList("ROLE_user"));
        map.put("GET_/a/test/b",Arrays.asList("read"));
        map.put("GET_/a/api/**",Arrays.asList("enterprise"));
        map.put("GET_/a/java/**",Arrays.asList("ROLE_java"));
        map.put("GET_/b/api/testRole",Arrays.asList("read"));
        map.put("GET_/b/api/testRole1",Arrays.asList("ROLE_enterprise"));
        map.put("GET_/b/api/testPermit",Arrays.asList("read"));
        return map;
    }


    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {


        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        String path = request.getMethodValue() + "_" + request.getURI().getPath();
        log.info("请求，path={}", path);
        PathMatcher pathMatcher = new AntPathMatcher();
        // 对应跨域的预检请求直接放行
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return Mono.just(new AuthorizationDecision(true));
        }

        // 非管理端路径无需鉴权直接放行
        if (!pathMatcher.match(AuthConstants.ADMIN_URL_PATTERN, path)) {
            log.info("请求无需鉴权，path={}", path);
            return Mono.just(new AuthorizationDecision(true));
        }


        // token为空拒绝访问
        String token = request.getHeaders().getFirst(AuthConstants.AUTHORIZATION_KEY);
        if (StringUtils.isEmpty(token)) {
            log.info("请求token为空拒绝访问，path={}", path);
            return Mono.just(new AuthorizationDecision(false));
        }


        // 从缓存取资源权限角色关系列表
        Map<String, Object> permissionRoles = pathPermissionMap();
        // .PERMISSION_ROLES_KEY);
        Iterator<String> iterator = permissionRoles.keySet().iterator();
        // 请求路径匹配到的资源需要的角色权限集合authorities统计
        Set<String> authorities = new HashSet<>();
        while (iterator.hasNext()) {
            String pattern = iterator.next();
            if (pathMatcher.match(pattern, path)) {
                authorities.addAll(Convert.toList(String.class, permissionRoles.get(pattern)));
            }
        }
        log.info("require authorities:{}", authorities);

        Mono<AuthorizationDecision> authorizationDecisionMono = mono
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(roleId -> {
                    // roleId是请求用户的角色(格式:ROLE_{roleId})，authorities是请求资源所需要角色的集合
                    log.info("访问路径：{}", path);
                    log.info("用户角色信息：{}", roleId);
                    log.info("资源需要权限authorities：{}", authorities);
                    return authorities.contains(roleId);
                })
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
        return authorizationDecisionMono;
    }


}
