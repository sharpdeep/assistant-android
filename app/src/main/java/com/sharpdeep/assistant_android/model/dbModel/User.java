package com.sharpdeep.assistant_android.model.dbModel;

import android.content.Context;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;
import com.sharpdeep.assistant_android.model.resultModel.SyllabusResult;
import com.sharpdeep.assistant_android.util.L;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by bear on 16-1-9.
 */
public class User extends SugarRecord{
    @Unique
    private String username;
    private String password;
    private String identify = "";
    private String token = "";
    private long authTime = 0l;
    private String currentYear;
    private int currentSemester;
    private String currentSyllabus;

    public User(){

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public long getAuthTime() {
        return authTime;
    }

    public void setAuthTime(long authTime) {
        this.authTime = authTime;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public User setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
        return this;
    }

    public int getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentSemester(int currentSemester) {
        this.currentSemester = currentSemester;
    }

    public void setSyllabusResult(String resultStr){
        this.currentSyllabus = resultStr;
    }

    public SyllabusResult getSyllabusResult(){
        return SyllabusResult.fromJson(this.currentSyllabus);
    }

}
