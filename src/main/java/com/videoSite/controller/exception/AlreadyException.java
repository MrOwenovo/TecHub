package com.videoSite.controller.exception;

/**
 * 数据库中已经存在该信息的异常
 */
public class AlreadyException extends RuntimeException{

    public AlreadyException() {
        super("");
    }

    public AlreadyException(String message) {
        super(message);
    }

    public AlreadyException(String message,Throwable cause) {
        super(message,cause);
    }

    public AlreadyException(Throwable cause) {
        super(cause);
    }

}
