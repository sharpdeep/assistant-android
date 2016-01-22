package com.sharpdeep.assistant_android.helper;

import android.content.Context;
import android.graphics.Color;

import com.sharpdeep.assistant_android.activity.MainActivity;
import com.sharpdeep.assistant_android.model.resultModel.Lesson;
import com.sharpdeep.assistant_android.model.resultModel.Schedule;
import com.sharpdeep.assistant_android.model.resultModel.SyllabusResult;
import com.sharpdeep.assistant_android.util.DisplayUtil;
import com.sharpdeep.assistant_android.util.L;

import java.util.List;
import java.util.Random;

/**
 * Created by bear on 16-1-18.
 */
public class SyllabusFormater {
    private SyllabusResult mSyllabusResult;
    private List<Lesson> lessons;
    private final static int mClassTableRow = 13;
    private final static int mClassTableColumn = 7;
    private Lesson mClassTable[][];
    private static int weekIndex = 0;
    private static int timeIndex = 0;

    private int unitGridWidth;
    private int unitGridHeigh;

    private int step; //表示一次next前进的步数

    public SyllabusFormater(Context context,SyllabusResult SyllabusResult) {
        this.mSyllabusResult = SyllabusResult;
        this.lessons = SyllabusResult.getSyllabus();
        initData(context);
    }


    private void initData(Context context) {
        initClassTable();
        weekIndex = 0;
        timeIndex = 0;
        unitGridWidth = DisplayUtil.dip2px(context, 50);
        unitGridHeigh = DisplayUtil.dip2px(context,60);
    }

    private void initClassTable() {
        mClassTable = new Lesson[mClassTableColumn][mClassTableRow];
        for (int i = 0 ; i < mClassTableColumn; i++){
            for (int j = 0; j < mClassTableRow; j++){
                mClassTable[i][j] = new Lesson(false);
            }
        }

        for(Lesson lesson : lessons){
            Schedule schedule = lesson.getSchedule();
            for(int week : schedule.getClassWeeks()){
                for (int time : schedule.getClassTimeByWeek(week)){
                    mClassTable[week][time-1] = lesson;
                }
            }
        }

    }



    public Boolean end(){
        return weekIndex >= mClassTableColumn;
    }

    public void next(){
        step = getStreakNum();
        timeIndex += step;
        if (timeIndex >= mClassTableRow){
            weekIndex++;
            timeIndex = 0;
        }
    }

    public Lesson getCurrentLesson(){
        return mClassTable[weekIndex][timeIndex];
    }

    public Boolean isLessonGrid(){
        return getCurrentLesson().isLessonGrid();
    }

    public int getGridWidth(){
        return unitGridWidth;
    }

    public int getGridHeigh(){
        return getStreakNum() * unitGridHeigh;
//        return getCurrentLesson().getSchedule().getClassCountByWeek(weekIndex) * unitGridHeigh;
    }

    public int getGridRowSpec(){
        return timeIndex;
    }

    public int getGridColumnSpec(){
        return weekIndex;
    }

    public int getGridRowSpan(){
        return getStreakNum();
    }

    public int getGridColumnSpan(){
        return 1;
    }

    public String getGridText(){
        if (!getCurrentLesson().isLessonGrid()){
            return "";
        }
        String className = getCurrentLesson().getName();
        className = className.substring(className.indexOf("]")+1);
        return className +"@"+getCurrentLesson().getClassroom();
    }

    public int getTextColor(){
        return Color.WHITE;
    }

    public int getBGRandomColor(){
        if (!getCurrentLesson().isLessonGrid()){
            return Color.argb(0,255,255,255);
        }
        Random random = new Random();
        int red = random.nextInt(200);
        int green = random.nextInt(200);
        int blue = random.nextInt(255);
        return Color.argb(255,red,green,blue);
    }

    private int getStreakNum(){
        Lesson currentLesson = getCurrentLesson();
        int streak = 1;

        if (!currentLesson.isLessonGrid()){//非课程，一个单位高度
            return streak;
        }

        for (int i = timeIndex+1; i < mClassTableRow; i++){
            if (i >= mClassTableRow){
                break;
            }
            Lesson nextLesson = mClassTable[weekIndex][i];
            if (!nextLesson.isLessonGrid()){
                break;
            }
            if (nextLesson.getId().equals(currentLesson.getId())){
                streak++;
            }else {
                break;
            }
        }
        return streak;
    }

    public boolean getClickable(){
        return getCurrentLesson().isLessonGrid();
    }
}
