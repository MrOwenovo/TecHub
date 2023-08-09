package com.videoSite.controller.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "数据库中未查到相关信息的异常")
public class NotExistInRedisException extends RuntimeException {

    @ApiModelProperty(value = "异常消息", example = "数据未在 Redis 中找到")
    private String message;

    public NotExistInRedisException() {
        super("");
        this.message = "数据未在 Redis 中找到";
    }

    public NotExistInRedisException(String message) {
        super(message);
        this.message = message;
    }

    public NotExistInRedisException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public NotExistInRedisException(Throwable cause) {
        super(cause);
        this.message = "数据未在 Redis 中找到";
    }

    public String getMessage() {
        return message;
    }
}
