package com.cat.application.servlet;

import com.cat.homcat.http.request.HmRequest;
import com.cat.homcat.http.response.HmResponse;
import com.cat.homcat.http.servlet.HmServlet;

public class UserServlet extends HmServlet {
    @Override
    public void doGet(HmRequest request, HmResponse response) throws Exception {
        doPost(request, response);
    }

    @Override
    public void doPost(HmRequest request, HmResponse response) throws Exception {
        response.write("this is UserServlet");
    }
}
