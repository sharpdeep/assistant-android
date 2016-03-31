package com.sharpdeep.assistant_android.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.aigestudio.wheelpicker.core.AbstractWheelDecor;
import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.view.WheelCurvedPicker;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.util.L;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.greenrobot.event.EventBus;
import me.drakeet.materialdialog.MaterialDialog;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by bear on 16-3-23.
 */
public class SemesterSelector {
    private Context mContext;
    private String mSelectedYear;
    private int mSelectedSemester = 1;
    private String mTitle = "";
    private String mPostiveBtnStr = "";
    private OnSelectedListener mSelectedListener;

    public SemesterSelector(Context context,String title,String postiveBtnStr){
        this.mContext = context;
        this.mTitle = title;
        this.mPostiveBtnStr = postiveBtnStr;
    }

    public void setOnSelectedListener(OnSelectedListener listener){
        mSelectedListener = listener;
    }

    public void show(){
        View item = LayoutInflater.from(this.mContext).inflate(R.layout.item_year_semester_selector,null);
        WheelCurvedPicker yearPicker = (WheelCurvedPicker) item.findViewById(R.id.year_selector);
        RadioGroup semesterPicker = (RadioGroup) item.findViewById(R.id.semester_selector);
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        final List<String> yearData = new ArrayList<String>();
        for (int i = 0; i < 5; i++){
            yearData.add((currentYear-i)+"-"+(currentYear-i+1)+"学年");
        }

        yearPicker.setData(yearData);
        yearPicker.setWheelDecor(false, new AbstractWheelDecor() {
            @Override
            public void drawDecor(Canvas canvas, Rect rectLast, Rect rectNext, Paint paint) {
                canvas.drawColor(0x8881d4fa);
            }
        });

        yearPicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {


            }

            @Override
            public void onWheelSelected(int index, String data) {
                mSelectedYear = String.valueOf(currentYear - yearData.indexOf(data));
                L.d(data + "-->select");
            }

            @Override
            public void onWheelScrollStateChanged(int state) {

            }
        });

        semesterPicker.check(R.id.autumn_semester);
        semesterPicker.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = 1;
                switch (checkedId) {
                    case R.id.autumn_semester:
                        index = 1;
                        break;
                    case R.id.spring_semester:
                        index = 2;
                        break;
                    case R.id.summer_semester:
                        index = 3;
                        break;
                }
                mSelectedSemester = index;
                L.d(checkedId + "-->checked");

            }
        });

        final MaterialDialog dialog = new MaterialDialog(this.mContext);

        dialog.setTitle(this.mTitle)
                .setContentView(item)
                .setPositiveButton(this.mPostiveBtnStr, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mSelectedListener != null){
                            mSelectedListener.onSelected(mSelectedYear,mSelectedSemester);
                        }
                        dialog.dismiss();
                    }
                })
                .setCanceledOnTouchOutside(true)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                })
                .show();


    }

    public interface OnSelectedListener{
        public void onSelected(String seletedYear,int selectedSemester);
    }
}
