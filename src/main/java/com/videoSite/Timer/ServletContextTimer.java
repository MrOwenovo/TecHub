package com.videoSite.Timer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.util.HashMap;

/**
 * 检查ServletContextTimer中的属性是否过期
 */
@Slf4j
@Component
public class ServletContextTimer implements ServletContextAware {

    private static ServletContext servletContext;
    @Resource
    RedisTemplate<Object,Object> redisTemplate;



    @Override
    public void setServletContext(ServletContext servletContext) {
        ServletContextTimer.servletContext=servletContext;
    }
}
