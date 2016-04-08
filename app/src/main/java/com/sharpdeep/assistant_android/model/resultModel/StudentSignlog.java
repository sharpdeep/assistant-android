
package com.sharpdeep.assistant_android.model.resultModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentSignlog {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("lesson_id")
    @Expose
    private String lessonId;
    @SerializedName("lesson_name")
    @Expose
    private String lessonName;

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
     *     The lessonId
     */
    public String getLessonId() {
        return lessonId;
    }

    /**
     * 
     * @param lessonId
     *     The lesson_id
     */
    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    /**
     * 
     * @return
     *     The lessonName
     */
    public String getLessonName() {
        return lessonName;
    }

    /**
     * 
     * @param lessonName
     *     The lesson_name
     */
    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

}
