package com.cat.component.web.request;

import com.cat.common.toolkit.ObjectUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于记录MVC请求
 */
@Slf4j
@Component
public class RequestData extends HandlerInterceptorAdapter implements RequestBodyAdvice, WebMvcConfigurer {

    private String method;
    private String body;
    private String queryString;
    private String ip;
    private String user;
    private String referrer;
    private String url;
    private String apiNotes;

    public Map<String, String> asMap() {
        Map<String, String> map = new HashMap<>();
        putIfNotNull(map, "method", this.method);
        putIfNotNull(map, "url", this.url);
        putIfNotNull(map, "queryParams", this.queryString);
        putIfNotNull(map, "body", this.body);
        putIfNotNull(map, "ip", this.ip);
        putIfNotNull(map, "referrer", this.referrer);
        putIfNotNull(map, "user", this.user);
        putIfNotNull(map, "apiNotes", this.apiNotes);
        return map;
    }

    private void setInfoFromRequest(HttpServletRequest request, Object handler) {
        this.method = request.getMethod();
        this.queryString = request.getQueryString();
        this.ip = request.getRemoteAddr();
        this.referrer = request.getRemoteHost();
        this.url = request.getRequestURI();
        //this.user = UserInfoContext.getUserName();
        if (handler instanceof HandlerMethod) {
            ApiOperation operation = ((HandlerMethod) handler).getMethod().getAnnotation(ApiOperation.class);
            if (operation != null) {
                this.apiNotes = ObjectUtil.defaultIfEmpty(operation.value(), operation.notes());
            }
        }
    }

    private <K, V> void putIfNotNull(Map<K, V> map, K k, V v) {
        if (map != null && v != null) map.put(k, v);
    }

    private void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            setInfoFromRequest(request, handler);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {

    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        setBody(body.toString());
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this);
    }
}
