package com.sharpdeep.assistant_android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.widget.curved.WheelDatePicker;
import com.hanks.htextview.HTextView;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.helper.Constant;
import com.sharpdeep.assistant_android.helper.DataCacher;
import com.sharpdeep.assistant_android.listener.DatePickedListener;
import com.sharpdeep.assistant_android.util.DateUtil;

import java.util.Calendar;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by bear on 16-4-13.
 */
public class DatePickerDialog {
    private MaterialDialog mDialog;
    private Context mContext;

    private int year;
    private int month;
    private int day_of_month;

    private DatePickedListener mListener;

    public DatePickerDialog(Context context){
        this.mContext = context;
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH)+1;
        day_of_month = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public void show(String title){
        mDialog = new MaterialDialog(this.mContext);
        View contentView = LayoutInflater.from(this.mContext).inflate(R.layout.item_dialog_date_picker,null);
        final WheelDatePicker datePicker = (WheelDatePicker) contentView.findViewById(R.id.date_picker);
        final HTextView txtDate = (HTextView) contentView.findViewById(R.id.txt_date_picker);

        txtDate.animateText(year + "年" + month + "月" + day_of_month + "日" + DateUtil.getWeekStrByDateStr(year+"-"+month+"-"+day_of_month,"yyyy-MM-dd"));

        datePicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {

            }

            @Override
            public void onWheelSelected(int index, String data) {
                String[] date = data.split("-");
                txtDate.animateText(date[0]+"年"+date[1]+"月"+date[2]+"日 "+ DateUtil.getWeekStrByDateStr(data,"yyyy-MM-dd"));
                year = Integer.valueOf(date[0]);
                month = Integer.valueOf(date[1]);
                day_of_month = Integer.valueOf(date[2]);
            }

            @Override
            public void onWheelScrollStateChanged(int state) {

            }
        });

        if (title != null && !title.isEmpty()){
            mDialog.setTitle(title);
        }
        mDialog.setContentView(contentView)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null){
                            mListener.onDatePicked(year,month,day_of_month);
                        }
                        mDialog.dismiss();
                    }
                })
                .show();

    }

    public void dismiss(){
        mDialog.dismiss();
    }

    public DatePickerDialog setOnDatePickedListener(DatePickedListener listener){
        this.mListener = listener;
        return this;
    }
}
