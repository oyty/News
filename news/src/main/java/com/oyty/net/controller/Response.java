package com.oyty.net.controller;


import org.json.JSONObject;

/**
 * Created by oyty on 5/9/15.
 */
public class Response {

    public int retcode;

    public String result;

    public String data;

    public Response(JSONObject jsonObject) {
        this.retcode = jsonObject.optInt("retcode");
        this.result = jsonObject.toString();
        this.data = jsonObject.optString("data");
    }

    /**
     * 是否请求成功
     * @return
     */
    public boolean isSuccess() {
        return (retcode == 200 ? true : false);
    }
}
