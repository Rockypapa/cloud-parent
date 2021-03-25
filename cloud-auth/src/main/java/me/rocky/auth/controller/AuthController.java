package me.rocky.auth.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import me.chanjar.weixin.common.error.WxErrorException;

import me.rocky.auth.utils.RequestUtils;
import me.rocky.base.BaseController;
import me.rocky.common.exception.BusinessException;
import me.rocky.constants.AuthConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/oauth")
public class AuthController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private WxMaService wxMaService;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "grant_type", defaultValue = "password", value = "授权模式", required = true),
            @ApiImplicitParam(name = "client_id", defaultValue = "client", value = "Oauth2客户端ID", required = true),
            @ApiImplicitParam(name = "client_secret", defaultValue = "123456", value = "Oauth2客户端秘钥", required = true),
            @ApiImplicitParam(name = "refresh_token", value = "刷新token"),
            @ApiImplicitParam(name = "username", defaultValue = "admin", value = "登录用户名"),
            @ApiImplicitParam(name = "password", defaultValue = "123456", value = "登录密码"),
            // 微信小程序认证参数（无小程序可忽略）
            @ApiImplicitParam(name = "code", value = "小程序code"),
            @ApiImplicitParam(name = "encryptedData", value = "包括敏感数据在内的完整用户信息的加密数据"),
            @ApiImplicitParam(name = "iv", value = "加密算法的初始向量"),
    })
    @PostMapping("/token")
    public OAuth2AccessToken postAccessToken(@ApiIgnore Principal principal, @ApiIgnore @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException, BusinessException {
        OAuth2AccessToken oAuth2AccessToken;
        /**
         * 获取登录认证的客户端ID
         *
         * 兼容两种方式获取Oauth2客户端信息（client_id、client_secret）
         * 方式一：client_id、client_secret放在请求路径中
         * 方式二：放在请求头（Request Headers）中的Authorization字段，且经过加密，例如 Basic Y2xpZW50OnNlY3JldA== 明文等于 client:secret
         */

        String  clientId = RequestUtils.getAuthClientId();
        switch (clientId) {
            // 微信认证
            case AuthConstants.WEAPP_CLIENT_ID:
                oAuth2AccessToken = this.handleForWxAuth(principal, parameters);
                break;
            default:
                oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
                break;
        }
        return oAuth2AccessToken;
    }




    public OAuth2AccessToken handleForWxAuth(Principal principal, Map<String, String> parameters) throws HttpRequestMethodNotSupportedException, BusinessException {

        String code = parameters.get("code");

        if (StringUtils.isEmpty(code)) {
            throw new BusinessException("code不能为空");
        }

        WxMaJscode2SessionResult session = null;
        try {
            session = wxMaService.getUserService().getSessionInfo(code);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        String openid = session.getOpenid();
        String sessionKey = session.getSessionKey();

        //远程调用查询出用户信息,
        //如果为空则解析用户信息，添加用户信息，解析失败则报错


        // oauth2认证参数对应授权登录时注册会员的username、password信息，模拟通过oauth2的密码模式认证
        parameters.put("username", openid);
        parameters.put("password", openid);

        return tokenEndpoint.postAccessToken(principal, parameters).getBody();
    }


}
