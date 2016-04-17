package com.sharpdeep.assistant_android.activity.fragment.lessonpage;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.api.AssistantService;
import com.sharpdeep.assistant_android.helper.Constant;
import com.sharpdeep.assistant_android.helper.DataCacher;
import com.sharpdeep.assistant_android.helper.RetrofitHelper;
import com.sharpdeep.assistant_android.model.bean.DiscussionMessage;
import com.sharpdeep.assistant_android.model.resultModel.BaseResult;
import com.sharpdeep.assistant_android.model.resultModel.Discussion;
import com.sharpdeep.assistant_android.model.resultModel.DiscussionResult;
import com.sharpdeep.assistant_android.model.resultModel.Schedule;
import com.sharpdeep.assistant_android.util.ToastUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;
import retrofit.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by bear on 16-1-23.
 */
public class LessonDiscussionFragment extends LessonPageBaseFragment {
    @Bind(R.id.listView_lessondiscussion)
    RecyclerView mViewDiscussionMsgList;
    @Bind(R.id.fab_add_discussion)
    FloatingActionButton mFabAddDiscussion;
    @Bind(R.id.lesson_discussion_refresh_controler)
    PullRefreshLayout mRefreshControler;

    private ArrayList<Discussion> mDiscussionList = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lessondiscussion,container,false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    private void setupView() {
        mViewDiscussionMsgList.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewDiscussionMsgList.setHasFixedSize(true);

        mAdapter = new RecyclerViewMaterialAdapter(new DiscussionMsgListAdapter(mDiscussionList));
        mViewDiscussionMsgList.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mViewDiscussionMsgList, null);

        mFabAddDiscussion.attachToRecyclerView(mViewDiscussionMsgList);

//        mRefreshControler.setRefreshing(true);
        syncDiscussion();

//        mRefreshControler.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                syncDiscussion();
//            }
//        });

//        syncDiscussion();
    }

    @OnClick(R.id.fab_add_discussion)
    public void onAddDiscussion(View view){
        mFabAddDiscussion.hide();
        setupAddDiscussionDialog();
    }

    private void setupAddDiscussionDialog() {
        final MaterialDialog addDiscussionDialog = new MaterialDialog(getContext());
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.item_make_discussion_dialog,null);
        final EditText editText = (EditText) contentView.findViewById(R.id.dialog_discussion_content);
        final CheckBox checkBoxAnonymous = (CheckBox) contentView.findViewById(R.id.checkbox_discussion_with_anonymous);

        addDiscussionDialog.setContentView(contentView)
                .setTitle("发表讨论")
                .setPositiveButton("send", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String msg = editText.getText().toString();
                        if (TextUtils.isEmpty(msg)){
                            editText.setError("讨论内容不能为空");
                            return;
                        }
                        addDiscussionDialog.dismiss();
                        if (checkBoxAnonymous.isChecked()){
                            sendMessage(Constant.ANONYMOUS_NAME,msg);
                        }else{
                            sendMessage(DataCacher.getInstance().getCurrentUser().getUsername(),msg);
                        }
                    }
                })
                .setCanceledOnTouchOutside(true)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mFabAddDiscussion.show();
                    }
                });

        addDiscussionDialog.show();
    }


    private void sendMessage(final String fromUserName, final String message) {
        Retrofit retrofit = RetrofitHelper.getRetrofit(getActivity());
        retrofit.create(AssistantService.class)
                .makeLessonDiscussion(getLessonId(),fromUserName,message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(getActivity(),"请检查网络设置");
                    }

                    @Override
                    public void onNext(BaseResult baseResult) {
                        if (baseResult.isSuccess()){
                            ToastUtil.show(getActivity(),"发表成功");
                            Discussion discussion = new Discussion(fromUserName,getLessonId(),message);
                            mDiscussionList.add(discussion);
                            mAdapter.notifyDataSetChanged();
                        }else{
                            ToastUtil.show(getActivity(),baseResult.getMsg());
                        }
                    }
                });

    }

    private void syncDiscussion(){
        //获取最新的信息
        Retrofit retrofit = RetrofitHelper.getRetrofit(getActivity());
        retrofit.create(AssistantService.class)
                .getLessonDiscussionAfter(getLessonId(),getCacheDiscussionSize())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DiscussionResult>() {
                    @Override
                    public void onCompleted() {
                        mRefreshControler.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(getActivity(),"请检查网络设置");
                        mRefreshControler.setRefreshing(false);
                    }

                    @Override
                    public void onNext(DiscussionResult discussionResult) {
                        if (discussionResult.isSuccess()){
                            mDiscussionList.addAll(discussionResult.getDiscussionList());
                            mAdapter.notifyDataSetChanged();
                        }else {
                            ToastUtil.show(getActivity(),"同步讨论失败");
                        }
                    }
                });
    }

    public int getCacheDiscussionSize(){
        return 0;
    }


    class DiscussionMsgListAdapter extends RecyclerView.Adapter<MsgItemHolder>{

        private List<Discussion> discussionList;

        public DiscussionMsgListAdapter(List<Discussion> discussionList) {
            this.discussionList = discussionList;
        }

        @Override
        public MsgItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_lessondiscussion_msg,parent,false);
            return new MsgItemHolder(view);
        }

        @Override
        public void onBindViewHolder(MsgItemHolder holder, int position) {
            Discussion discussion = discussionList.get(discussionList.size()-1-position);
            holder.fromUserName.setText(discussion.getFromUserName());
            holder.content.setText(discussion.getContent());
            holder.time.setText(discussion.getCreateTime());
        }

        @Override
        public int getItemCount() {
            return discussionList.size();
        }

    }

    class MsgItemHolder extends RecyclerView.ViewHolder{
        public TextView content;
        public TextView fromUserName;
        public TextView time;

        public MsgItemHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.textview_lessondiscussion_msg);
            fromUserName = (TextView) itemView.findViewById(R.id.lesson_discussion_fromUserName);
            time = (TextView) itemView.findViewById(R.id.lesson_discussion_time);
        }
    }

}
