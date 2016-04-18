package com.sharpdeep.assistant_android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.model.resultModel.Homework;
import com.sharpdeep.assistant_android.util.DateUtil;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by bear on 16-4-18.
 */
public class HomeworkListAdapter extends RecyclerView.Adapter<HomeworkViewHolder>{
    private List<Homework> mHomeworkList;

    public HomeworkListAdapter(List<Homework> homeworkList){
        this.mHomeworkList = homeworkList;
    }

    @Override
    public HomeworkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homework_card,parent,false);
        return new HomeworkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeworkViewHolder holder, int position) {
        Homework homework = this.mHomeworkList.get(mHomeworkList.size()-1-position);
        holder.title.setText(homework.getTitle());
        holder.content.setText(homework.getContent());
        holder.deadline.setText("截止时间 : "+DateUtil.convertDateStrFormat(homework.getDeadline(),"yyyyMMdd","yyyy年MM月dd日 "+DateUtil.getWeekStrByDateStr(homework.getDeadline(),"yyyyMMdd")));
        holder.time.setText(homework.getCreateTime());

    }

    @Override
    public int getItemCount() {
        return mHomeworkList.size();
    }
}

class HomeworkViewHolder extends RecyclerView.ViewHolder{
    public TextView title;
    public TextView content;
    public TextView deadline;
    public TextView time;

    public HomeworkViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.lesson_homework_title);
        content = (TextView) itemView.findViewById(R.id.lesson_homework_content);
        deadline = (TextView) itemView.findViewById(R.id.lesson_homework_deadline);
        time = (TextView) itemView.findViewById(R.id.lesson_homework_time);
    }
}
