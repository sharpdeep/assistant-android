package com.sharpdeep.assistant_android.model.bean;

/**
 * Created by bear on 16-1-23.
 */
public class DiscussionMessage {
    private Boolean mIsMine;
    private String mContent;

    public DiscussionMessage(String mContent,Boolean mIsMine) {
        this.mIsMine = mIsMine;
        this.mContent = mContent;
    }

    public Boolean isMine() {
        return mIsMine;
    }

    public void setIsMine(Boolean mIsMine) {
        this.mIsMine = mIsMine;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }
}
