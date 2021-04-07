package me.rocky.gateway.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nimbusds.jose.JWSObject;
import me.rocky.common.result.ResultCode;
import me.rocky.constants.AuthConstants;
import me.rocky.gateway.util.WebUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;

/**
 * 全局过滤器
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(AuthGlobalFilter.class);


//    @Autowired
//    private RedisTemplate<String,Object> redisTemplate;

    // 是否演示环境
    @Value("${demo:false}")
    private Boolean isDemoEnvironment;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 演示环境禁止删除和修改
        if (isDemoEnvironment && HttpMethod.DELETE.toString().equals(request.getMethodValue())) {
            log.warn(ResultCode.FORBIDDEN_OPERATION.getMessage());
            return WebUtils.writeFailedToResponse(response, ResultCode.FORBIDDEN_OPERATION);
        }

        // 无token放行
        String token = request.getHeaders().getFirst(AuthConstants.AUTHORIZATION_KEY);
        if (StrUtil.isBlank(token) || !token.startsWith(AuthConstants.JWT_PREFIX)) {
            return chain.filter(exchange);
        }

        // 解析JWT获取jti，以jti为key判断redis的黑名单列表是否存在，存在拦截响应token失效
        token = token.replace(AuthConstants.JWT_PREFIX, Strings.EMPTY);
        JWSObject jwsObject = null;
        try {
            jwsObject = JWSObject.parse(token);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String payload = jwsObject.getPayload().toString();
        JSONObject jsonObject = JSONUtil.parseObj(payload);
//        String jti = jsonObject.getStr(AuthConstants.CLIENT_ID_KEY);
//        Boolean isBlack = redisTemplate.hasKey(AuthConstants.TOKEN_BLACKLIST_PREFIX + jti);
//        if (isBlack) {
//            return WebUtils.writeFailedToResponse(response, ResultCode.TOKEN_INVALID_OR_EXPIRED);
//        }
        // 存在token且不是黑名单，request写入JWT的载体信息

        // 将token封装成x-username转发
        request = exchange.getRequest().mutate()
                .header("x-username", jsonObject.getStr("user_name"))
                .build();
        exchange = exchange.mutate().request(request).build();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
