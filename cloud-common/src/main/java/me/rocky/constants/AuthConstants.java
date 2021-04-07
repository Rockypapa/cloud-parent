package me.rocky.constants;

public interface AuthConstants {


    /**
     * 认证请求头key
     */
    String AUTHORIZATION_KEY = "Authorization";

    /**
     * JWT令牌前缀
     */
    String JWT_PREFIX = "bearer ";


    /**
     * Basic认证前缀
     */
    String BASIC_PREFIX = "Basic ";

    /**
     * JWT载体key
     */
    String JWT_PAYLOAD_KEY = "payload";

    /**
     * Redis缓存权限规则key
     */
    String PERMISSION_ROLES_KEY = "auth:permission:roles";

    /**
     * 黑名单token前缀
     */
    String TOKEN_BLACKLIST_PREFIX = "auth:token:blacklist:";

    String CLIENT_DETAILS_FIELDS = "client_id, client_secret, resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information, autoapprove";

    String BASE_CLIENT_DETAILS_SQL = "select " + CLIENT_DETAILS_FIELDS + " from oauth_client_details";

    String FIND_CLIENT_DETAILS_SQL = BASE_CLIENT_DETAILS_SQL + " order by client_id";

    String SELECT_CLIENT_DETAILS_SQL = BASE_CLIENT_DETAILS_SQL + " where client_id = ?";

    String JWT_USER_ID_KEY = "user_id";

    String CLIENT_ID_KEY = "client_id";

    /**
     * JWT存储权限前缀
     */
    String AUTHORITY_PREFIX = "ROLE_";

    /**
     * JWT存储权限前缀
     */
    String AUTHORITY_EMPTY_PREFIX = "";

    /**
     * JWT存储权限属性
     */
    String JWT_AUTHORITIES_KEY = "authorities";


    /**
     * 有来商城后台管理客户端ID
     */
    String ADMIN_CLIENT_ID = "admin";


    /**
     * 有来商城微信小程序客户端ID
     */
    String WEAPP_CLIENT_ID = "weapp";

    /**
     * 后台管理接口路径匹配
     */
    String ADMIN_URL_PATTERN = "*_/b/**";
}
