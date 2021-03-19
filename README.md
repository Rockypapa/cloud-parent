### 框架使用步骤
前置条件-mysql数据库  
#### 搭建nacos 1.4.1 
1.配置nacos数据库创建nacos_config数据库  使用脚本创建表   参见document/nacos-mysql.sql
2.配置数据库脚本  参见document/application.properties

为了方便起见，在该项目中，nacos只作为注册中心使用


#### 搭建授权中心
1.需要必要的一张表 oauth_client_details 参见document/oauth_client_details.sql 

#### 