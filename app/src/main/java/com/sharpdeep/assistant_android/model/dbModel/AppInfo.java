package com.sharpdeep.assistant_android.model.dbModel;

import com.orm.SugarRecord;

/**
 * Created by bear on 16-1-13.
 */
public class AppInfo extends SugarRecord {
    private User currentUser;
    private String currentYear;
    private int currentSemester;
    private long authTime;

    public AppInfo() {
    }

    public AppInfo(User currentUser, String currentYear, int currentSemester) {
        this.currentUser = currentUser;
        this.currentYear = currentYear;
        this.currentSemester = currentSemester;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
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
}
