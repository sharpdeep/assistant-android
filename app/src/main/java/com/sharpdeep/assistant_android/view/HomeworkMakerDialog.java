package com.sharpdeep.assistant_android.view;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.widget.curved.WheelDatePicker;
import com.hanks.htextview.HTextView;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.listener.HomeworkEditedListener;
import com.sharpdeep.assistant_android.util.DateUtil;

import java.util.Calendar;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by bear on 16-4-18.
 */
public class HomeworkMakerDialog {
    private MaterialDialog mDialog;
    private Context mContext;

    private int year = Calendar.getInstance().get(Calendar.YEAR);
    private int month = Calendar.getInstance().get(Calendar.MONTH)+1;
    private int day_of_month = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    private HomeworkEditedListener mHomeworkEditedListener;

    public HomeworkMakerDialog(Context context){
        this.mContext = context;
    }

    public void show(){
        mDialog = new MaterialDialog(this.mContext);
        View contentView = LayoutInflater.from(this.mContext).inflate(R.layout.item_homeworkmaker_dialog,null);
        final EditText homeworkTitle = (EditText) contentView.findViewById(R.id.edittxt_homework_title);
        final EditText homeworkContent = (EditText) contentView.findViewById(R.id.edittxt_homework_content);
        WheelDatePicker datePicker = (WheelDatePicker) contentView.findViewById(R.id.datepicker_homework_deadline);
        final HTextView deadlineTxt = (HTextView) contentView.findViewById(R.id.txt_homework_deadline);

        deadlineTxt.animateText(year + "年" + month + "月" + day_of_month + "日" + " "+ DateUtil.getWeekStrByDateStr(DateUtil.getDateStr(year,month,day_of_month,"yyyyMMdd"),"yyyyMMdd"));

        datePicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {

            }

            @Override
            public void onWheelSelected(int index, String data) {
                String[] date = data.split("-");
                deadlineTxt.animateText(date[0]+"年"+date[1]+"月"+date[2]+"日"+" "+DateUtil.getWeekStrByDateStr(data,"yyyy-MM-dd"));
                year = Integer.valueOf(date[0]);
                month = Integer.valueOf(date[1]);
                day_of_month = Integer.valueOf(date[2]);
            }

            @Override
            public void onWheelScrollStateChanged(int state) {

            }
        });


        mDialog.setTitle("发布作业")
                .setContentView(contentView)
                .setPositiveButton("发布", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = homeworkTitle.getText().toString();
                        if (title.isEmpty()){
                            homeworkTitle.setError("Title不能为空");
                            return;
                        }
                        String content = homeworkContent.getText().toString();
                        if (content.isEmpty()){
                            homeworkContent.setError("内容不能为空");
                            return;
                        }
                        if (mHomeworkEditedListener != null){
                            mHomeworkEditedListener.onHomeworkEdited(title,content,DateUtil.getDateStr(year,month,day_of_month,"yyyyMMdd"));
                        }
                        dismiss();
                    }
                })
                .show();
    }

    public void setOnHomeworkEditedListener(HomeworkEditedListener listener){
        mHomeworkEditedListener = listener;
    }

    public void dismiss(){
        mDialog.dismiss();
    }
}
