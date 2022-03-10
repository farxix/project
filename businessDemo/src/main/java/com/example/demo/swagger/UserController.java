package com.example.demo.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Tag(name = "user-controller", description = "用户接口")
@RestController
public class UserController {
   // 忽略这个api
   @ApiIgnore
   @GetMapping("/hello")
   public String hello(){
       return "hello";
   }
  
   @Operation(summary = "用户接口 - 获取用户详情")
   @GetMapping("/user/detail")
   // 这里的@Parameter也可以不加，Swagger会自动识别到这个name参数
   // 但是加@Parameter注解可以增加一些描述等有用的信息
   public User getUser(@Parameter(in = ParameterIn.QUERY, name = "name", description = "用户名") String name){
       User user = new User();
       user.setUsername(name);
       user.setPassword("123");
       return user;
   }
  
   @Operation(summary = "用户接口 - 添加用户")
   @PostMapping("/user/add")
   // 这里的user会被Swagger自动识别
   public User addUser(@RequestBody User user){
       System.out.println("添加用户");
       return user;
   }
}
