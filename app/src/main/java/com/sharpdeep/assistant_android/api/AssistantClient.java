package com.sharpdeep.assistant_android.api;

import android.app.Application;

import com.sharpdeep.assistant_android.MyApplication;
import com.sharpdeep.assistant_android.helper.RetrofitHelper;

import retrofit.Retrofit;
import rx.schedulers.Schedulers;

/**
 * Created by bear on 16-4-19.
 */
public class AssistantClient {
    private static AssistantService mService;
    protected static final Object monitor = new Object();

    private AssistantClient(){}

    public static AssistantService getServiceInstance(){
        synchronized (monitor){
            if (mService == null){
                mService = RetrofitHelper.getRetrofit(MyApplication.getContext())
                        .create(AssistantService.class);

            }
            return mService;
        }
    }

}
