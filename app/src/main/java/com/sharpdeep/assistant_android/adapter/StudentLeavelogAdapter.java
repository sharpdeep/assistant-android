package com.sharpdeep.assistant_android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.helper.Constant;
import com.sharpdeep.assistant_android.model.resultModel.Leavelog;
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
    private ArrayList<ArrayList<Leavelog>> mLeavelogs;
    private LayoutInflater mInflater;

    public StudentLeavelogAdapter(Context context){
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLeavelogs = new ArrayList<>();
    }

    public void updateData(List<Leavelog> studentLeavelogList){
        mLeavelogs = handleStudentLeavelogList(studentLeavelogList);
        notifyDataSetChanged();
    }

    private ArrayList<ArrayList<Leavelog>> handleStudentLeavelogList(List<Leavelog> studentLeavelogList){
        //根据日期分组
        HashMap<String,ArrayList<Leavelog>> map = new HashMap<>();
        for (Leavelog log : studentLeavelogList){
            if (map.containsKey(log.getLeaveDate())){
                map.get(log.getLeaveDate()).add(log);
            }else{
                ArrayList<Leavelog> list = new ArrayList<>();
                list.add(log);
                map.put(log.getLeaveDate(),list);
            }
        }
        //对HashMap根据key(Date)排序
        List<Map.Entry<String,ArrayList<Leavelog>>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, ArrayList<Leavelog>>>() {
            @Override
            public int compare(Map.Entry<String, ArrayList<Leavelog>> lhs, Map.Entry<String, ArrayList<Leavelog>> rhs) {
                return lhs.getKey().compareTo(rhs.getKey());
            }
        });

        ArrayList<ArrayList<Leavelog>> signlog = new ArrayList<>();
        //将分组排序好的数据再组装到signlogs中
        for (Map.Entry<String,ArrayList<Leavelog>> entry : list){
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
        String dateStr = mLeavelogs.get(groupPosition).get(0).getLeaveDate();
        holder.sectionName.setText(dateStr+" "+ DateUtil.getWeekStrByDateStr(dateStr, "yyyyMMdd"));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_log_content,null);
            holder = new ChildViewHolder();
            holder.content = (TextView) convertView.findViewById(R.id.txt_signlog_content);
            holder.divider = convertView.findViewById(R.id.log_content_divider);
            holder.status = (TextView) convertView.findViewById(R.id.log_content_status);
            convertView.setTag(holder);
        }
        holder = (ChildViewHolder) convertView.getTag();
        Leavelog log = mLeavelogs.get(groupPosition).get(childPosition);
        holder.content.setText(
                        "课程号:\t"+log.getClassid()+
                        "\n课程名\t:"+log.getClassname()+
                        "\n\n请假人:\t"+log.getStudentname()+
                        "\n请假人id:\t"+log.getStudentid()+
                        "\n请假类型:\t"+Constant.getLeaveTypeName(log.getLeaveType())+
                        "\n请假时间:\t"+log.getLeaveDate()+DateUtil.getWeekStrByDateStr(log.getLeaveDate(),"yyyyMMdd")+
                        "\n请假原因:\t"+log.getLeaveReason()

        );
        holder.divider.setVisibility(View.VISIBLE);
        if (log.getVerify()){
            holder.status.setText("已同意");
            holder.status.setTextColor(Color.GREEN);
        }else{
            holder.status.setText("未同意");
            holder.status.setTextColor(Color.RED);
        }
        holder.status.setVisibility(View.VISIBLE);
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
        View divider;
        TextView status;
    }
}
