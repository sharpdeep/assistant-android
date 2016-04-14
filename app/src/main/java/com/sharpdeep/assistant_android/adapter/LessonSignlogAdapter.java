package com.sharpdeep.assistant_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.model.resultModel.Leavelog;
import com.sharpdeep.assistant_android.model.resultModel.LessonSignlog;
import com.sharpdeep.assistant_android.model.resultModel.Student;
import com.sharpdeep.assistant_android.model.resultModel.StudentSignlog;
import com.sharpdeep.assistant_android.util.DateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bear on 16-4-13.
 */
public class LessonSignlogAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private String mDateStr;
    private LayoutInflater mInflater;

    private ArrayList<Student> mStudents;

    public LessonSignlogAdapter(Context context){
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mStudents = new ArrayList<>();
    }

    //用来更新签到名单
    public void updateData(HashMap<String,Student> studentMap,ArrayList<LessonSignlog> signlogs,String dateStr){
        HashMap map = new HashMap();
        for (LessonSignlog log : signlogs){
            map.put(log.getStudentName(),studentMap.get(log.getStudentName()));
        }
        updateData(map,dateStr);
    }

    //更新缺勤名单
    public void updateData(HashMap<String,Student> studentMap, ArrayList<LessonSignlog> signlogs, ArrayList<Leavelog> leavelogs,String dateStr){
        HashMap<String,Student> map = (HashMap<String, Student>) studentMap.clone();
        for (LessonSignlog log : signlogs){
            map.remove(log.getStudentName());
        }
        for (Leavelog log : leavelogs){
            map.remove(log.getStudentname());
        }
        updateData(map,dateStr);
    }

    public void updateData(HashMap<String,Student> studentMap,String dateStr){
        mStudents.clear();
        if (studentMap.size() != 0){
            for (Map.Entry<String,Student> entry : studentMap.entrySet()){
                mStudents.add(entry.getValue());
            }
        }
        mDateStr = dateStr;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return (mStudents.size() == 0 ? 0 : 1);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mStudents.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mStudents;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mStudents.get(childPosition);
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
        holder.sectionName.setText(mDateStr+" "+ DateUtil.getWeekStrByDateStr(mDateStr,"yyyyMMdd"));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_studentlist,null);
            holder = new ChildViewHolder();
            holder.studentName = (TextView) convertView.findViewById(R.id.textview_studentlist_studentname);
            holder.studentId = (TextView) convertView.findViewById(R.id.textview_studentlist_studentid);
            holder.studentMajor = (TextView) convertView.findViewById(R.id.textview_studentlist_studentmajor);
            holder.avator = (CircleImageView) convertView.findViewById(R.id.imageview_studentlist_studentavator);
            convertView.setTag(holder);
        }
        holder = (ChildViewHolder) convertView.getTag();
        Student student = mStudents.get(childPosition);
        holder.studentName.setText(student.getName());
        holder.studentId.setText(student.getId());
        holder.studentMajor.setText(student.getMajor());
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
        CircleImageView avator;
        TextView studentName;
        TextView studentId;
        TextView studentMajor;
//        TextView content;
    }
}
