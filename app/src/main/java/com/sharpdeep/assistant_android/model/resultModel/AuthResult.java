package com.sharpdeep.assistant_android.model.resultModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sharpdeep.assistant_android.helper.Constant;

/**
 * Created by bear on 16-1-10.
 */
public class AuthResult {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
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
        return Constant.SUCCESS.equals(this.status);
    }

    class Data {
        @SerializedName("identify")
        @Expose
        private String identify;
        @SerializedName("token")
        @Expose
        private String token;
    }

}
