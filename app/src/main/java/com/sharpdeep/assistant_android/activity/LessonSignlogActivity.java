package com.sharpdeep.assistant_android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.melnykov.fab.FloatingActionButton;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.adapter.LessonSignlogAdapter;
import com.sharpdeep.assistant_android.api.AssistantService;
import com.sharpdeep.assistant_android.helper.RetrofitHelper;
import com.sharpdeep.assistant_android.listener.DatePickedListener;
import com.sharpdeep.assistant_android.model.eventModel.LessonEvent;
import com.sharpdeep.assistant_android.model.resultModel.Leavelog;
import com.sharpdeep.assistant_android.model.resultModel.LeavelogResult;
import com.sharpdeep.assistant_android.model.resultModel.LessonSignlog;
import com.sharpdeep.assistant_android.model.resultModel.LessonSignlogResult;
import com.sharpdeep.assistant_android.model.resultModel.Student;
import com.sharpdeep.assistant_android.util.DateUtil;
import com.sharpdeep.assistant_android.util.L;
import com.sharpdeep.assistant_android.util.ToastUtil;
import com.sharpdeep.assistant_android.view.DatePickerDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by bear on 16-4-13.
 */
public class LessonSignlogActivity extends AppCompatActivity {

    @Bind(R.id.fab_lesson_signlog)
    FloatingActionButton mFab;
    @Bind(R.id.listview_lesson_signlog)
    ExpandableListView mLvSignlog;
    @Bind(R.id.toolbar_lesson_signlog)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_title_lesson_signlog)
    TextView mToolbarTitle;
    @Bind(R.id.lesson_signlog_filter)
    RadioGroup mSignlogFilter;

    private LessonSignlogAdapter mAdapter;
    private DatePickerDialog mDatePickerDialog;

    private LessonEvent mLessonEvent;

    final static int FILTER_TYPE_SIGNED = 0;
    final static int FILTER_TYPE_NOT_SIGNED = 1;

    private ArrayList<LessonSignlog> mSignedList;
    private ArrayList<Leavelog> mLeaveList;

    private HashMap<String,Student> mStudentMap;
    private String mSelectedDateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_signlog);
        ButterKnife.bind(this);
        EventBus.getDefault().register(LessonSignlogActivity.this);

        setupView();
    }

    private void setupView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mAdapter = new LessonSignlogAdapter(LessonSignlogActivity.this);
        mLvSignlog.setAdapter(mAdapter);
        mLvSignlog.setGroupIndicator(null);

        mSignlogFilter.check(R.id.filter_signed);
        mSignlogFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.filter_signed:
                        filter(FILTER_TYPE_SIGNED);
                        break;
                    case R.id.filter_not_signed:
                        filter(FILTER_TYPE_NOT_SIGNED);
                        break;
                }
            }
        });
    }

    private void filter(int filter_type){
        switch (filter_type){
            case FILTER_TYPE_SIGNED: //显示签到，不包括请假的
                mAdapter.updateData(mStudentMap,mSignedList,mSelectedDateStr);
                mToolbarTitle.setText("课程考勤(已签到)");
                break;
            case FILTER_TYPE_NOT_SIGNED:
                mAdapter.updateData(mStudentMap,mSignedList,mLeaveList,mSelectedDateStr);
                mToolbarTitle.setText("课程考勤(缺勤)");
                break;
        }
        for (int i = 0; i < mAdapter.getGroupCount(); i++){
            mLvSignlog.expandGroup(i);
        }
    }

    @OnClick(R.id.fab_lesson_signlog)
    void OnFabClick(){
        mDatePickerDialog = new DatePickerDialog(LessonSignlogActivity.this);
        mDatePickerDialog.setOnDatePickedListener(new DatePickedListener() {
            @Override
            public void onDatePicked(int year, int month, int day_of_month) {
                mSelectedDateStr = DateUtil.getDateStr(year,month,day_of_month,"yyyyMMdd");
                getLessonSignlog(mLessonEvent.getLessonId(),year,month,day_of_month);
            }
        })
                .show(null);
    }

    private void getLessonSignlog(String classid,int year,int month, int day_of_month){
        Retrofit retrofit = RetrofitHelper.getRetrofit(LessonSignlogActivity.this);

        Observable.zip(retrofit.create(AssistantService.class).getLessonSignlog(classid, DateUtil.getDateStr(year, month, day_of_month, "yyyyMMdd")),
                retrofit.create(AssistantService.class).getLessonLeaveLog(classid, DateUtil.getDateStr(year, month, day_of_month, "yyyyMMdd")),
                new Func2<LessonSignlogResult, LeavelogResult, Boolean>() {
                    @Override
                    public Boolean call(LessonSignlogResult lessonSignlogResult, LeavelogResult leavelogResult) {
                        mSignedList = (ArrayList<LessonSignlog>) lessonSignlogResult.getSignlog();
                        mLeaveList = (ArrayList<Leavelog>) leavelogResult.getLeavelog();
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.show(LessonSignlogActivity.this,"请检查网络设置");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isSuccess) {
                        if (isSuccess){
                            L.d("获取课程签到记录成功");
                            mToolbarTitle.setText("课程考勤(已签到)");
                            mAdapter.updateData(mStudentMap,mSignedList,mSelectedDateStr);
                            mSignlogFilter.check(R.id.filter_signed);
                            mSignlogFilter.setVisibility(View.VISIBLE);
                            for (int i = 0; i < mAdapter.getGroupCount(); i++){
                                mLvSignlog.expandGroup(i);
                            }
                        }
                    }
                });
    }

    public void onEventMainThread(LessonEvent event){
        mLessonEvent = event;
        if (mStudentMap == null){
            mStudentMap = new HashMap<>();
            for (Student student : mLessonEvent.getStudentList()){
                mStudentMap.put(student.getName(),student);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(LessonSignlogActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }
}
