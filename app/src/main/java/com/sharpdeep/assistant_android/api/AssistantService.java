package com.sharpdeep.assistant_android.api;


import com.sharpdeep.assistant_android.model.AuthResult;
import com.sharpdeep.assistant_android.helper.Constant;
import com.sharpdeep.assistant_android.model.SyllabusResult;

import retrofit.Call;
import retrofit.CallAdapter;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by bear on 16-1-10.
 */
public interface AssistantService {
    String prefix = Constant.API_PREFIX;

    @FormUrlEncoded()
    @POST(prefix+"/auth")
    Observable<AuthResult> authUser(@Field("username") String username, @Field("password") String password);

    @GET(prefix+"/syllabus/{start_year}/{semester}")
    Observable<SyllabusResult> getSyllabus(@Header("Authorization") String token,@Path("start_year") String start_year,@Path("semester")int semester);
}
