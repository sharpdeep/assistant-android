package com.sharpdeep.assistant_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.model.resultModel.Student;
import com.sharpdeep.assistant_android.model.resultModel.StudentSIgnlogResult;
import com.sharpdeep.assistant_android.model.resultModel.StudentSignlog;
import com.sharpdeep.assistant_android.util.L;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by bear on 16-4-8.
 */
public class StudentSignlogAdapter extends BaseExpandableListAdapter{
    private Context mContext;
    private ArrayList<ArrayList<StudentSignlog>> mSignlogs;
    private LayoutInflater mInflater;

    public StudentSignlogAdapter(Context context){
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mSignlogs = new ArrayList<>();
    }

    public void updateData(List<StudentSignlog> studentSignlogList){
        mSignlogs = handleStudentSignlogList(studentSignlogList);
        notifyDataSetChanged();
    }

    private ArrayList<ArrayList<StudentSignlog>> handleStudentSignlogList(List<StudentSignlog> studentSignlogList){
        //根据日期分组
        HashMap<String,ArrayList<StudentSignlog>> map = new HashMap<>();
        for (StudentSignlog log : studentSignlogList){
            if (map.containsKey(log.getDate())){
                map.get(log.getDate()).add(log);
            }else{
                ArrayList<StudentSignlog> list = new ArrayList<>();
                list.add(log);
                map.put(log.getDate(),list);
            }
        }
        //对HashMap根据key(Date)排序
        List<Map.Entry<String,ArrayList<StudentSignlog>>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, ArrayList<StudentSignlog>>>() {
            @Override
            public int compare(Map.Entry<String, ArrayList<StudentSignlog>> lhs, Map.Entry<String, ArrayList<StudentSignlog>> rhs) {
                return lhs.getKey().compareTo(rhs.getKey());
            }
        });

        ArrayList<ArrayList<StudentSignlog>> signlog = new ArrayList<>();
        //将分组排序好的数据再组装到signlogs中
        for (Map.Entry<String,ArrayList<StudentSignlog>> entry : list){
            signlog.add(entry.getValue());
        }

        return signlog;
    }

    @Override
    public int getGroupCount() {
        return mSignlogs.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mSignlogs.get(groupPosition) == null){
            return 0;
        }
        return mSignlogs.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mSignlogs.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (mSignlogs.get(groupPosition) == null){
            return null;
        }
        return mSignlogs.get(groupPosition).get(childPosition);
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
            convertView = mInflater.inflate(R.layout.item_student_signlog_section,null);
            holder = new GroupViewHolder();
            holder.sectionName = (TextView) convertView.findViewById(R.id.txt_signlog_section);
            convertView.setTag(holder);
        }
        holder = (GroupViewHolder) convertView.getTag();
        holder.sectionName.setText(mSignlogs.get(groupPosition).get(0).getDate());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_student_signlog_content,null);
            holder = new ChildViewHolder();
            holder.content = (TextView) convertView.findViewById(R.id.txt_signlog_content);
            convertView.setTag(holder);
        }
        holder = (ChildViewHolder) convertView.getTag();
        StudentSignlog log = mSignlogs.get(groupPosition).get(childPosition);
        holder.content.setText("课程号:"+log.getLessonId()+"\n课程名:"+log.getLessonName());
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
