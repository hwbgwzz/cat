package com.cat.common.exception.enums;


public enum ExceptionEnum {
    SUCCESS(200, "ok"),
    INVALID_ARGUMENT(400, "客户端使用了错误的参数"),
    UNAUTHENTICATED(401, "缺少、无效或过期的 OAuth 令牌，请求未通过身份验证"),
    PERMISSION_DENIED(403, "没有足够的权限，这可能是因为 OAuth 令牌没有正确的范围，客户端没有权限或者 API 还没有开放"),
    NOT_FOUND(404, "指定的资源不存在，或者由于未公开的原因（如白名单）请求被拒绝"),
    METHOD_NOT_ALLOWED(405, "http请求方法不被允许"),
    ABORTED(409, "并发冲突/读写冲突/资源已经存在"),
    RESOURCE_EXHAUSTED(429, "超过资源限额或频率限制"),
    UNKNOWN(500, "服务器异常"),
    FILE_UPLOAD_ERROR(413, "文件上传错误"),
    BUSINESS(900, "业务错误");

    private ExceptionEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 错误码 code
     */
    private int code;
    /**
     * 错误码描述
     */
    private String description;

    public int getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }
}
