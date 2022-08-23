package com.cat.common.exception.extend;


import com.cat.common.exception.enums.ErrorCodeEnum;

public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String errorCode;

    public String getError() {
        return errorCode;
    }

    public NotFoundException(String msg) {
        super(msg);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getDescription());
        this.errorCode = errorCodeEnum.getCode();
    }

    public NotFoundException(ErrorCodeEnum errorCodeEnum, String... values) {
        super(String.format(errorCodeEnum.getDescription(), values));
        this.errorCode = errorCodeEnum.getCode();
    }

    public NotFoundException(ErrorCodeEnum errorCodeEnum, Throwable cause) {

        super(errorCodeEnum.getDescription(), cause);
        this.errorCode = errorCodeEnum.getCode();
    }

    public NotFoundException(ErrorCodeEnum errorCodeEnum, Throwable cause, String... values) {

        super(String.format(errorCodeEnum.getDescription(), values), cause);
        this.errorCode = errorCodeEnum.getCode();
    }

    public static NotFoundException wrap(String msg) {
        return new NotFoundException(msg);
    }

}
