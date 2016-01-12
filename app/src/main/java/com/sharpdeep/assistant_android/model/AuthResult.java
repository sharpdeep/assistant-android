package com.sharpdeep.assistant_android.model;

import com.google.gson.annotations.Expose;

/**
 * Created by bear on 16-1-10.
 */
public class AuthResult {
    @Expose
    private String status;
    @Expose
    private String msg;
    @Expose
    private Data data;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdentify(){
        return this.data.identify;
    }

    public void setIdentify(String identify){
        this.data.identify = identify;
    }

    public String getToken(){
        return this.data.token;
    }

    public void setToken(String token){
        this.data.token = token;
    }

    public Boolean isSuccess(){
        return "success".equals(this.status);
    }

    class Data {
        @Expose
        private String identify;
        @Expose
        private String token;
    }

}
