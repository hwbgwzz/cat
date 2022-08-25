package com.cat.homcat.http.servlet;

import com.cat.homcat.http.field.HmHttpMethod;
import com.cat.homcat.http.request.HmRequest;
import com.cat.homcat.http.response.HmResponse;

public abstract class HmServlet {
    public void service(HmRequest request, HmResponse response) throws Exception{
        if (HmHttpMethod.GET.equalsIgnoreCase(request.getMethod())) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }
    }

    public abstract void doGet(HmRequest request, HmResponse response) throws Exception;

    public abstract void doPost(HmRequest request, HmResponse response) throws Exception;
}

