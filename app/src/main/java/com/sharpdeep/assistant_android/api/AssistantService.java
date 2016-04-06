package com.sharpdeep.assistant_android.api;


import com.sharpdeep.assistant_android.model.resultModel.AuthResult;
import com.sharpdeep.assistant_android.helper.Constant;
import com.sharpdeep.assistant_android.model.resultModel.BaseResult;
import com.sharpdeep.assistant_android.model.resultModel.StudentListResult;
import com.sharpdeep.assistant_android.model.resultModel.SyllabusResult;

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

    @GET(prefix+"/classinfo/studentlist/{classid}")
    Observable<StudentListResult> getStudentListByClassid(@Header("Authorization") String token,@Path("classid") String classid);

    @FormUrlEncoded()
    @POST(prefix+"/sign")
    Observable<BaseResult> signin(@Header("Authorization") String token,@Field("classid") String classid, @Field("device_id") String device_id,@Field("mac") String mac);

    @FormUrlEncoded()
    @POST(prefix+"/leave")
    Observable<BaseResult> askLeave(@Header("Authorization") String token,@Field("classid") String classid,@Field("leave_type") int leave_type, @Field("leave_date") String leave_date, @Field("leave_reason") String leave_reason);

    @GET(prefix+"/signlist/student/count/{username}/{date}")
    Observable<BaseResult> getStudentSignlogCount(@Path("username") String username, @Path("date") String date);

    @GET(prefix+"/leavelist/student/count/{username}/{date}")
    Observable<BaseResult> getStudentLeavelogCount(@Path("username") String username, @Path("date") String date);
}
