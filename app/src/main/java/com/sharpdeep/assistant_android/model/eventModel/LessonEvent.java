package com.sharpdeep.assistant_android.model.eventModel;

import com.sharpdeep.assistant_android.model.resultModel.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bear on 16-4-14.
 */
public class LessonEvent {

    public int mLessonColor;
    public String mLessonName;
    public String mLessonId;
    public String mLessonTeacher;
    private ArrayList<Student> mStudentList;

    public LessonEvent(int lessonColor,String lessonName, String lessonId, String lessonTeacher,ArrayList<Student> studenList){
        this.mLessonColor = lessonColor;
        this.mLessonName = lessonName;
        this.mLessonId = lessonId;
        this.mLessonTeacher = lessonTeacher;
        this.mStudentList = studenList;
    }

    public int getLessonColor() {
        return mLessonColor;
    }

    public void setLessonColor(int lessonColor) {
        this.mLessonColor = lessonColor;
    }

    public String getLessonName() {
        return mLessonName;
    }

    public void setLessonName(String lessonName) {
        this.mLessonName = lessonName;
    }

    public String getLessonId() {
        return mLessonId;
    }

    public void setLessonId(String lessonId) {
        this.mLessonId = lessonId;
    }

    public String getLessonTeacher() {
        return mLessonTeacher;
    }

    public void setLessonTeacher(String lessonTeacher) {
        this.mLessonTeacher = lessonTeacher;
    }

    public ArrayList<Student> getStudentList() {
        return mStudentList;
    }

    public void setStudentList(ArrayList<Student> studentList) {
        this.mStudentList = studentList;
    }
}
