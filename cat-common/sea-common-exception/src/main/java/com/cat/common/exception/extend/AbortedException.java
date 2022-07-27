package com.cat.common.exception.extend;


import com.cat.common.exception.enums.ErrorCodeEnum;

public class AbortedException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String errorCode;

    public String getError() {
        return errorCode;
    }

    public AbortedException(String msg) {
        super(msg);
    }

    public AbortedException(Throwable cause) {
        super(cause);
    }

    public AbortedException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getDescription());
        this.errorCode = errorCodeEnum.getCode();
    }

    public AbortedException(ErrorCodeEnum errorCodeEnum, String... values) {
        super(String.format(errorCodeEnum.getDescription(), values));
        this.errorCode = errorCodeEnum.getCode();
    }

    public AbortedException(ErrorCodeEnum errorCodeEnum, Throwable cause) {

        super(errorCodeEnum.getDescription(), cause);
        this.errorCode = errorCodeEnum.getCode();
    }

    public AbortedException(ErrorCodeEnum errorCodeEnum, Throwable cause, String... values) {

        super(String.format(errorCodeEnum.getDescription(), values), cause);
        this.errorCode = errorCodeEnum.getCode();
    }

    public static AbortedException wrap(String msg) {
        return new AbortedException(msg);
    }

}
