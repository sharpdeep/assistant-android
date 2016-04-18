
package com.sharpdeep.assistant_android.model.resultModel;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sharpdeep.assistant_android.helper.Constant;

public class HomeworkResult {

    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("homeworkList")
    @Expose
    private List<Homework> homeworkList = new ArrayList<Homework>();
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
     *     The homeworkList
     */
    public List<Homework> getHomeworkList() {
        return homeworkList;
    }

    /**
     * 
     * @param homeworkList
     *     The homeworkList
     */
    public void setHomeworkList(List<Homework> homeworkList) {
        this.homeworkList = homeworkList;
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

    public boolean isSuccess(){
        return this.status.equals(Constant.SUCCESS);
    }
}
