
package com.sharpdeep.assistant_android.model.resultModel;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sharpdeep.assistant_android.helper.Constant;

public class LessonSignlogResult {

    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("signlog")
    @Expose
    private List<LessonSignlog> signlog = new ArrayList<LessonSignlog>();
    @SerializedName("status")
    @Expose
    private String status;

    /**
     * 
     * @return
     *     The errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * 
     * @param errorCode
     *     The error_code
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 
     * @return
     *     The msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 
     * @param msg
     *     The msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 
     * @return
     *     The signlog
     */
    public List<LessonSignlog> getSignlog() {
        return signlog;
    }

    /**
     * 
     * @param signlog
     *     The signlog
     */
    public void setSignlog(List<LessonSignlog> signlog) {
        this.signlog = signlog;
    }

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccess(){
        return this.status.equals(Constant.SUCCESS);
    }
}
