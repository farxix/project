package com.micro.cloud.controller;

import com.micro.cloud.api.CommonResult;
import com.micro.cloud.api.ResultCode;
import com.micro.cloud.constant.RedisConstant;
import com.micro.cloud.domain.Oauth2TokenDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 自定义Oauth2获取令牌接口
 * Created by farxix on 2022/3/4.
 **/
@RestController
@RequestMapping("/oauth")
public class AuthController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    /**
     * Oauth2登录认证
     */
    @PostMapping("/token")
    public CommonResult<Oauth2TokenDto> postAccessToken(Principal principal, @RequestParam Map<String,String> params) throws HttpRequestMethodNotSupportedException {
        OAuth2AccessToken accessToken = tokenEndpoint.postAccessToken(principal,params).getBody();
        Oauth2TokenDto tokenDto = Oauth2TokenDto.builder()
                .token(accessToken.getValue())
                .refreshToken(accessToken.getRefreshToken().getValue())
                .expiresIn(accessToken.getExpiresIn())
                .tokenHead("Bearer").build();
        return CommonResult.success(tokenDto);

    }

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 用户退出登录
     * @param authHeader 从请求头获取token
     */
    @DeleteMapping("/logout")
    public CommonResult<String> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader){

        //获取token，去除前缀
        String token = authHeader.replace(OAuth2AccessToken.BEARER_TYPE,"").trim();

        // 解析Token
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);

        //token 已过期
        if(oAuth2AccessToken.isExpired()){
            return CommonResult.failed(ResultCode.INVALID_TOKEN_OR_EXPIRED,ResultCode.INVALID_TOKEN_OR_EXPIRED.getMessage());
        }

        if(StringUtils.isBlank(oAuth2AccessToken.getValue())){
            //访问令牌不合法
            return CommonResult.failed(ResultCode.INVALID_TOKEN_OR_EXPIRED,ResultCode.INVALID_TOKEN_OR_EXPIRED.getMessage());
        }

        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(oAuth2AccessToken);
        String userName = oAuth2Authentication.getName();

        //获取token唯一标识
        String jti = (String) oAuth2AccessToken.getAdditionalInformation().get("jti");

        //获取过期时间
        Date expiration = oAuth2AccessToken.getExpiration();
        long exp = expiration.getTime() / 1000;

        long currentTimeSeconds = System.currentTimeMillis() / 1000;

        //设置token过期时间
        redisTemplate.opsForValue().set(RedisConstant.TOKEN_BLACKLIST_PREFIX + jti, userName, (exp - currentTimeSeconds), TimeUnit.SECONDS);
        return CommonResult.success("退出成功");
    }
}
