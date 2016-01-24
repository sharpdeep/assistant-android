package com.sharpdeep.assistant_android.activity.fragment;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.model.bean.DiscussionMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by bear on 16-1-23.
 */
public class LessonDiscussionFragment extends Fragment {
    @Bind(R.id.listView_lessondiscussion)
    RecyclerView mViewDiscussionMsgList;
    @Bind(R.id.fab_add_discussion)
    FloatingActionButton mFabAddDiscussion;

    private ArrayList<DiscussionMessage> mLastestMsg;
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
        getLastestMsg();
        mViewDiscussionMsgList.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewDiscussionMsgList.setHasFixedSize(true);

        mAdapter = new RecyclerViewMaterialAdapter(new DiscussionMsgListAdapter(mLastestMsg));
        mViewDiscussionMsgList.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mViewDiscussionMsgList, null);
    }

    @OnClick(R.id.fab_add_discussion)
    public void onAddDiscussion(View view){
        mFabAddDiscussion.hide();
        setupAddDiscussionDialog();
    }

    private void setupAddDiscussionDialog() {
        final MaterialDialog addDiscussionDialog = new MaterialDialog(getContext());
        final EditText editText = new EditText(getContext());
        editText.setLines(5);
        editText.setTextColor(Color.BLACK);
        editText.setHint("share your idea");
        addDiscussionDialog.setContentView(editText)
                .setPositiveButton("send", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addDiscussionDialog.dismiss();
                        String msg = editText.getText().toString();
                        if (TextUtils.isEmpty(msg)){
                            return;
                        }
                        sendMessage(msg);
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


    private void sendMessage(String message) {
        DiscussionMessage msg = new DiscussionMessage(message,true);
        mLastestMsg.add(0,msg);
        mAdapter.notifyDataSetChanged();
    }

    private void getLastestMsg(){
        //获取最新的信息
        mLastestMsg = new ArrayList<>();
    }


    class DiscussionMsgListAdapter extends RecyclerView.Adapter<MsgItemHolder>{

        private List<DiscussionMessage> messages;

        public DiscussionMsgListAdapter(List<DiscussionMessage> messages) {
            this.messages = messages;
        }

        @Override
        public MsgItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_lessondiscussion_msg,parent,false);
            return new MsgItemHolder(view);
        }

        @Override
        public void onBindViewHolder(MsgItemHolder holder, int position) {
            holder.msg.setText(messages.get(position).getContent());
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

    }

    class MsgItemHolder extends RecyclerView.ViewHolder{
        public TextView msg;

        public MsgItemHolder(View itemView) {
            super(itemView);
            msg = (TextView) itemView.findViewById(R.id.textview_lessondiscussion_msg);
        }
    }


}
