package com.sharpdeep.assistant_android.helper;

import com.sharpdeep.assistant_android.model.dbModel.User;

/**
 * Created by bear on 16-1-15.
 */
public class DataCacher {
    private static DataCacher cacher = new DataCacher();
    private String token = "";
    private String currentYear = "";
    private int currentSemester = 0;
    private long authTime = -1;
    private String identify = "";
    private User currentUser;
    private String showingYear = "";
    private int showingSemester = 0;
    private String showingSyllabus = "";

    private DataCacher(){

    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
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

    public String getShowingYear() {
        return showingYear;
    }

    public void setShowingYear(String showingYear) {
        this.showingYear = showingYear;
    }

    public int getShowingSemester() {
        return showingSemester;
    }

    public void setShowingSemester(int showingSemester) {
        this.showingSemester = showingSemester;
    }

    public String getShowingSyllabus() {
        return showingSyllabus;
    }

    public void setShowingSyllabus(String showingSyllabus) {
        this.showingSyllabus = showingSyllabus;
    }

    public void logout(){
        this.showingYear = "";
        this.showingSemester = 0;
        this.showingSyllabus = "";
    }

    public void free(){
        cacher.setShowingYear("");
        cacher.setShowingSemester(0);
        cacher.setShowingSyllabus("");
    }
}
