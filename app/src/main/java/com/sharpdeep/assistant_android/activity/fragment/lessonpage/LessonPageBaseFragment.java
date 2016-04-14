package com.sharpdeep.assistant_android.activity.fragment.lessonpage;

import android.support.v4.app.Fragment;

import com.sharpdeep.assistant_android.activity.LessonHomePageActivity;
import com.sharpdeep.assistant_android.model.resultModel.Student;

import java.util.ArrayList;

/**
 * Created by bear on 16-4-13.
 */
public class LessonPageBaseFragment extends Fragment {
    protected String getLessonId(){
        return getHomeActivity().mLessonId;
    }

    protected int getLessonColor(){
        return getHomeActivity().mLessonColor;
    }

    protected String getLessonName(){
        return getHomeActivity().mLessonName;
    }

    protected String getLessonTeacher(){
        return getHomeActivity().mLessonTeacher;
    }


    private LessonHomePageActivity getHomeActivity(){
        return (LessonHomePageActivity) getActivity();
    }
}
