# client 相关信息
# https://andaily.com/spring-oauth-server/db_table_description.html
create table oauth_client_details
(
    client_id               VARCHAR(256) PRIMARY KEY comment '必填，Oauth2 client_id',
    resource_ids            VARCHAR(256) comment '可选，资源id集合，多个资源用英文逗号隔开',
    client_secret           VARCHAR(256) comment '必填，Oauth2 client_secret',
    scope                   VARCHAR(256) comment '必填，Oauth2 权限范围，比如 read，write等可自定义',
    authorized_grant_types  VARCHAR(256) comment '必填，Oauth2 授权类型，支持类型：authorization_code,password,refresh_token,implicit,client_credentials，多个用英文逗号隔开',
    web_server_redirect_uri VARCHAR(256) comment '可选，客户端的重定向URI,当grant_type为authorization_code或implicit时,此字段是需要的',
    authorities             VARCHAR(256) comment '可选，指定客户端所拥有的Spring Security的权限值',
    access_token_validity   INTEGER comment '可选，access_token的有效时间值(单位:秒)，不填写框架(类refreshTokenValiditySeconds)默认12小时',
    refresh_token_validity  INTEGER comment '可选，refresh_token的有效时间值(单位:秒)，不填写框架(类refreshTokenValiditySeconds)默认30天',
    additional_information  VARCHAR(4096) comment '预留字段，格式必须是json',
    autoapprove             VARCHAR(256) comment '该字段适用于grant_type="authorization_code"的情况下，用户是否自动approve操作'
);

# token 存储
create table oauth_access_token
(
    token_id          VARCHAR(256) comment 'MD5加密后存储的access_token',
    token             BLOB comment 'access_token序列化的二进制数据格式',
    authentication_id VARCHAR(256) PRIMARY KEY comment '主键，其值是根据当前的username(如果有),client_id与scope通过MD5加密生成的,具体实现参见DefaultAuthenticationKeyGenerator',
    user_name         VARCHAR(256),
    client_id         VARCHAR(256),
    authentication    BLOB comment '将OAuth2Authentication对象序列化后的二进制数据',
    refresh_token     VARCHAR(256) comment 'refresh_token的MD5加密后的数据'
);