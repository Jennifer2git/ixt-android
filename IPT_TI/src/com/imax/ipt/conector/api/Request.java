package com.imax.ipt.conector.api;

import java.util.List;

import com.imax.ipt.controller.eventbus.handler.Handler;

public class Request {
    private int id;
    private String method;
    private List<Object> params;

    private Handler handler;

    public Request() {
    }

    public Request(int id, String method, List<Object> params, Handler handler) {
        super();
        this.id = id;
        this.method = method;
        this.params = params;
        this.handler = handler;
    }


    public int getId() {
        return id;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }
}
