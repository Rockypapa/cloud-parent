package me.rocky.auth.utils;


import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import me.rocky.constants.AuthConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;


public class RequestUtils {


    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    public static JSONObject getJwtPayload() {
        String jwtPayload = getRequest().getHeader(AuthConstants.JWT_PAYLOAD_KEY);
        JSONObject jsonObject = JSON.parseObject(jwtPayload);
        return jsonObject;
    }

    public static Long getUserId() {
        Long id = getJwtPayload().getLong(AuthConstants.JWT_USER_ID_KEY);
        return id;
    }

    /**
     * 获取JWT的载体中的clientId
     *
     * @return
     */
    public static String getClientId() {
        String clientId = getJwtPayload().getString(AuthConstants.CLIENT_ID_KEY);
        return clientId;
    }

    /**
     * 获取登录认证的客户端ID
     * <p>
     * 兼容两种方式获取Oauth2客户端信息（client_id、client_secret）
     * 方式一：client_id、client_secret放在请求路径中
     * 方式二：放在请求头（Request Headers）中的Authorization字段，且经过加密，例如 Basic Y2xpZW50OnNlY3JldA== 明文等于 client:secret
     *
     * @return
     */

    public static String getAuthClientId() {
        String clientId;

        HttpServletRequest request = getRequest();

        // 从请求路径中获取
        clientId = request.getParameter(AuthConstants.CLIENT_ID_KEY);
        if (StringUtils.isNotBlank(clientId)) {
            return clientId;
        }

        // 从请求头获取
        String basic = request.getHeader(AuthConstants.AUTHORIZATION_KEY);
        basic = request.getHeader(AuthConstants.AUTHORIZATION_KEY);
        if (StringUtils.isNotBlank(basic) && basic.startsWith(AuthConstants.BASIC_PREFIX)) {
            basic = basic.replace(AuthConstants.BASIC_PREFIX, Strings.EMPTY);
            String basicPlainText = null;
            basicPlainText = new String(Base64.decode(basic), StandardCharsets.UTF_8);
            //client:secret
            clientId = basicPlainText.split(":")[0];
        }
        return clientId;
    }


    public static List<Long> getRoleIds() {
        JSONArray jsonArray = getJwtPayload().getJSONArray(AuthConstants.JWT_AUTHORITIES_KEY);
        List<String> strings = jsonArray.toJavaList(String.class);
        List<Long> authorities = strings.stream().map(Long::valueOf).collect(Collectors.toList());
        return authorities;
    }
}
