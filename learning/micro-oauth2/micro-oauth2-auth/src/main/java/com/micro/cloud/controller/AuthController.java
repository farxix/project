package com.micro.cloud.controller;

import com.micro.cloud.api.CommonResult;
import com.micro.cloud.domain.Oauth2TokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

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
}
