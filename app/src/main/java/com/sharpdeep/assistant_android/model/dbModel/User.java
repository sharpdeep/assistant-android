package com.sharpdeep.assistant_android.model.dbModel;

import android.content.Context;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by bear on 16-1-9.
 */
public class User extends SugarRecord{
    @Unique
    private String username;
    private String password;
    private String identify;
    private String token;

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
}
