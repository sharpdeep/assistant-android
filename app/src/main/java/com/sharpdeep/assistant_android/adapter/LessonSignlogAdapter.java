package com.sharpdeep.assistant_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.model.resultModel.LessonSignlog;
import com.sharpdeep.assistant_android.model.resultModel.StudentSignlog;
import com.sharpdeep.assistant_android.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bear on 16-4-13.
 */
public class LessonSignlogAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<LessonSignlog> mSignlogs;
    private LayoutInflater mInflater;

    public LessonSignlogAdapter(Context context){
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mSignlogs = new ArrayList<>();
    }

    public void updateData(ArrayList<LessonSignlog> signlogs){
        mSignlogs = signlogs;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return (mSignlogs.size() == 0 ? 0 : 1);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mSignlogs.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mSignlogs;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mSignlogs.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_log_section,null);
            holder = new GroupViewHolder();
            holder.sectionName = (TextView) convertView.findViewById(R.id.txt_signlog_section);
            convertView.setTag(holder);
        }
        holder = (GroupViewHolder) convertView.getTag();
        String dateStr = mSignlogs.get(0).getDate();
        holder.sectionName.setText(dateStr+" "+ DateUtil.getWeekStrByDateStr(dateStr,"yyyyMMdd"));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_log_content,null);
            holder = new ChildViewHolder();
            holder.content = (TextView) convertView.findViewById(R.id.txt_signlog_content);
            convertView.setTag(holder);
        }
        holder = (ChildViewHolder) convertView.getTag();
        LessonSignlog log = mSignlogs.get(childPosition);
        holder.content.setText("学生id:"+log.getStudentId()+"\n姓名:"+log.getStudentName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    static class GroupViewHolder{
        TextView sectionName;
    }

    static class ChildViewHolder{
        TextView content;
    }
}
