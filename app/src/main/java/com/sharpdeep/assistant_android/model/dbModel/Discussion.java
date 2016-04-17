package com.sharpdeep.assistant_android.model.dbModel;

import com.orm.SugarRecord;
import com.sharpdeep.assistant_android.helper.Constant;

/**
 * Created by bear on 16-4-17.
 */
public class Discussion extends SugarRecord {
    private String content;
    private String createTime;
    private String fromUserName;
    private String toUserName;
    private int type;
    private String updateTime;

    public Discussion(){}

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

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void saveToDB(com.sharpdeep.assistant_android.model.resultModel.Discussion discussion){
        this.fromUserName = discussion.getFromUserName();
        this.content = discussion.getContent();
        this.createTime = discussion.getCreateTime();
        this.updateTime = discussion.getUpdateTime();
        this.type = discussion.getType();
        this.toUserName = discussion.getToUserName();
        this.save();
    }

    public com.sharpdeep.assistant_android.model.resultModel.Discussion loadDiscussion(){
        com.sharpdeep.assistant_android.model.resultModel.Discussion discussion =  new com.sharpdeep.assistant_android.model.resultModel.Discussion();
        discussion.setFromUserName(this.fromUserName);
        discussion.setToUserName(this.toUserName);
        discussion.setType(this.type);
        discussion.setCreateTime(this.createTime);
        discussion.setCreateTime(this.updateTime);
        discussion.setContent(this.content);
        return discussion;
    }
}
