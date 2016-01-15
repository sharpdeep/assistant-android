package com.sharpdeep.assistant_android.helper;

import com.sharpdeep.assistant_android.model.dbModel.User;

/**
 * Created by bear on 16-1-15.
 */
public class DataCacher {
    private static DataCacher cacher = new DataCacher();
    private String token = "";
    private String currentYear = "";
    private int currentSemester = -1;
    private long authTime = -1;
    private String identify = "";

    private DataCacher(){

    }

    public static DataCacher getInstance(){
        return cacher;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }

    public int getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentSemester(int currentSemester) {
        this.currentSemester = currentSemester;
    }

    public long getAuthTime() {
        return authTime;
    }

    public void setAuthTime(long authTime) {
        this.authTime = authTime;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }
}
