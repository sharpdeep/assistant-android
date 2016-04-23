package com.sharpdeep.assistant_android.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.baoyz.widget.PullRefreshLayout;
import com.melnykov.fab.FloatingActionButton;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.adapter.LessonLeavelogAdapter;
import com.sharpdeep.assistant_android.api.AssistantClient;
import com.sharpdeep.assistant_android.helper.Constant;
import com.sharpdeep.assistant_android.helper.DataCacher;
import com.sharpdeep.assistant_android.model.eventModel.LessonEvent;
import com.sharpdeep.assistant_android.model.resultModel.BaseResult;
import com.sharpdeep.assistant_android.model.resultModel.Leavelog;
import com.sharpdeep.assistant_android.model.resultModel.LeavelogResult;
import com.sharpdeep.assistant_android.model.resultModel.Student;
import com.sharpdeep.assistant_android.util.DateUtil;
import com.sharpdeep.assistant_android.util.ProjectUtil;
import com.sharpdeep.assistant_android.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.drakeet.materialdialog.MaterialDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by bear on 16-4-21.
 */
public class LessonLeavelogActivity extends AppCompatActivity {
    @Bind(R.id.listview_lesson_leavelog)
    ExpandableListView mLvLeavelog;
    @Bind(R.id.toolbar_lesson_leavelog)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_title_lesson_leavelog)
    TextView mToolbarTitle;
    @Bind(R.id.lesson_leavelog_filter)
    RadioGroup mLeavelogFilter;
    @Bind(R.id.pullrefresh_lesson_leavelog)
    PullRefreshLayout mRefreshControler;

    final static int FILTER_TYPE_ALL = 0;
    final static int FILTER_TYPE_NOT_VERIFY = 1;

    private String mLessonId;
    private LessonLeavelogAdapter mAdapter;
    private List<Leavelog> mLeavelogs;
    private List<Leavelog> mNotVerifyLeavelogs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_leavelog);
        ButterKnife.bind(this);
        Bundle bundle = this.getIntent().getExtras();
        mLessonId = bundle.getString("lessonid");
        mLeavelogs = new ArrayList<>();
        mNotVerifyLeavelogs = new ArrayList<>();

        setupView();
    }

    private void setupView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mAdapter = new LessonLeavelogAdapter(this);
        mLvLeavelog.setAdapter(mAdapter);
        mLvLeavelog.setGroupIndicator(null);

        mLeavelogFilter.check(R.id.filter_all);
        mLeavelogFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.filter_all:
                        updateListView(FILTER_TYPE_ALL);
                        break;
                    case R.id.filter_only_not_verify:
                        updateListView(FILTER_TYPE_NOT_VERIFY);
                        break;
                }
            }
        });

        getLessonLeavelog();
        mRefreshControler.setRefreshing(true);
        mRefreshControler.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLessonLeavelog();
            }
        });

        mLvLeavelog.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Leavelog log = (Leavelog) mAdapter.getChild(groupPosition,childPosition);
                showVerifyDialog(log);
                return false;
            }
        });
    }

    private void showVerifyDialog(final Leavelog log) {
        final MaterialDialog dialog = new MaterialDialog(LessonLeavelogActivity.this);
        dialog.setTitle("管理请假请求")
                .setMessage(
                                "是否同意请求:"+"\n\n"+
                                "课程号:\t"+log.getClassid()+
                                "\n课程名\t:"+log.getClassname()+
                                "\n\n请假人:\t"+log.getStudentname()+
                                "\n请假人id:\t"+log.getStudentid()+
                                "\n请假类型:\t"+ Constant.getLeaveTypeName(log.getLeaveType())+
                                "\n请假时间:\t"+log.getLeaveDate()+ DateUtil.getWeekStrByDateStr(log.getLeaveDate(),"yyyyMMdd")+
                                "\n请假原因:\t"+log.getLeaveReason()
                )
                .setPositiveButton("同意", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!log.getVerify()){
                            verifyLeave(log,true);
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("不同意", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (log.getVerify()){
                            verifyLeave(log,false);
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void verifyLeave(Leavelog log, boolean mVerify) {
        int verify = (mVerify ? 1 : 0);
        AssistantClient.getServiceInstance()
                .verifyLeave(DataCacher.getInstance().getToken(),ProjectUtil.genLeaveId(log),verify)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(LessonLeavelogActivity.this,"请检查网络设置");
                    }

                    @Override
                    public void onNext(BaseResult baseResult) {
                        if (baseResult.isSuccess()){
                            ToastUtil.show(LessonLeavelogActivity.this,"成功修改请假状态");
                            getLessonLeavelog();
                        }else{
                            ToastUtil.show(LessonLeavelogActivity.this,baseResult.getMsg());
                        }
                    }
                });
    }

    private void getLessonLeavelog(){
        AssistantClient.getServiceInstance()
                .getLessonLeaveLog(mLessonId,"all")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LeavelogResult>() {
                    @Override
                    public void onCompleted() {
                        mRefreshControler.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(LessonLeavelogActivity.this,"请检查网络设置");
                        mRefreshControler.setRefreshing(false);
                    }

                    @Override
                    public void onNext(LeavelogResult leavelogResult) {
                        if (leavelogResult.isSuccess()){
                            updateData(leavelogResult.getLeavelog());
                            mLeavelogFilter.check(R.id.filter_all);
                            updateListView(FILTER_TYPE_ALL);
                        }else{
                            ToastUtil.show(LessonLeavelogActivity.this,leavelogResult.getMsg());
                        }
                    }
                });
    }

    private void updateData(List<Leavelog> logs){
        mLeavelogs = logs;
        mNotVerifyLeavelogs.clear();
        for (Leavelog log : logs){
            if (!log.getVerify()){
                mNotVerifyLeavelogs.add(log);
            }
        }
    }

    private void updateListView(int filter_type){
        switch (filter_type){
            case FILTER_TYPE_ALL:
                mAdapter.updateData(mLeavelogs);
                mToolbarTitle.setText("请假管理(全部)");
                break;
            case FILTER_TYPE_NOT_VERIFY:
                mAdapter.updateData(mNotVerifyLeavelogs);
                mToolbarTitle.setText("请假管理(仅未同意)");
                break;
        }
        for (int i = 0; i < mAdapter.getGroupCount(); i++){
            mLvLeavelog.expandGroup(i);
        }
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
