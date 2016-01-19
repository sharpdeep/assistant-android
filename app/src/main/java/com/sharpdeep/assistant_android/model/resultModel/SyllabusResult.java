
package com.sharpdeep.assistant_android.model.resultModel;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sharpdeep.assistant_android.helper.Constant;

public class SyllabusResult {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("syllabuses")
    @Expose
    private List<Lesson> syllabus  = new ArrayList<Lesson>();

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

    /**
     * 
     * @return
     *     The syllabus
     */
    public List<Lesson> getSyllabus() {
        return syllabus;
    }

    /**
     * 
     * @param lessons
     *     The syllabuses
     */
    public void setSyllabus(List<Lesson> lessons) {
        this.syllabus = lessons;
    }

    public Boolean isSuccess(){
        return Constant.SUCCESS.equals(this.status);
    }
}
