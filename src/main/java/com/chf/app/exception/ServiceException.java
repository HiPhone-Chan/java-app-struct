package com.chf.app.exception;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public ServiceException(String code) {
        this(code, code);
    }

    public String getCode() {
        return code;
    }

}
