package com.videoSite.controller.filter;

import com.videoSite.common.repo.RestBean;
import com.videoSite.common.repo.RestBeanBuilder;
import com.videoSite.common.repo.ResultCode;
import com.videoSite.controller.exception.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.PostConstruct;

/**
 * 全局异常处理
 */

// 添加@Api注解来描述全局异常处理器
@Api(tags = "全局异常处理器")
@Slf4j
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class OverallExceptionHandler {

    @PostConstruct
    private void init(){
        log.info("全局异常拦截器启用");
    }

    @ApiOperation("处理通用异常")
    @ExceptionHandler
    public RestBean<Object> handleException(Exception e) {
        e.printStackTrace();
        return RestBeanBuilder.builder().code(ResultCode.FAILURE).messageType(RestBeanBuilder.USER_DEFINED).message("发生了未知的错误!请联系管理员!").build().ToRestBean();
    }



    @ApiOperation("处理 NotExistInRedisException 异常")
    @ExceptionHandler(NotExistInRedisException.class)
    public RestBean<Object> handleNotExistInRedisException(NotExistInRedisException e) {
        if ("".equals(e.getMessage()))
            return RestBeanBuilder.builder().code(ResultCode.NOT_EXIST).build().ToRestBean();
        return RestBeanBuilder.builder().code(ResultCode.NOT_EXIST).messageType(RestBeanBuilder.USER_DEFINED).message(e.getMessage()).build().ToRestBean();

    }

    @ApiOperation("处理 ThreadLocalIsNullException 异常")
    @ExceptionHandler(ThreadLocalIsNullException.class)
    public RestBean<Object> handleThreadLocalIsNullException(ThreadLocalIsNullException e) {
        if ("".equals(e.getMessage()))
            return RestBeanBuilder.builder().code(ResultCode.THREAD_LOCAL_IS_NULL).build().ToRestBean();
        return RestBeanBuilder.builder().code(ResultCode.THREAD_LOCAL_IS_NULL).messageType(RestBeanBuilder.USER_DEFINED).message(e.getMessage()).build().ToRestBean();

    }




}
