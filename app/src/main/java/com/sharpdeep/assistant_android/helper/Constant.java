package com.sharpdeep.assistant_android.helper;

/**
 * Created by bear on 16-1-10.
 */
public class Constant {
    public static final String API_PREFIX = "/api";
    public static final long EXPRIED = 7*24*3600;//token有效期

    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";

    public static final int LEAVE_OTHER = 0; //其他类型
    public static final int LEAVE_SICK = 1; //病假
    public static final int LEAVE_AFFAIR = 2; //事假

    public static String getLeaveTypeName(int type){
        switch (type){
            case LEAVE_OTHER:
                return "其他类型";
            case LEAVE_SICK:
                return "病假";
            case LEAVE_AFFAIR:
                return "事假";
        }
        return "其他类型";
    }
}
