
package com.sharpdeep.assistant_android.model.resultModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LessonSignlog {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("student_id")
    @Expose
    private String studentId;
    @SerializedName("student_name")
    @Expose
    private String studentName;

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
     *     The studentId
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * 
     * @param studentId
     *     The student_id
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * 
     * @return
     *     The studentName
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * 
     * @param studentName
     *     The student_name
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

}
