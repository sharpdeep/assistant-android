package com.sharpdeep.assistant_android.activity;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.aigestudio.wheelpicker.core.AbstractWheelDecor;
import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.view.WheelCrossPicker;
import com.aigestudio.wheelpicker.view.WheelCurvedPicker;
import com.melnykov.fab.FloatingActionButton;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.helper.DataCacher;
import com.sharpdeep.assistant_android.model.dbModel.AppInfo;
import com.sharpdeep.assistant_android.model.eventModel.ImportDialogEvent;
import com.sharpdeep.assistant_android.util.L;
import com.sharpdeep.assistant_android.view.SyncHorizontalScrollView;
import com.sharpdeep.assistant_android.view.SyncScrollView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import me.drakeet.materialdialog.MaterialDialog;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by bear on 16-1-13.
 */
public class MainActivity extends AppCompatActivity{
    @Bind(R.id.toolbar_main)
    Toolbar mToolBar;
    @Bind(R.id.syllabus_bg)
    LinearLayout mSyllabusbgLL;
    @Bind(R.id.dayHoriScrollView)
    SyncHorizontalScrollView mDayHoriScrollView;
    @Bind(R.id.timeScrollView)
    SyncScrollView mTimeScrollView;
    @Bind(R.id.columnScrollView)
    SyncScrollView mColumnScrollView;
    @Bind(R.id.rowHoriScrollView)
    SyncHorizontalScrollView mRowHoriScrollView;
    @Bind(R.id.classGridLayout)
    GridLayout mClassTable;
    @Bind(R.id.fab)
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cacheData();
        init();
    }

    private void init() {
        ButterKnife.bind(this); //butterknife init
        setSupportActionBar(mToolBar);
        EventBus.getDefault().register(MainActivity.this);
        L.init();

        //init Scroll
        mDayHoriScrollView.setView(mRowHoriScrollView);
        mRowHoriScrollView.setView(mDayHoriScrollView);
        mDayHoriScrollView.setHorizontalScrollBarEnabled(false);

        mTimeScrollView.setView(mColumnScrollView);
        mColumnScrollView.setView(mTimeScrollView);
        mTimeScrollView.setVerticalScrollBarEnabled(false);

        //init Fab scroll action
        mFab.attachToScrollView(mColumnScrollView);
        mFab.attachToScrollView(mTimeScrollView);
    }

    @OnClick(R.id.fab)
    void onFabCilck(View view){
        mFab.hide(true);
        showImportDialog();
    }

    void showImportDialog(){
        View item = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_year_semester_selector,null);
        WheelCurvedPicker yearPicker = (WheelCurvedPicker) item.findViewById(R.id.year_selector);
        RadioGroup semesterPicker = (RadioGroup) item.findViewById(R.id.semester_selector);
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        final List<String> yearData = new ArrayList<String>();
        for (int i = 0; i < 5; i++){
            yearData.add((currentYear-i)+"-"+(currentYear-i+1)+"学年");
        }

        final ImportDialogEvent event = new ImportDialogEvent();

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
                event.setSelectYear(String.valueOf(currentYear - yearData.indexOf(data)));
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
                int index = -1;
                switch (checkedId){
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
                event.setSelectSemester(index);
                L.d(checkedId + "-->checked");

            }
        });

        final MaterialDialog dialog = new MaterialDialog(MainActivity.this);

        dialog.setTitle(getString(R.string.import_selector_dialog_title))
                .setContentView(item)
                .setPositiveButton("导入", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(event);
                        dialog.dismiss();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mFab.show(true);
                    }
                })
                .show();

    }

    private void cacheData() {
        Observable.create(new Observable.OnSubscribe<AppInfo>() {
            @Override
            public void call(Subscriber<? super AppInfo> subscriber) {
                List<AppInfo> infoList = AppInfo.listAll(AppInfo.class);
                if (infoList.size() > 0){
                    subscriber.onNext(infoList.get(0));
                }else{
                    subscriber.onNext(null);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<AppInfo>() {
                    @Override
                    public void call(AppInfo appInfo) {
                        if (appInfo != null){
                            DataCacher.getInstance().setAuthTime(appInfo.getAuthTime());
                            DataCacher.getInstance().setCurrentYear(appInfo.getCurrentYear());
                            DataCacher.getInstance().setCurrentSemester(appInfo.getCurrentSemester());
                            DataCacher.getInstance().setIdentify(appInfo.getCurrentUser().getIdentify());
                            DataCacher.getInstance().setToken(appInfo.getCurrentUser().getToken());
                        }
                    }
                });
    }

    public void onEvent(ImportDialogEvent event){
        L.d("get "+event.getSelectYear()+" and "+event.getSelectSemester()+" from event");
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(MainActivity.this);
        super.onDestroy();
    }
}
