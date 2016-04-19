package com.sharpdeep.assistant_android.api;


import com.sharpdeep.assistant_android.model.resultModel.AuthResult;
import com.sharpdeep.assistant_android.helper.Constant;
import com.sharpdeep.assistant_android.model.resultModel.BaseResult;
import com.sharpdeep.assistant_android.model.resultModel.DiscussionResult;
import com.sharpdeep.assistant_android.model.resultModel.Homework;
import com.sharpdeep.assistant_android.model.resultModel.HomeworkResult;
import com.sharpdeep.assistant_android.model.resultModel.LessonInfoResult;
import com.sharpdeep.assistant_android.model.resultModel.LessonSignlogResult;
import com.sharpdeep.assistant_android.model.resultModel.LeavelogResult;
import com.sharpdeep.assistant_android.model.resultModel.StudentListResult;
import com.sharpdeep.assistant_android.model.resultModel.StudentSIgnlogResult;
import com.sharpdeep.assistant_android.model.resultModel.SyllabusResult;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
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

    @GET(prefix+"/lesson/{lessonid}")
    Observable<LessonInfoResult> getLessonInfo(@Path("lessonid") String lessonid);

    @FormUrlEncoded()
    @POST(prefix+"/syllabus/{start_year}/{semester}")
    Observable<SyllabusResult> addLesson(@Header("Authorization") String token, @Path("start_year") String start_year, @Path("semester") int semester, @Field("classid") String classid);

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

    @GET(prefix+"/signlist/student/{username}/{date}")
    Observable<StudentSIgnlogResult> getStudentSignlog(@Path("username") String username, @Path("date") String date);

    @GET(prefix+"/leavelist/student/{username}/{date}")
    Observable<LeavelogResult> getStudentLeavelog(@Path("username") String username, @Path("date") String date);

    @GET(prefix+"/signlist/lesson/{classid}/{date}")
    Observable<LessonSignlogResult> getLessonSignlog(@Path("classid") String classid, @Path("date") String date);

    @GET(prefix+"/leavelist/lesson/{classid}/{date}")
    Observable<LeavelogResult> getLessonLeaveLog(@Path("classid") String classid, @Path("date") String date);

    @POST(prefix+"/like/lesson/{classid}")
    Observable<BaseResult> likeLesson(@Header("Authorization") String token, @Path("classid") String classid);

    @GET(prefix+"/like/lesson/{classid}")
    Observable<BaseResult> getLessonLikeCount(@Path("classid") String classid);

    @FormUrlEncoded()
    @PUT(prefix+"/discussion/lesson/{classid}")
    Observable<BaseResult> makeLessonDiscussion(@Path("classid") String classid,@Field("fromUserName") String fromUserName, @Field("content") String content);

    @GET(prefix+"/discussion/lesson/{classid}")
    Observable<DiscussionResult> getLessonDiscussionAfter(@Path("classid") String classid,@Query("after") int after_index);

    @FormUrlEncoded()
    @PUT(prefix+"/homework/{classid}")
    Observable<BaseResult> makeHomework(@Header("Authorization") String token,@Path("classid") String classid,@Field("title") String title, @Field("content") String content, @Field("deadline") String deadline);

    @GET(prefix+"/homework/{classid}")
    Observable<HomeworkResult> getHomeworkAfter(@Path("classid") String classid, @Query("after") int after_index);

    @GET(prefix+"/device")
    Observable<BaseResult> getBindingDeviceIds(@Header("Authorization") String token);

    @FormUrlEncoded()
    @PUT(prefix+"/device")
    Observable<BaseResult> bindDevice(@Header("Authorization") String token,@Field("deviceid") String deviceid);

}
