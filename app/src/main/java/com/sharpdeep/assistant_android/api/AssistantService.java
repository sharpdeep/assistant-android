package com.sharpdeep.assistant_android.api;


import com.sharpdeep.assistant_android.model.AuthResult;
import com.sharpdeep.assistant_android.helper.Constant;

import retrofit.Call;
import retrofit.CallAdapter;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by bear on 16-1-10.
 */
public interface AssistantService {
    String version = Constant.API_VERSION;

    @FormUrlEncoded()
    @POST(version+"/auth")
    Observable<AuthResult> authUser(@Field("username") String username, @Field("password") String password);
}
