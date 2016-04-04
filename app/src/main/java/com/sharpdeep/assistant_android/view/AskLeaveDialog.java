package com.sharpdeep.assistant_android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.widget.curved.WheelDatePicker;
import com.hanks.htextview.HTextView;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.helper.Constant;
import com.sharpdeep.assistant_android.util.L;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.SimpleFormatter;
import java.util.zip.Inflater;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by bear on 16-4-3.
 */
public class AskLeaveDialog {
    private MaterialDialog mDialog;
    private Context mContext;
    private int mLeaveType;
    private String mLeaveDate;
    private String mLeaveReason;
    private OnLeaveAskCompleteListener mListener;

    public AskLeaveDialog(Context context){
        this.mContext = context;
    }

    public void show(){
        mDialog = new MaterialDialog(this.mContext);
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.item_askleave_dialog,null);
        WheelDatePicker datePicker = (WheelDatePicker) view.findViewById(R.id.askleave_datepicker);
        final HTextView dateText = (HTextView) view.findViewById(R.id.askleave_date_text);
        final EditText reasonText = (EditText) view.findViewById(R.id.askleave_reason_text);
        RadioGroup typePicker = (RadioGroup) view.findViewById(R.id.leave_type_selector);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH)+1;
        int day_of_month = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        mLeaveDate = date2str(year,month,day_of_month);
        mLeaveType = Constant.LEAVE_OTHER;
        mLeaveReason = "";

        typePicker.check(R.id.other_leave);
        typePicker.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.other_leave:
                        mLeaveType = Constant.LEAVE_OTHER;
                        break;
                    case R.id.sick_leave:
                        mLeaveType = Constant.LEAVE_SICK;
                        break;
                    case R.id.affair_leave:
                        mLeaveType = Constant.LEAVE_AFFAIR;
                        break;
                }
            }
        });

        dateText.animateText(year + "年" + month + "月" + day_of_month + "日" + " "+getWeekStr(year+"-"+month+"-"+day_of_month));

        datePicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {

            }

            @Override
            public void onWheelSelected(int index, String data) {
                String[] date = data.split("-");
                dateText.animateText(date[0]+"年"+date[1]+"月"+date[2]+"日"+" "+getWeekStr(data));
                mLeaveDate = data.replace("-","");
            }

            @Override
            public void onWheelScrollStateChanged(int state) {

            }
        });

        mDialog.setTitle("请假申请")
                .setPositiveButton("提交", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null){
                            mLeaveReason = reasonText.getText().toString();
                            mListener.onLeaveAskComplete(mLeaveType,mLeaveDate,mLeaveReason);
                            mDialog.dismiss();
                        }
                    }
                })
                .setCanceledOnTouchOutside(true)
                .setContentView(view);

        mDialog.show();
    }

    private String date2str(int year, int month, int day){
        String ret = year + "";

        if (month < 10){
            ret += "0";
        }
        ret += month;
        if (day < 10){
            ret += "0";
        }
        ret += day;

        return ret;
    }

    private String getWeekStr(String dateStr){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (c.get(Calendar.DAY_OF_WEEK)){
            case 1:
                return "星期天";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
        }
        return "";
    }

    public void setOnLeaveAskCompleteListener(OnLeaveAskCompleteListener listener){
        this.mListener = listener;
    }

    public interface OnLeaveAskCompleteListener{
        void onLeaveAskComplete(int leave_type, String leave_date, String leave_reason);
    }
}
