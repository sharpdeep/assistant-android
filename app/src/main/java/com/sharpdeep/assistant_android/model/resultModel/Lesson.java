
package com.sharpdeep.assistant_android.model.resultModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lesson {

    @SerializedName("classroom")
    @Expose
    private String classroom;
    @SerializedName("credit")
    @Expose
    private double credit;
    @SerializedName("end_week")
    @Expose
    private int endWeek;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("schedule")
    @Expose
    private Schedule schedule;
    @SerializedName("start_week")
    @Expose
    private int startWeek;
    @SerializedName("teacher")
    @Expose
    private String teacher;

    private Boolean isLessonGrid = true; //是否是课程格子

    public Lesson(Boolean isLessonGrid){
        this.isLessonGrid = isLessonGrid;
    }

    /**
     * 
     * @return
     *     The classroom
     */
    public String getClassroom() {
        return classroom;
    }

    /**
     * 
     * @param classroom
     *     The classroom
     */
    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    /**
     * 
     * @return
     *     The credit
     */
    public double getCredit() {
        return credit;
    }

    /**
     * 
     * @param credit
     *     The credit
     */
    public void setCredit(double credit) {
        this.credit = credit;
    }

    /**
     * 
     * @return
     *     The endWeek
     */
    public int getEndWeek() {
        return endWeek;
    }

    /**
     * 
     * @param endWeek
     *     The end_week
     */
    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The schedule
     */
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * 
     * @param schedule
     *     The schedule
     */
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    /**
     * 
     * @return
     *     The startWeek
     */
    public int getStartWeek() {
        return startWeek;
    }

    /**
     * 
     * @param startWeek
     *     The start_week
     */
    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    /**
     * 
     * @return
     *     The teacher
     */
    public String getTeacher() {
        return teacher;
    }

    /**
     * 
     * @param teacher
     *     The teacher
     */
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public Boolean isLessonGrid(){
        return this.isLessonGrid;
    }

}
