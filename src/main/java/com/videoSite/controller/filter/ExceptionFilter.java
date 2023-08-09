package com.videoSite.controller.filter;

import com.videoSite.anno.MyFilterOrder;
import com.videoSite.controller.exception.NotExistInRedisException;
import com.videoSite.controller.exception.ThreadLocalIsNullException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

/**
 * 异常拦截过滤器，用于捕获特定异常并进行处理转发。
 * 当 ThreadLocal 为空或 Redis 中不存在相关信息时，捕获并转发到对应的错误处理控制器。
 */
@Slf4j
@MyFilterOrder(-100)
public class ExceptionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("异常拦截器启用");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (ThreadLocalIsNullException e) {
            //异常捕获，发送到error controller
            servletRequest.setAttribute("filter.error.ThreadLocalIsNullException", e);
            //将异常分发到/error/reThrow控制器
            servletRequest.getRequestDispatcher("/error/throwThreadLocalIsNullException").forward(servletRequest, servletResponse);
        } catch (NotExistInRedisException e) {
            servletRequest.setAttribute("filter.error.NotExistInRedisException", e);
            servletRequest.getRequestDispatcher("/error/throwNotExistInRedisException").forward(servletRequest, servletResponse);
        }catch (NullPointerException e) {
                servletRequest.setAttribute("filter.error.NullPointerException", e);
                servletRequest.getRequestDispatcher("/error/throwNullPointerException").forward(servletRequest, servletResponse);
        } catch (Exception e) {
            servletRequest.setAttribute("filter.error.Exception", e);
            servletRequest.getRequestDispatcher("/error/throwException").forward(servletRequest, servletResponse);

        }
    }


}
