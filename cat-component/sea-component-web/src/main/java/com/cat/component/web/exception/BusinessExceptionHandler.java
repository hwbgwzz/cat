package com.cat.component.web.exception;

import com.cat.common.bean.Result;
import com.cat.common.exception.enums.ExceptionEnum;
import com.cat.common.exception.extend.AbortedException;
import com.cat.common.exception.extend.BusinessException;
import com.cat.common.exception.extend.NotFoundException;
import com.cat.common.toolkit.json.JSON;
import com.cat.component.web.request.RequestData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;


import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 统一异常处理器
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class BusinessExceptionHandler {
    private final RequestData requestData;

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BusinessException.class)
    // @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleRRException(BusinessException e) {
        log.error(e.getError() + "_" + e.getMessage(), e);
        printWithRequest();
        return Result.ERROR(ExceptionEnum.BUSINESS.getCode(), e.getMessage());
    }


    /**
     * 处理自定义异常
     */
    // @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public Result<?> handleRRException(NotFoundException e) {
        log.error(e.getMessage(), e);
        printWithRequest();
        return Result.ERROR(ExceptionEnum.NOT_FOUND.getCode(), e.getMessage());
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(AbortedException.class)
    // @ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE)
    public Result<?> handleRRException(AbortedException e) {
        log.error(e.getMessage(), e);
        printWithRequest();
        return Result.ERROR(ExceptionEnum.NOT_FOUND.getCode(), e.getMessage());
    }

    // @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<?> handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        printWithRequest();
        return Result.ERROR(ExceptionEnum.NOT_FOUND.getCode(), e.getMessage());
    }

    /**
     * <a>
     * http://greenbytes.de/tech/webdav/rfc4918.html#rfc.section.11.2
     * </a>
     */
    @ExceptionHandler(DuplicateKeyException.class)
    // @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Result<?> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        printWithRequest();
        return Result.ERROR(ExceptionEnum.ABORTED.getCode(), "数据库资源已存在！");
    }


    @ExceptionHandler(Exception.class)
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        printWithRequest();
        return Result.ERROR(ExceptionEnum.UNKNOWN.getCode(), ExceptionEnum.UNKNOWN.getDescription());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    // @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<?> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        StringBuilder sb = new StringBuilder();
        sb.append("不支持");
        sb.append(e.getMethod());
        sb.append("请求方法，");
        sb.append("支持以下");
        String[] methods = e.getSupportedMethods();
        if (methods != null) {
            for (String str : methods) {
                sb.append(str);
                sb.append("、");
            }
        }
        log.error(sb.toString(), e, "");
        printWithRequest();
        return Result.ERROR(ExceptionEnum.METHOD_NOT_ALLOWED.getCode(), sb.toString());
    }

    /**
     * spring默认上传大小100MB 超出大小捕获异常MaxUploadSizeExceededException
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    // @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public Result<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error(e.getMessage(), e);
        printWithRequest();
        return Result.ERROR(ExceptionEnum.RESOURCE_EXHAUSTED.getCode(), "文件大小超出限制, 请压缩或降低文件质量");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    // @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public Result<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error(e.getMessage(), e);
        printWithRequest();
        return Result.ERROR(ExceptionEnum.RESOURCE_EXHAUSTED.getCode(), "字段太长,超出数据库字段的长度");
    }

    /**
     * Spring - validate exception处理
     */
    @ExceptionHandler(BindException.class)
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> validatedException(BindException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        log.error("参数校验失败,{}", JSON.toJSONString(fieldErrors));
        printWithRequest();
        String messages = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        return Result.ERROR(ExceptionEnum.INVALID_ARGUMENT.getCode(), messages);
    }

    /**
     * Spring - validate exception处理
     */
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> validExceptionHandler(MethodArgumentNotValidException ex) {
        ObjectError objectError = ex.getBindingResult().getAllErrors().stream().findFirst().get();
        log.error("参数校验失败,{}", JSON.toJSONString(objectError));
        printWithRequest();
        String messages = objectError.getDefaultMessage();
        return Result.ERROR(ExceptionEnum.INVALID_ARGUMENT.getCode(), messages);
    }

    /**
     * 处理请求普通参数(非 java bean)上validate失败后抛出的异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> ConstraintViolationExceptionHandler(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        log.error("参数校验失败,{}", JSON.toJSONString(ex.getMessage()));
        printWithRequest();
        String messages = constraintViolations.stream().map(ConstraintViolation::getMessageTemplate).collect(Collectors.joining(";"));
        return Result.ERROR(ExceptionEnum.INVALID_ARGUMENT.getCode(), messages);
    }


    /**
     * 打印mvc的请求参数
     */
    private void printWithRequest() {
        log.error("Request Info Param : {}", requestData.asMap());
    }

}
