package com.sharpdeep.assistant_android.activity;

import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.adapter.LessonSignlogAdapter;
import com.sharpdeep.assistant_android.api.AssistantService;
import com.sharpdeep.assistant_android.helper.RetrofitHelper;
import com.sharpdeep.assistant_android.listener.DatePickedListener;
import com.sharpdeep.assistant_android.model.eventModel.LessonEvent;
import com.sharpdeep.assistant_android.model.resultModel.LessonSignlog;
import com.sharpdeep.assistant_android.model.resultModel.LessonSignlogResult;
import com.sharpdeep.assistant_android.util.DateUtil;
import com.sharpdeep.assistant_android.util.L;
import com.sharpdeep.assistant_android.util.ToastUtil;
import com.sharpdeep.assistant_android.view.DatePickerDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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

    private int mFilterType; //0->显示已签到;1->显示缺勤
    final static int FILTER_TYPE_SIGNED = 0;
    final static int FILTER_TYPE_NOT_SIGNED = 1;

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
                        mFilterType = FILTER_TYPE_SIGNED;
                        break;
                    case R.id.filter_not_signed:
                        mFilterType = FILTER_TYPE_NOT_SIGNED;
                        break;
                }
            }
        });
    }

    @OnClick(R.id.fab_lesson_signlog)
    void OnFabClick(){
        mDatePickerDialog = new DatePickerDialog(LessonSignlogActivity.this);
        mDatePickerDialog.setOnDatePickedListener(new DatePickedListener() {
            @Override
            public void onDatePicked(int year, int month, int day_of_month) {
                getLessonSignlog(mLessonEvent.getLessonId(),year,month,day_of_month);
            }
        })
                .show(null);
    }

    private void getLessonSignlog(String classid,int year,int month, int day_of_month){
        Retrofit retrofit = RetrofitHelper.getRetrofit(LessonSignlogActivity.this);
        retrofit.create(AssistantService.class)
                .getLessonSignlog(classid,DateUtil.getDateStr(year,month,day_of_month,"yyyyMMdd"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LessonSignlogResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(LessonSignlogActivity.this,"请检查网络设置");
                    }

                    @Override
                    public void onNext(LessonSignlogResult lessonSignlogResult) {
                        if (lessonSignlogResult.isSuccess()){
                            L.d("获取课程签到记录成功");
                            mSignlogFilter.setVisibility(View.VISIBLE);
                            mAdapter.updateData((ArrayList<LessonSignlog>) lessonSignlogResult.getSignlog());
                            for (int i = 0; i < mAdapter.getGroupCount(); i++){
                                mLvSignlog.expandGroup(i);
                            }
                            return;
                        }
                        ToastUtil.show(LessonSignlogActivity.this,lessonSignlogResult.getMsg());
                    }
                });
    }

    public void onEvent(LessonEvent event){
        mLessonEvent = event;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(LessonSignlogActivity.this);
    }
}
