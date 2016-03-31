package com.sharpdeep.assistant_android.model.resultModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sharpdeep.assistant_android.helper.Constant;

/**
 * Created by bear on 16-3-31.
 */
public class BaseResult {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("error_code")
    @Expose
    private long error_code;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getError_code() {
        return error_code;
    }

    public void setError_code(long error_code) {
        this.error_code = error_code;
    }

    public Boolean isSuccess(){
        return Constant.SUCCESS.equals(this.status);
    }

    public String convertCode(){
        return "";
    }
}
