package com.videoSite.controller.filter;

import com.videoSite.anno.MyFilterOrder;
import com.videoSite.common.constant.ThreadDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@MyFilterOrder(value=-50)
public class ThreadInitFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("线程初始化拦截器启用");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //初始化SecurityContext
        SecurityContext securityContext = SecurityContextHolder.getContext();
        ThreadDetails.securityContext.set(securityContext);


        //初始化ServletContext
        ServletContext servletContext = request.getServletContext();
        ThreadDetails.servletContext.set(servletContext);

        filterChain.doFilter(servletRequest,servletResponse);

    }

    @Override
    public void destroy() {
        ThreadDetails.servletContext.remove();
        ThreadDetails.securityContext.remove();
    }
}
