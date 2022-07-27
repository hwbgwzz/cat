package com.cat.common.bean;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cat.common.exception.enums.ExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;


/**
 * 统一rest接口返回格式
 * @param <T>
 */
@ApiModel("通用接口返回对象")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    @ApiModelProperty(required = true, notes = "成功标志")
    private boolean success = true;

    /**
     * 返回处理消息
     */
    @ApiModelProperty(required = true, notes = "返回处理消息")
    private String msg = "操作成功！";

    /**
     * 返回代码
     */

    @ApiModelProperty(required = true, notes = "返回代码", example = "0")
    private Integer code = 0;

    /**
     * 跳转url
     */
    @ApiModelProperty(required = true, notes = "跳转url")
    private String url;

    /**
     * 总件数
     */
    @ApiModelProperty(required = true, notes = "总条数")
    private Integer count = 0;

    /**
     * 时间戳
     */
    @ApiModelProperty(required = true, notes = "时间戳")
    private long timestamp = System.currentTimeMillis();

    /**
     * 返回数据对象 data
     */
    @ApiModelProperty(required = true, notes = "返回数据对象")
    private T data;

    public static Result<?> error(String s) {
        return ERROR(s);
    }

    public Result<T> JUMP(String url) {
        this.url = url;
        this.success = true;
        return this;
    }

    public Result<T> JUMP(String url, String message) {
        this.url = url;
        this.success = true;
        return this;
    }

    public static <T> Result<T> OK() {
        Result<T> r = new Result<T>();
        r.setCode(HttpStatus.HTTP_OK);
        r.setSuccess(true);
        r.setMsg("成功");
        return r;
    }

    public static <T> Result<T> OK(T data) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setData(data);
        r.setCode(HttpStatus.HTTP_OK);
        if (data instanceof IPage && !ObjectUtil.isEmpty(data)) {
            r.setData((T) ((IPage<?>) data).getRecords());
            r.setCount(Long.valueOf(((IPage<?>) data).getTotal()).intValue());
        }
        if (data instanceof Collection && !ObjectUtil.isEmpty(data)) {
            r.setCount(((Collection<?>) data).size());
        }
        return r;
    }

    public static Result<?> OK(int influenceCount) {
        return influenceCount > 0 ? Result.OK() : Result.error("操作失败");
    }

    public static <T> Result<T> OK(String msg, T data) {
        Result<T> r = OK(data);
        r.setMsg(msg);
        return r;
    }

    public static <T> Result<T> ERROR(String msg) {
        return ERROR(ExceptionEnum.UNKNOWN.getCode(), msg);
    }

    public static <T> Result<T> ERROR(int code, String msg) {
        Result<T> r = new Result<T>();
        r.setCode(code);
        r.setMsg(msg);
        r.setSuccess(false);
        return r;
    }

    public Result<T> error500(String msg) {
        this.msg = msg;
        this.code = ExceptionEnum.UNKNOWN.getCode();
        this.success = false;
        return this;
    }

    public static <T> Result<T> error(boolean flag) {
        Result<T> r = new Result<T>();
        r.setCode(ExceptionEnum.UNKNOWN.getCode());
        r.setMsg(ExceptionEnum.UNKNOWN.getDescription());
        r.setSuccess(flag);
        return r;
    }
}
