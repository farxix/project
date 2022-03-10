package com.example.demo.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

public class SwaggerConfig {

//    @Value("${spring.profiles.active:NA}")
    private String active;

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.OAS_30)
//                .enable("dev".equals(active)) // 仅在开发环境开启Swagger
                .apiInfo(apiInfo())
                .host("http://localhost:8080/swagger-ui/") // Base URL
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo.swagger"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API文档")
                .description("这是描述信息")
                .contact(new Contact("farxix", null, null))
                .version("1.0")
                .build();
    }

}
