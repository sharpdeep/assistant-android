
package com.sharpdeep.assistant_android.model.resultModel;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sharpdeep.assistant_android.helper.Constant;

public class StudentListResult {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("students")
    @Expose
    private List<Student> students = new ArrayList<Student>();

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
     *     The students
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * 
     * @param students
     *     The students
     */
    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public boolean isSuccess(){
        return Constant.SUCCESS.equals(getStatus());
    }
}
