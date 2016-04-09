
package com.sharpdeep.assistant_android.model.resultModel;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentLeavelogResult {

    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("leavelog")
    @Expose
    private List<StudentLeavelog> leavelog = new ArrayList<StudentLeavelog>();
    @SerializedName("msg")
    @Expose
    private String msg;
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
     *     The leavelog
     */
    public List<StudentLeavelog> getLeavelog() {
        return leavelog;
    }

    /**
     * 
     * @param leavelog
     *     The leavelog
     */
    public void setLeavelog(List<StudentLeavelog> leavelog) {
        this.leavelog = leavelog;
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

}
