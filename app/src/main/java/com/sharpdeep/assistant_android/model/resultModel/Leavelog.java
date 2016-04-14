
package com.sharpdeep.assistant_android.model.resultModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Leavelog {

    @SerializedName("classid")
    @Expose
    private String classid;
    @SerializedName("classname")
    @Expose
    private String classname;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("leave_date")
    @Expose
    private String leaveDate;
    @SerializedName("leave_reason")
    @Expose
    private String leaveReason;
    @SerializedName("leave_type")
    @Expose
    private int leaveType;
    @SerializedName("studentid")
    @Expose
    private String studentid;
    @SerializedName("studentname")
    @Expose
    private String studentname;

    /**
     * 
     * @return
     *     The classid
     */
    public String getClassid() {
        return classid;
    }

    /**
     * 
     * @param classid
     *     The classid
     */
    public void setClassid(String classid) {
        this.classid = classid;
    }

    /**
     * 
     * @return
     *     The classname
     */
    public String getClassname() {
        return classname;
    }

    /**
     * 
     * @param classname
     *     The classname
     */
    public void setClassname(String classname) {
        this.classname = classname;
    }

    /**
     *
     * @return
     *     The date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     *     The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * 
     * @return
     *     The leaveDate
     */
    public String getLeaveDate() {
        return leaveDate;
    }

    /**
     * 
     * @param leaveDate
     *     The leave_date
     */
    public void setLeaveDate(String leaveDate) {
        this.leaveDate = leaveDate;
    }

    /**
     * 
     * @return
     *     The leaveReason
     */
    public String getLeaveReason() {
        return leaveReason;
    }

    /**
     * 
     * @param leaveReason
     *     The leave_reason
     */
    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    /**
     * 
     * @return
     *     The leaveType
     */
    public int getLeaveType() {
        return leaveType;
    }

    /**
     * 
     * @param leaveType
     *     The leave_type
     */
    public void setLeaveType(int leaveType) {
        this.leaveType = leaveType;
    }

    /**
     * 
     * @return
     *     The studentid
     */
    public String getStudentid() {
        return studentid;
    }

    /**
     * 
     * @param studentid
     *     The studentid
     */
    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    /**
     * 
     * @return
     *     The studentname
     */
    public String getStudentname() {
        return studentname;
    }

    /**
     * 
     * @param studentname
     *     The studentname
     */
    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

}
