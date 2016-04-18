
package com.sharpdeep.assistant_android.model.resultModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sharpdeep.assistant_android.util.DateUtil;

import java.util.Calendar;

public class Homework {

    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("createTime")
    @Expose
    private String createTime;
    @SerializedName("deadline")
    @Expose
    private String deadline;
    @SerializedName("fromUserName")
    @Expose
    private String fromUserName;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("toUserName")
    @Expose
    private String toUserName;
    @SerializedName("updateTime")
    @Expose
    private String updateTime;

    public Homework(){}

    public Homework(String fromUserName,String toUserName,String title,String content,String deadline){
        super();
        this.fromUserName = fromUserName;
        this.toUserName = toUserName;
        this.title = title;
        this.content = content;
        this.deadline = deadline;

        String now = DateUtil.getDateStr(Calendar.getInstance().getTime(),"yyyy-MM-dd HH:mm:ss");

        this.createTime  = now;
        this.updateTime = now;
    }

    /**
     * 
     * @return
     *     The content
     */
    public String getContent() {
        return content;
    }

    /**
     * 
     * @param content
     *     The content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 
     * @return
     *     The createTime
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 
     * @param createTime
     *     The createTime
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 
     * @return
     *     The deadline
     */
    public String getDeadline() {
        return deadline;
    }

    /**
     * 
     * @param deadline
     *     The deadline
     */
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    /**
     * 
     * @return
     *     The fromUserName
     */
    public String getFromUserName() {
        return fromUserName;
    }

    /**
     * 
     * @param fromUserName
     *     The fromUserName
     */
    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The toUserName
     */
    public String getToUserName() {
        return toUserName;
    }

    /**
     * 
     * @param toUserName
     *     The toUserName
     */
    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    /**
     * 
     * @return
     *     The updateTime
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * 
     * @param updateTime
     *     The updateTime
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
