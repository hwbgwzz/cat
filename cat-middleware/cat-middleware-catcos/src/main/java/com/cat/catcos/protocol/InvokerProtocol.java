package com.cat.catcos.protocol;

import lombok.Data;

import java.io.Serializable;

@Data
public class InvokerProtocol implements Serializable {
    private String serviceName;
    private String requestUrl;
    private String requestMethod;

    private String className;
    private String methodName;
    private Class<?>[] params;
    private Object[] values;
}
