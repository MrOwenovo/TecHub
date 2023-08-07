package com.videoSite.controller.filter;

import com.videoSite.common.constant.ThreadDetails;
import com.videoSite.utils.IpTools;
import com.videoSite.utils.RedisTools;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;



@Slf4j
//@MyFilterOrder(value = 100,urlPatterns ={"/api/game/*", "/api/chat/*"} )
public class GameFilter implements Filter {

    private static RedisTools<String> redisTools;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("游戏线程拦截器启用");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (!request.getServletPath().startsWith("/api/game/changePassNumber")) {


            filterChain.doFilter(servletRequest, servletResponse);

        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    public static void setRedisTools(RedisTools<String> redisTools) {
        GameFilter.redisTools = redisTools;
    }
}
