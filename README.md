### 框架使用流程
1.通过网关访问授权中心登录接口 携带信息
```asp
method: 'post'
header: 'Authorization': 'Basic d2VhcHA6MTIzNDU2'  //内容为  "Basic "开头的  "weapp:123456" 的base64编码字符串  weapp是clientid
,123456是客户端密码
params: {  微信登录接口数据
    iv: xxx,
    code: xxx,
    encryptedData: xxx
    grant_type: password
}
地址为  http://{网关:端口}/{授权中心前缀}/oauth/token
```
2.请求通过网关的过滤器判断该请求放行，于是将请求路由到认证中心
3.认证中心首先回拿到header里面的Authorization 解析出clientid以及密码，校验时会根据工程的PasswordEncoder来校验clientid与密码，比如BcryptPasswordEncoder
加密的"123456"是"$2a$10$r9nDINxC8UfTxYooMGblruFkvLEzVQPmh.Wiv5CGzXfX8qbYAepgC" 校验成功过后会校验该条数据到认证方式"authorization_code
,password,refresh_token,implicit"  数据 参见document/oauth_client_details.sql 
4.client验证成功过后,方法将执行到/oauth/token的controller,对于微信登录我们将其账号密码都设置为openid,方法路由到UserDetailsService的loadByUsername,username
为openid,通过获取该servlet请求中的clientid加密数判断调用哪个服务的用户信息。
5.按照校验client_id的思路对远程获取的用户信息进行校验   注意: 表中密码加密方式与用户密码加密方式需要一样!
6.校验成功后成功获取token，将返回的token写回到前端前端可以访问符合条件的接口
7.网关将从token中解析出来的username等信息封装到请求中，路由到下游服务时，可以直接获取此类信息
8.如果不涉及用户信息的接口，服务之间可以通过feign直接调用
### 框架使用步骤
前置条件-mysql数据库  
#### 搭建nacos 1.4.1 
1.配置nacos数据库创建nacos_config数据库  使用脚本创建表   参见document/nacos-mysql.sql
2.配置数据库脚本  参见document/application.properties

为了方便起见，在该项目中，nacos只作为注册中心使用
#### 搭建授权中心
1.需要必要的一张表 oauth_client_details 参见document/oauth_client_details.sql 
2.需要生成一个jks密钥,该密钥在访问时需要
cmd:  keytool -genkey -alias [密钥别名] -keyalg [密钥算法] -keypass [密钥密码] -keystore [文件名] -storepass [密钥库口令]
例: keytool -genkey -alias youlai -keyalg RSA -keypass 123456 -keystore youlai.jks -storepass 123456
     
#### 搭建网关
1.设置路由规则 参考 cloud-gateway\src\main\resources\application.yml
2.设置过滤规则 参考 cloud-gateway\src\main\java\me\rocky\gateway\filter\AuthGlobalFilter.java
3.权限获取规则 获取token中的权限信息，参考 cloud-gateway\src\main\java\me\rocky\gateway\security\SecurityContextRepository.java
4.配置资源服务以及权限放行规则  参考cloud-gateway\src\main\java\me\rocky\gateway\config\ResourceServerConfig
