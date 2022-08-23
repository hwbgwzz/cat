package com.cat.common.exception.enums;

/**
 * 错误码：
 * 1. 五位组成
 * 2. A代表用户端错误
 * 3. B代表当前系统异常
 * 4. C代表第三方服务异常
 * 5. D代表各个业务系统产品文档上异常提示
 * 4. 若无法确定具体错误，选择宏观错误
 * 6. 大的错误类间的步长间距预留100
 */
public enum ErrorCodeEnum {

    /**
     * 成功
     */
    SUCCESS("00000", "一切 ok");

    private final String code;

    private final String description;

    ErrorCodeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

