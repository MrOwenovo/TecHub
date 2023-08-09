package com.videoSite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    // 全局请求参数，用于身份验证等场景
    @Bean
    public Docket api() {
        List<Parameter> globalParameters = Collections.singletonList(
                new ParameterBuilder()
                        .name("Authorization")
                        .description("Bearer token")
                        .modelRef(new ModelRef("string"))
                        .parameterType("header")
                        .required(false)
                        .build()
        );

        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .globalOperationParameters(globalParameters) // 将全局参数添加到每个接口
                .securitySchemes(Collections.singletonList(apiKey())) // 添加身份验证安全方案
                .securityContexts(Collections.singletonList(securityContext())) // 添加安全上下文
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.videoSite.controller")) // 扫描指定包下的接口
                .paths(PathSelectors.any()) // 可以根据路径规则进行过滤
                .build();


    }

    // API 文档信息
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("TecHub 教学视频网站 API")
                .description("API 文档详细说明")
                .version("1.4")
                .contact(new Contact("MrOwenovo", "https://github.com/MrOwenovo?tab=repositories", "lmq122677@qq.com"))
                .build();
    }

    // 安全上下文，定义哪些接口需要身份验证
    private SecurityContext securityContext() {
        return springfox.documentation.spi.service.contexts.SecurityContext.builder()
                .securityReferences(Collections.singletonList(new SecurityReference("Bearer",
                        new AuthorizationScope[]{new AuthorizationScope("global", "accessEverything")})))
                .forPaths(PathSelectors.any()) // 可以根据路径规则进行过滤
                .build();
    }

    // 身份验证安全方案
    private ApiKey apiKey() {
        return new ApiKey(
                "Authorization",
                "Bearer token",
                "header"
        );
    }

}
