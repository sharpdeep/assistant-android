package com.sharpdeep.assistant_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.helper.Constant;
import com.sharpdeep.assistant_android.model.resultModel.StudentLeavelog;
import com.sharpdeep.assistant_android.model.resultModel.StudentSignlog;
import com.sharpdeep.assistant_android.util.DateUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bear on 16-4-9.
 */
public class StudentLeavelogAdapter<T> extends BaseExpandableListAdapter{
    private Context mContext;
    private ArrayList<ArrayList<StudentLeavelog>> mLeavelogs;
    private LayoutInflater mInflater;

    public StudentLeavelogAdapter(Context context){
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLeavelogs = new ArrayList<>();
    }

    public void updateData(List<StudentLeavelog> studentLeavelogList){
        mLeavelogs = handleStudentLeavelogList(studentLeavelogList);
        notifyDataSetChanged();
    }

    private ArrayList<ArrayList<StudentLeavelog>> handleStudentLeavelogList(List<StudentLeavelog> studentLeavelogList){
        //根据日期分组
        HashMap<String,ArrayList<StudentLeavelog>> map = new HashMap<>();
        for (StudentLeavelog log : studentLeavelogList){
            if (map.containsKey(log.getDate())){
                map.get(log.getDate()).add(log);
            }else{
                ArrayList<StudentLeavelog> list = new ArrayList<>();
                list.add(log);
                map.put(log.getDate(),list);
            }
        }
        //对HashMap根据key(Date)排序
        List<Map.Entry<String,ArrayList<StudentLeavelog>>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, ArrayList<StudentLeavelog>>>() {
            @Override
            public int compare(Map.Entry<String, ArrayList<StudentLeavelog>> lhs, Map.Entry<String, ArrayList<StudentLeavelog>> rhs) {
                return lhs.getKey().compareTo(rhs.getKey());
            }
        });

        ArrayList<ArrayList<StudentLeavelog>> signlog = new ArrayList<>();
        //将分组排序好的数据再组装到signlogs中
        for (Map.Entry<String,ArrayList<StudentLeavelog>> entry : list){
            signlog.add(entry.getValue());
        }

        return signlog;
    }

    @Override
    public int getGroupCount() {
        return mLeavelogs.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mLeavelogs.get(groupPosition) == null){
            return 0;
        }
        return mLeavelogs.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mLeavelogs.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (mLeavelogs.get(groupPosition) == null){
            return null;
        }
        return mLeavelogs.get(groupPosition).get(childPosition);
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
        String dateStr = mLeavelogs.get(groupPosition).get(0).getDate();
        holder.sectionName.setText(dateStr+" "+ DateUtil.getWeekStrByDateStr(dateStr, "yyyyMMdd"));
        return convertView;    }

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
        StudentLeavelog log = mLeavelogs.get(groupPosition).get(childPosition);
        holder.content.setText(
                        "课程号:\t"+log.getClassid()+
                        "\n课程名\t:"+log.getClassname()+
                        "\n\n请假人:\t"+log.getStudentname()+
                        "\n请假人id:\t"+log.getStudentid()+
                        "\n请假类型:\t"+Constant.getLeaveTypeName(log.getLeaveType())+
                        "\n请假时间:\t"+log.getLeaveDate()+DateUtil.getWeekStrByDateStr(log.getLeaveDate(),"yyyyMMdd")+
                        "\n请假原因:\t"+log.getLeaveReason()

        );
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