package com.micro.cloud.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordEncodeUtil {

    private static final BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

    public static String bcryptEncode(String password) {
        return bcryptEncoder.encode(password);
    }

    public static String genOauthEncodePwd(String password) {
        return "{bcrypt}" + bcryptEncode(password);
    }


    public static void main(String[] args) {
//        String oriPwd = "123456";
//        System.out.println(genOauthEncodePwd(oriPwd));

        String oriPwd = "123456";
        System.out.println(PasswordEncodeUtil.bcryptEncode(oriPwd));
        System.out.println(PasswordEncodeUtil.genOauthEncodePwd(oriPwd));

    }
}