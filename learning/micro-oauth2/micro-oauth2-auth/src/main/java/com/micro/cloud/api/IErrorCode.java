package com.micro.cloud.api;

/**
 * 封装API的错误码
 * Created by farxix on 2022/3/4.
 */
public interface IErrorCode {
    long getCode();

    String getMessage();
}
