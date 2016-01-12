package com.sharpdeep.assistant_android.helper;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.util.L;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by bear on 16-1-10.
 */
public class RetrofitHelper {
    public final static Retrofit getRetrofit(Context context){
        String baseUrl = context.getResources().getString(R.string.base_url);
        OkHttpClient client = new OkHttpClient();
        //set time out interval
        client.setReadTimeout(10, TimeUnit.MINUTES);
        client.setConnectTimeout(10, TimeUnit.MINUTES);
        client.setWriteTimeout(10, TimeUnit.MINUTES);

        //print log
        client.interceptors().add(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (message.startsWith("{")) {
                    L.json(message);
                } else {
                    L.i("Api", message);
                }
            }
        }));


        // add custom headers
//        client.interceptors().add(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request newRequest = chain.request().newBuilder()
//                        .addHeader("platform", "android")
//                        .addHeader("appVersion", BuildConfig.VERSION_NAME)
//                        .build();
//                return chain.proceed(newRequest);
//            }
//        });

        //set DateFormat
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .client(client) // setup okHttp client
                .addConverterFactory(GsonConverterFactory.create(gson)) // GSON converter
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()); // RxCallAdapte

        return builder.baseUrl(baseUrl).build();
    }
}
