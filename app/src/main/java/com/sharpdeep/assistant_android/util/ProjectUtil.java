package com.sharpdeep.assistant_android.util;

import com.sharpdeep.assistant_android.model.dbModel.User;
import com.sharpdeep.assistant_android.model.resultModel.Leavelog;

/**
 * Created by bear on 16-3-20.
 */
public class ProjectUtil {
    public final static Boolean isStudent(String identify){
        return identify.equals(User.IDENTIFY_STUDENT);
    }
    public final static Boolean isTeacher(String identify){
        return identify.equals(User.IDENTIFY_TEACHER);
    }
    public final static String genLeaveId(Leavelog log){
        return log.getStudentid()+log.getClassid()+log.getLeaveDate();
    }
}
