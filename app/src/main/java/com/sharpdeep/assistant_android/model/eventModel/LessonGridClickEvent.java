package com.sharpdeep.assistant_android.model.eventModel;

import com.sharpdeep.assistant_android.model.resultModel.Lesson;

/**
 * Created by bear on 16-1-21.
 */
public class LessonGridClickEvent {
    private String message = "";
    private Lesson leeson;
    private int lessonColor;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Lesson getLeeson() {
        return leeson;
    }

    public void setLeeson(Lesson leeson) {
        this.leeson = leeson;
    }

    public int getLessonColor() {
        return lessonColor;
    }

    public void setLessonColor(int lessonColor) {
        this.lessonColor = lessonColor;
    }
}
