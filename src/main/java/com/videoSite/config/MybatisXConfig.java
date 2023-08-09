package com.videoSite.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import io.swagger.annotations.Api;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MybatisX 配置类
 **/
@Configuration
@EnableTransactionManagement
@MapperScan("com.videoSite.mapper")
@Api(tags = "MybatisX 配置", description = "MybatisX 配置类")
public class MybatisXConfig {

    /**
     * 配置分页插件
     * @return 分页插件实例
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor paginationInterceptor=new PaginationInterceptor();
        return paginationInterceptor;
    }
}
