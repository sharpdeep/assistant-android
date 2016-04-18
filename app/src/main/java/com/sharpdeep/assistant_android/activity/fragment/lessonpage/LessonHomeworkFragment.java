package com.sharpdeep.assistant_android.activity.fragment.lessonpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.adapter.HomeworkListAdapter;
import com.sharpdeep.assistant_android.api.AssistantService;
import com.sharpdeep.assistant_android.helper.Constant;
import com.sharpdeep.assistant_android.helper.DataCacher;
import com.sharpdeep.assistant_android.helper.RetrofitHelper;
import com.sharpdeep.assistant_android.listener.HomeworkEditedListener;
import com.sharpdeep.assistant_android.model.resultModel.BaseResult;
import com.sharpdeep.assistant_android.model.resultModel.Homework;
import com.sharpdeep.assistant_android.model.resultModel.HomeworkResult;
import com.sharpdeep.assistant_android.model.resultModel.Schedule;
import com.sharpdeep.assistant_android.util.L;
import com.sharpdeep.assistant_android.util.ProjectUtil;
import com.sharpdeep.assistant_android.util.ToastUtil;
import com.sharpdeep.assistant_android.view.HomeworkMakerDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by bear on 16-4-18.
 */
public class LessonHomeworkFragment extends LessonPageBaseFragment {
    @Bind(R.id.listView_lesson_homework)
    RecyclerView mLvHomeworkList;
    @Bind(R.id.fab_add_homework)
    FloatingActionButton mFab;
    @Bind(R.id.lesson_homework_refresh_controler)
    PullRefreshLayout mRefreshControler;

    private RecyclerView.Adapter mHomeworkListAdapter;
    private List<Homework> mHomeworkList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_homework,container,false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    private void setupView() {
        mLvHomeworkList.setLayoutManager(new LinearLayoutManager(getContext()));
        mLvHomeworkList.setHasFixedSize(true);

        mHomeworkListAdapter = new RecyclerViewMaterialAdapter(new HomeworkListAdapter(mHomeworkList));
        mLvHomeworkList.setAdapter(mHomeworkListAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mLvHomeworkList, null);

        mFab.attachToRecyclerView(mLvHomeworkList);

        if (ProjectUtil.isStudent(DataCacher.getInstance().getIdentify())){
            mFab.setVisibility(View.GONE);
        }

        showCacheHomework();
        mRefreshControler.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                syncHomework();
            }
        });
    }

    @OnClick(R.id.fab_add_homework)
    void onFabClick(){
        HomeworkMakerDialog dialog = new HomeworkMakerDialog(getContext());
        dialog.setOnHomeworkEditedListener(new HomeworkEditedListener() {
            @Override
            public void onHomeworkEdited(String title, String content, String deadline) {
                makeHomework(title,content,deadline);
            }
        });
        dialog.show();
    }

    private void syncHomework(){
        Retrofit retrofit = RetrofitHelper.getRetrofit(getContext());
        retrofit.create(AssistantService.class)
                .getHomeworkAfter(getLessonId(),getCacheHomeworkSize())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<HomeworkResult, Boolean>() {
                    @Override
                    public Boolean call(HomeworkResult homeworkResult) {
                        if (homeworkResult.isSuccess() && !homeworkResult.getHomeworkList().isEmpty()){
                            //有新数据，缓存
                            for (Homework homework : homeworkResult.getHomeworkList()){
                                com.sharpdeep.assistant_android.model.dbModel.Homework dbHomework = new com.sharpdeep.assistant_android.model.dbModel.Homework();
                                dbHomework.saveToDB(homework);
                            }
                            mHomeworkList.addAll(homeworkResult.getHomeworkList());
                            return true;
                        }
                        return false;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        mRefreshControler.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(getActivity(),"请检查网络设置");
                        L.d(e.toString());
                        mRefreshControler.setRefreshing(false);
                    }

                    @Override
                    public void onNext(Boolean needRefresh) {
                        if (needRefresh){
                            mHomeworkListAdapter.notifyDataSetChanged();
                        }else{
                            ToastUtil.show(getActivity(),"没有新数据");
                        }
                    }
                });
    }

    private int getCacheHomeworkSize(){
        return mHomeworkList.size();
    }

    private void showCacheHomework(){
        Observable.create(new Observable.OnSubscribe<Homework>() {
            @Override
            public void call(Subscriber<? super Homework> subscriber) {
                List<com.sharpdeep.assistant_android.model.dbModel.Homework> dbHomeworkList = com.sharpdeep.assistant_android.model.dbModel.Homework.find(
                        com.sharpdeep.assistant_android.model.dbModel.Homework.class,
                        "to_user_name = ?",
                        getLessonId()
                );
                for (com.sharpdeep.assistant_android.model.dbModel.Homework dbHomework : dbHomeworkList){
                    subscriber.onNext(dbHomework.loadHomework());
                }
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Homework>() {
                    @Override
                    public void onCompleted() {
                        mHomeworkListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(getActivity(),"读取作业数据库失败");
                    }

                    @Override
                    public void onNext(Homework homework) {
                        mHomeworkList.add(homework);
                    }
                });
    }

    private void makeHomework(final String title, final String content, final String deadline) {
        Retrofit retrofit = RetrofitHelper.getRetrofit(getContext());
        retrofit.create(AssistantService.class)
                .makeHomework(DataCacher.getInstance().getToken(),getLessonId(),title,content,deadline)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<BaseResult, Homework>() {
                    @Override
                    public Homework call(BaseResult baseResult) {
                        if (baseResult.isSuccess()){
                            //发布成功,本地保存
                            Homework homework = new Homework(DataCacher.getInstance().getCurrentUser().getUsername(),getLessonId(),title,content,deadline);
                            com.sharpdeep.assistant_android.model.dbModel.Homework dbHomework = new com.sharpdeep.assistant_android.model.dbModel.Homework();
                            dbHomework.saveToDB(homework);
                            return homework;
                        }
                        ToastUtil.show(getActivity(),baseResult.getMsg());
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Homework>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(getActivity(),"请检查网络设置");
                    }

                    @Override
                    public void onNext(Homework homework) {
                        if (homework != null){
                            //本地显示
                            ToastUtil.show(getActivity(),"发表成功");
                            mHomeworkList.add(homework);
                            mHomeworkListAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

}
