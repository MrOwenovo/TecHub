package com.videoSite.config;

import com.videoSite.common.repo.RestBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Configuration
@Slf4j
@Api(tags = "Redis Mybatis 缓存配置", description = "配置 Mybatis 缓存存储到 Redis")
public class RedisMybatisCacheConfiguration {

    @Resource
    RedisTemplate<Object, Object> redisTemplate;

    /**
     * 初始化方法，在容器加载时设置 RedisTemplate 到 RedisMybatisCache
     */
    @PostConstruct
    @ApiOperation("初始化 Mybatis 缓存配置")
    public void init() {
        log.info("存储 Mybatis 缓存的 Redis 配置加载");
        RedisMybatisCache.setTemplate(redisTemplate);
    }
}
