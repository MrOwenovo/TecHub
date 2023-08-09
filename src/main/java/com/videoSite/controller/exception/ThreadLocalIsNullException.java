package com.videoSite.controller.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "从ThreadLocal中取出为空时抛出的异常")
public class ThreadLocalIsNullException extends RuntimeException {

    @ApiModelProperty(value = "异常消息", example = "ThreadLocal中的值为空")
    private String message;

    public ThreadLocalIsNullException() {
        super("");
        this.message = "ThreadLocal中的值为空";
    }

    public ThreadLocalIsNullException(String message) {
        super(message);
        this.message = message;
    }

    public ThreadLocalIsNullException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public ThreadLocalIsNullException(Throwable cause) {
        super(cause);
        this.message = "ThreadLocal中的值为空";
    }

    public String getMessage() {
        return message;
    }
}
