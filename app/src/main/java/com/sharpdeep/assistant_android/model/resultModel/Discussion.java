
package com.sharpdeep.assistant_android.model.resultModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sharpdeep.assistant_android.util.DateUtil;

import java.util.Calendar;
import java.util.Date;

public class Discussion {

    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("createTime")
    @Expose
    private String createTime;
    @SerializedName("fromUserName")
    @Expose
    private String fromUserName;
    @SerializedName("toUserName")
    @Expose
    private String toUserName;
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("updateTime")
    @Expose
    private String updateTime;

    public Discussion(){}

    public Discussion(String fromUserName,String toUserName,String content){
        super();
        this.fromUserName = fromUserName;
        this.toUserName = toUserName;
        this.content = content;

        String now = DateUtil.getDateStr(Calendar.getInstance().getTime(),"yyyy-MM-dd HH:mm:ss");

        this.createTime = now;
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
     *     The type
     */
    public int getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(int type) {
        this.type = type;
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
