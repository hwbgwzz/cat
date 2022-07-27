package com.cat.common.exception.extend;


import com.cat.common.exception.enums.ErrorCodeEnum;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String errorCode;


    public String getError() {
        return errorCode;
    }

    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getDescription());
        this.errorCode = errorCodeEnum.getCode();
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum, String... values) {
        super(String.format(errorCodeEnum.getDescription(), values));
        this.errorCode = errorCodeEnum.getCode();
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum, Throwable cause) {

        super(errorCodeEnum.getDescription(), cause);
        this.errorCode = errorCodeEnum.getCode();
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum, Throwable cause, String... values) {

        super(String.format(errorCodeEnum.getDescription(), values), cause);
        this.errorCode = errorCodeEnum.getCode();
    }

    public static BusinessException wrap(String msg) {
        return new BusinessException(msg);
    }

    public static BusinessException wrap(ErrorCodeEnum errorCodeEnum) {
        return new BusinessException(errorCodeEnum);
    }

    public static BusinessException wrap(ErrorCodeEnum errorCodeEnum, String... values) {
        return new BusinessException(errorCodeEnum, values);
    }

}
