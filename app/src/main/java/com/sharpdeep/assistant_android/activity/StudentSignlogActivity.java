package com.sharpdeep.assistant_android.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import com.avos.avoscloud.AVAnalytics;
import com.baoyz.widget.PullRefreshLayout;
import com.mikepenz.materialdrawer.Drawer;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.adapter.StudentSignlogAdapter;
import com.sharpdeep.assistant_android.api.AssistantService;
import com.sharpdeep.assistant_android.helper.DataCacher;
import com.sharpdeep.assistant_android.helper.DrawerHelper;
import com.sharpdeep.assistant_android.helper.RetrofitHelper;
import com.sharpdeep.assistant_android.model.resultModel.StudentSIgnlogResult;
import com.sharpdeep.assistant_android.model.resultModel.StudentSignlog;
import com.sharpdeep.assistant_android.util.L;
import com.sharpdeep.assistant_android.util.ToastUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StudentSignlogActivity extends AppCompatActivity {

    @Bind(R.id.pullrefresh_student_signlog)
    PullRefreshLayout mRefreshControl;
    @Bind(R.id.listview_signlog)
    ExpandableListView mLvSinglog;
    @Bind(R.id.toolbar_student_signlog)
    Toolbar mToolbar;

    private StudentSignlogAdapter mSignlogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signlog);
        ButterKnife.bind(this);

        setupView();

        getStudentSignlog();
    }

    private void setupView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSignlogAdapter = new StudentSignlogAdapter(StudentSignlogActivity.this);
        mLvSinglog.setAdapter(mSignlogAdapter);
        mLvSinglog.setGroupIndicator(null);

        setupPullRefresh();

        mRefreshControl.setRefreshing(true);
    }

    private void setupPullRefresh() {
        mRefreshControl.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        mRefreshControl.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getStudentSignlog();
            }
        });
    }

    private void getStudentSignlog(){
        Retrofit retrofit = RetrofitHelper.getRetrofit(this);
        retrofit.create(AssistantService.class)
                .getStudentSignlog(DataCacher.getInstance().getCurrentUser().getUsername(),"all")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StudentSIgnlogResult>() {
                    @Override
                    public void onCompleted() {
                        mRefreshControl.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.d("刷新学生签到记录时候出错"+e.toString());
                        e.printStackTrace();
                        ToastUtil.show(StudentSignlogActivity.this, "网络可能出了点问题");
                        mRefreshControl.setRefreshing(false);
                    }

                    @Override
                    public void onNext(StudentSIgnlogResult studentSIgnlogResult) {
                        mSignlogAdapter.updateData(studentSIgnlogResult.getSignlog());
                        for (int i = 0; i < mSignlogAdapter.getGroupCount(); i++){
                            mLvSinglog.expandGroup(i);
                        }
                    }
                });
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
