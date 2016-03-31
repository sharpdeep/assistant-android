package com.sharpdeep.assistant_android.util;

import com.sharpdeep.assistant_android.model.dbModel.User;

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
}
