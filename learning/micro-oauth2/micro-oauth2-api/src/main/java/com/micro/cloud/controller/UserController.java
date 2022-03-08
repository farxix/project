package com.micro.cloud.controller;

import com.micro.cloud.domain.UserDTO;
import com.micro.cloud.holder.LoginUserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 获取登录用户信息接口
 * Created by farxix on 2022/3/4.
 */
@RestController
@RequestMapping("/user")
public class UserController{

    @Autowired
    private LoginUserHolder loginUserHolder;

    @GetMapping("/currentUser")
    public UserDTO currentUser() {
        return loginUserHolder.getCurrentUser();
    }

    /**
     * 用户退出登录
     */
    @GetMapping("/logout")
    public String logout() {
        return "退出成功";
//        UserDTO userDTO = loginUserHolder.getCurrentUser();
//
//        String authHeader = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhbmR5Iiwic2NvcGUiOlsiYWxsIl0sImlkIjoyLCJleHAiOjE2NDY2NDY2MTEsImF1dGhvcml0aWVzIjpbIlRFU1QiXSwianRpIjoiMWZmMGUyY2EtOTE2NS00MGU2LWIzNzgtZTUyOGVmMzZhYWQ4IiwiY2xpZW50X2lkIjoiY2xpZW50LWFwcCJ9.GUEeyUhjCsHyHejIshcrTB0Yqe29US64sOU2dCra92JjKp_OdTJ2jwXmYT6TN5vG2QawBPfM0l7LZc4nugUnNC256u9qjebfFvjiOuE4HVVZIJMvFKcahXpiTtezE8UdgHQamP3bLZjoeouB74epw0ipSXg7sN2U_kv1DxSfH5jpw5bfQfvMz_kTYfP17Ky26Shpovha8D04wB-L1BBOk5vgsSNpZM7aUYtiPMx8KUZDd6P5iJJ3ERhFsmMuzX1vy-k4f2MVgZvsJmZbvpGX0fa03ST2lAPiUwkSWZwHwGlfp4Uf4-kkWVTGnDNo9oEhoZAQwy0IlMnMhShKU5drog";
//
//        //获取token，去除前缀
//        String token = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, "").trim();
//
//        // 解析Token
//        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
//
//        //token 已过期
//        if (oAuth2AccessToken.isExpired()) {
//            return CommonResult.failed(ResultCode.INVALID_TOKEN_OR_EXPIRED, ResultCode.INVALID_TOKEN_OR_EXPIRED.getMessage());
//        }
//
//        if (StringUtils.isBlank(oAuth2AccessToken.getValue())) {
//            //访问令牌不合法
//            return CommonResult.failed(ResultCode.INVALID_TOKEN_OR_EXPIRED, ResultCode.INVALID_TOKEN_OR_EXPIRED.getMessage());
//        }
//
//        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(oAuth2AccessToken);
//        String userName = userDTO.getUsername();
//
//        //获取token唯一标识
//        String jti = userDTO.getJti();
//
//        //获取过期时间
//        Long expiration = userDTO.getExpireTime();
//        long exp = expiration / 1000;
//
//        long currentTimeSeconds = System.currentTimeMillis() / 1000;
//
//        //设置token过期时间
//        redisTemplate.opsForValue().set(RedisConstant.TOKEN_BLACKLIST_PREFIX + jti, userName, (exp - currentTimeSeconds), TimeUnit.SECONDS);
//        return CommonResult.success("退出成功");
    }

}
