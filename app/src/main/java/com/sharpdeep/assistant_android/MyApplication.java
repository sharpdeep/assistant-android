package com.sharpdeep.assistant_android;

import android.app.Activity;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.orm.SugarApp;
import com.sharpdeep.assistant_android.conf.AbstractConfig;
import com.sharpdeep.assistant_android.conf.ProjectConfig;
import com.sharpdeep.assistant_android.util.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bear on 16-4-7.
 */
public class MyApplication extends SugarApp {
    private List<Activity> activityList = new ArrayList<>();
    private static Context mContext;
    private AbstractConfig config;

    @Override
    public void onCreate() {
        super.onCreate();
        config = new ProjectConfig();
        mContext = getApplicationContext();
        AVOSCloud.initialize(this, config.getLeanCloudAppId(), config.getLeanCloudAppKey());
    }

    public void addActivity(Activity activity){
        activityList.add(activity);
    }



    public void finishAllActivity(){
        for (Activity activity : activityList){
            activity.finish();
        }
    }

    public static Context getContext(){
        return mContext;
    }
}
