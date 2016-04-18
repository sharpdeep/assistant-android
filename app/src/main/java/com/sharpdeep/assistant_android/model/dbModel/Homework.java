package com.sharpdeep.assistant_android.model.dbModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by bear on 16-4-18.
 */
public class Homework extends SugarRecord{
    private String content;
    private String createTime;
    private String deadline;
    private String fromUserName;
    private String title;
    private String toUserName;
    private String updateTime;

    public Homework(){}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void saveToDB(com.sharpdeep.assistant_android.model.resultModel.Homework homework){
        this.title = homework.getTitle();
        this.content = homework.getContent();
        this.deadline = homework.getDeadline();
        this.fromUserName = homework.getFromUserName();
        this.toUserName = homework.getToUserName();
        this.createTime = homework.getCreateTime();
        this.updateTime = homework.getUpdateTime();
        this.save();
    }

    public com.sharpdeep.assistant_android.model.resultModel.Homework loadHomework(){
        com.sharpdeep.assistant_android.model.resultModel.Homework homework = new com.sharpdeep.assistant_android.model.resultModel.Homework();
        homework.setTitle(this.title);
        homework.setContent(this.content);
        homework.setDeadline(this.deadline);
        homework.setFromUserName(this.fromUserName);
        homework.setToUserName(this.toUserName);
        homework.setCreateTime(this.createTime);
        homework.setUpdateTime(this.updateTime);
        return homework;
    }
}
