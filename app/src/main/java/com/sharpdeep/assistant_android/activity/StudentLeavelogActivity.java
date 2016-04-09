package com.sharpdeep.assistant_android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import com.baoyz.widget.PullRefreshLayout;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.adapter.StudentLeavelogAdapter;
import com.sharpdeep.assistant_android.api.AssistantService;
import com.sharpdeep.assistant_android.helper.DataCacher;
import com.sharpdeep.assistant_android.helper.RetrofitHelper;
import com.sharpdeep.assistant_android.model.resultModel.StudentLeavelog;
import com.sharpdeep.assistant_android.model.resultModel.StudentLeavelogResult;
import com.sharpdeep.assistant_android.util.L;
import com.sharpdeep.assistant_android.util.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by bear on 16-4-9.
 */
public class StudentLeavelogActivity extends AppCompatActivity {
    @Bind(R.id.pullrefresh_student_leavelog)
    PullRefreshLayout mRefreshControl;
    @Bind(R.id.listview_leavelog)
    ExpandableListView mLVLeavelog;
    @Bind(R.id.toolbar_student_leavelog)
    Toolbar mToolbar;

    private StudentLeavelogAdapter mleavelogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_leavelog);
        ButterKnife.bind(this);

        setupView();

        getStudentLeavelog();
    }

    private void setupView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mleavelogAdapter = new StudentLeavelogAdapter(StudentLeavelogActivity.this);
        mLVLeavelog.setAdapter(mleavelogAdapter);
        mLVLeavelog.setGroupIndicator(null);

        setupPullRefresh();

        mRefreshControl.setRefreshing(true);
    }

    private void setupPullRefresh() {
        mRefreshControl.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        mRefreshControl.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getStudentLeavelog();
            }
        });
    }

    private void getStudentLeavelog(){
        Retrofit retrofit = RetrofitHelper.getRetrofit(StudentLeavelogActivity.this);
        retrofit.create(AssistantService.class)
                .getStudentLeavelog(DataCacher.getInstance().getCurrentUser().getUsername(),"all")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StudentLeavelogResult>() {
                    @Override
                    public void onCompleted() {
                        mRefreshControl.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.d("刷新学生签到记录时候出错" + e.toString());
                        e.printStackTrace();
                        ToastUtil.show(StudentLeavelogActivity.this, "网络可能出了点问题");
                        mRefreshControl.setRefreshing(false);
                    }

                    @Override
                    public void onNext(StudentLeavelogResult studentLeavelogResult) {
                        mleavelogAdapter.updateData(studentLeavelogResult.getLeavelog());
                        for (int i = 0; i < mleavelogAdapter.getGroupCount(); i++){
                            mLVLeavelog.expandGroup(i);
                        }
                    }
                });
    }
}
