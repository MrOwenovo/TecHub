package com.videoSite.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC 配置类
 **/
@Configuration
@Api(tags = "MVC 配置", description = "MVC 配置类")
public class MyMvcConfig implements WebMvcConfigurer {

    // 配置视图控制器
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/register.html").setViewName("register");
        registry.addViewController("/post.html").setViewName("video/post");
    }
}
