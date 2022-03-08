package com.micro.cloud.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.micro.cloud.api.CommonResult;
import com.micro.cloud.api.ResultCode;
import com.micro.cloud.constant.RedisConstant;
import com.nimbusds.jose.JWSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;

/**
 * 将登录用户的JWT转化成用户信息的全局过滤器
 * Created by farxix on 2022/3/4.
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private static Logger LOGGER = LoggerFactory.getLogger(AuthGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StrUtil.isEmpty(token)) {
            return chain.filter(exchange);
        }
        try {
            // 是否在黑名单
            if (isBlack(token)) {
                return Result(exchange);
            }
            //从token中解析用户信息并设置到Header中去
            String realToken = token.replace("Bearer ", "");
            JWSObject jwsObject = JWSObject.parse(realToken);
            String userStr = jwsObject.getPayload().toString();
            LOGGER.info("AuthGlobalFilter.filter() user:{}", userStr);

            ServerHttpRequest request = exchange.getRequest().mutate().header("user", userStr).build();
            exchange = exchange.mutate().request(request).build();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return chain.filter(exchange);
    }

    public Mono<Void> Result(ServerWebExchange exchange){
        ServerHttpResponse response = exchange.getResponse();
        byte[] bits = JSONUtil.toJsonStr(CommonResult.failed(ResultCode.TOKEN_EXPIRE, ResultCode.TOKEN_EXPIRE.getMessage())).getBytes();
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.OK);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 通过redis判断token是否为黑名单
     *
     * @param headerToken 请求头
     * @return boolean
     */
    private boolean isBlack(String headerToken) throws ParseException {
        //todo  移除所有oauth2相关代码，暂时使用 OAuth2AccessToken.BEARER_TYPE 代替
        String token = headerToken.replace("Bearer ", StrUtil.EMPTY).trim();

        //解析token
        JWSObject jwsObject = JWSObject.parse(token);
        String payload = jwsObject.getPayload().toString();
        JSONObject jsonObject = JSONUtil.parseObj(payload);

        // JWT唯一标识
        String jti = jsonObject.getStr("jti");
        System.out.println(RedisConstant.TOKEN_BLACKLIST_PREFIX + jti);
        return redisTemplate.hasKey(RedisConstant.TOKEN_BLACKLIST_PREFIX + jti);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
