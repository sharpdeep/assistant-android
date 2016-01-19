package com.sharpdeep.assistant_android.helper;

import android.content.Context;

import com.sharpdeep.assistant_android.activity.MainActivity;
import com.sharpdeep.assistant_android.model.resultModel.Lesson;
import com.sharpdeep.assistant_android.model.resultModel.Schedule;
import com.sharpdeep.assistant_android.model.resultModel.SyllabusResult;
import com.sharpdeep.assistant_android.util.DisplayUtil;

import java.util.List;

/**
 * Created by bear on 16-1-18.
 */
public class SyllabusFormater {
    private SyllabusResult mSyllabusResult;
    private List<Lesson> lessons;
    private Lesson[][] mClassTable;
    private int mClassTableRow = 13;
    private int mClassTableColumn = 7;
    private static int weekIndex = 0;
    private static int timeIndex = 0;

    private int unitGridWidth;
    private int unitGridHeigh;

    private int skipNum = 0;

    public SyllabusFormater(Context context,SyllabusResult SyllabusResult) {
        this.mSyllabusResult = SyllabusResult;
        this.lessons = SyllabusResult.getSyllabus();
        this.mClassTable = getClassTable();
        initData(context);
    }

    private void initData(Context context) {
        unitGridWidth = DisplayUtil.dip2px(context,50);
        unitGridWidth = DisplayUtil.dip2px(context,60);
    }

    private Lesson[][] getClassTable() {
        Lesson[][] classTable = new Lesson[mClassTableColumn][mClassTableRow];
        for (int i = 0 ; i < mClassTableColumn; i++){
            for (int j = 0; j < mClassTableRow; j++){
                classTable[i][j] = new Lesson(false);
            }
        }

        for(Lesson lesson : lessons){
            Schedule schedule = lesson.getSchedule();
            for(int week : schedule.getClassWeeks()){
                for (int time : schedule.getClassTimeByWeek(week)){
                    classTable[week][time] = lesson;
                }
            }
        }
        return classTable;
    }



    public Boolean end(){
        return weekIndex >= mClassTableColumn && timeIndex >= mClassTableRow;
    }

    public void next(){
        skipNum = getCurrentLesson().getSchedule().getClassCountByWeek(weekIndex);
        if (skipNum > 0){
            timeIndex += skipNum;

        }else{
            timeIndex++;
        }
        if (timeIndex >= 13){
            weekIndex++;
            timeIndex = 0;
        }
    }

    private Lesson getCurrentLesson(){
        return mClassTable[weekIndex][timeIndex];
    }

    public Boolean isLessonGrid(){
        return getCurrentLesson().isLessonGrid();
    }

    public int getGridWidth(){
        return unitGridWidth;
    }

    public int getGridHeigh(){
        return getCurrentLesson().getSchedule().getClassCountByWeek(weekIndex) * unitGridHeigh;
    }

    public int getGridRowSpec(){
        return timeIndex;
    }

    public int getGridColumnSpec(){
        return weekIndex;
    }

    public String getGridText(){
        return getCurrentLesson().getName()+"@"+getCurrentLesson().getClassroom();
    }
}
