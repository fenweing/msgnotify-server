package com.tuanbaol.messageserver.exception;


import com.tuanbaol.messageserver.util.StringUtil;
import org.springframework.http.HttpStatus;

/**
 * @version 1.0
 * @description 服务器异常类
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private int code;
    private String message;

    public ServiceException(String message) {
        super(message);
        this.message = message;
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public ServiceException(Throwable throwable) {
        super(throwable);
    }

    public ServiceException(Throwable throwable, String template, Object... args) {
        super(throwable);
        this.message = StringUtil.formatByRegex(template, args);
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public ServiceException(String template, Object... args) {
        this.message = StringUtil.formatByRegex(template, args);
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public ServiceException(String message, Throwable throwable) {
        super(message, throwable);
        this.message = message;
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

