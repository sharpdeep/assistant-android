package com.sharpdeep.assistant_android.activity;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.core.AbstractWheelDecor;
import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.view.WheelCurvedPicker;
import com.melnykov.fab.FloatingActionButton;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.api.AssistantService;
import com.sharpdeep.assistant_android.helper.DataCacher;
import com.sharpdeep.assistant_android.helper.RetrofitHelper;
import com.sharpdeep.assistant_android.helper.SyllabusFormater;
import com.sharpdeep.assistant_android.model.dbModel.AppInfo;
import com.sharpdeep.assistant_android.model.eventModel.ImportDialogEvent;
import com.sharpdeep.assistant_android.model.eventModel.LessonGridClickEvent;
import com.sharpdeep.assistant_android.model.resultModel.Lesson;
import com.sharpdeep.assistant_android.model.resultModel.SyllabusResult;
import com.sharpdeep.assistant_android.util.AndroidUtil;
import com.sharpdeep.assistant_android.util.L;
import com.sharpdeep.assistant_android.view.SyncHorizontalScrollView;
import com.sharpdeep.assistant_android.view.SyncScrollView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import me.drakeet.materialdialog.MaterialDialog;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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
    @Bind(R.id.snackbar_contanier)
    CoordinatorLayout mSnackbarContanier;
    Snackbar mSnackbar;

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

        //init DrawMenu
        PrimaryDrawerItem homeItem = new PrimaryDrawerItem()
                .withName(R.string.home_page)
                .withIcon(R.drawable.ic_action_home)
                .withSelectedIcon(R.drawable.ic_action_home_selected);

        PrimaryDrawerItem oaItem = new PrimaryDrawerItem()
                .withName(R.string.oa_page)
                .withIcon(R.drawable.ic_oa)
                .withSelectedIcon(R.drawable.ic_oa_selected);

        PrimaryDrawerItem settingItem = new PrimaryDrawerItem()
                .withName(R.string.setting_page)
                .withIcon(R.drawable.ic_action_settings)
                .withSelectedIcon(R.drawable.ic_action_settings_selected);

        PrimaryDrawerItem suggestionItem = new PrimaryDrawerItem()
                .withName(R.string.suggestion_page)
                .withIcon(R.drawable.ic_suggestion)
                .withSelectedIcon(R.drawable.ic_suggestion_selected);

        PrimaryDrawerItem exitItem = new PrimaryDrawerItem()
                .withName(R.string.exit)
                .withIcon(R.drawable.ic_exit)
                .withSelectedIcon(R.drawable.ic_exit_selected);

        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(MainActivity.this)
                .withHeaderBackground(R.drawable.header)
                .withOnlyMainProfileImageVisible(true)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        //显示个人主页(todo)
                        Toast.makeText(MainActivity.this,"onclick",Toast.LENGTH_SHORT).show();
                        return false;//false 为 click后close
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        Toast.makeText(MainActivity.this,"快别按了，我要窒息了！",Toast.LENGTH_SHORT).show();
                        return true; //true为出发long click就不触发click
                    }
                })
                .addProfiles(new ProfileDrawerItem().withName(DataCacher.getInstance().getCurrentUser().getUsername()).withIcon(R.drawable.profile))
                .build();

        new DrawerBuilder().withActivity(MainActivity.this)
                .withToolbar(mToolBar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(homeItem, oaItem,new DividerDrawerItem(),settingItem,suggestionItem,new DividerDrawerItem(),exitItem)
                .build();
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
                .setCanceledOnTouchOutside(true)
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
                            DataCacher.getInstance().setCurrentUser(appInfo.getCurrentUser());
                        }
                    }
                });
    }

    //获取课表
    public void onEvent(ImportDialogEvent event){
        L.d("get " + event.getSelectYear() + " and " + event.getSelectSemester() + " from event");
        RetrofitHelper.getRetrofit(MainActivity.this)
                .create(AssistantService.class)
                .getSyllabus(DataCacher.getInstance().getToken(),event.getSelectYear(),event.getSelectSemester())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SyllabusResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this,"服务器好像出了点问题，稍后再试",Toast.LENGTH_SHORT).show();
                        L.d(e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(SyllabusResult syllabusResult) {
                        if (syllabusResult.isSuccess()) {
                            //显示课表(todo)
                            mSnackbar = Snackbar.make(mSnackbarContanier.getRootView(),"成功获取课表",Snackbar.LENGTH_SHORT);
                            showSyllabus(syllabusResult);
                        } else {
                            mSnackbar = Snackbar.make(mSnackbarContanier.getRootView(),"获取课表失败",Snackbar.LENGTH_SHORT);
                        }
                        mFab.hide();
                        mSnackbar.setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                mFab.show();
                            }
                        })
                                .show();
                    }
                });
    }

    private void showSyllabus(SyllabusResult result) {
        mClassTable.removeAllViews();
        SyllabusFormater formater = new SyllabusFormater(MainActivity.this,result);
        for(;!formater.end();formater.next()){
//            L.d(formater.getGridText()+",("+formater.getGridColumnSpec()+","+formater.getGridColumnSpan()+"),("+formater.getGridRowSpec()+","+formater.getGridRowSpan()+"),("+formater.getGridHeigh()+","+formater.getGridWidth()+")");
            TextView grid = new TextView(MainActivity.this);
            grid.setTextSize(11);
            grid.setText(formater.getGridText());
            grid.setTextColor(formater.getTextColor());
            grid.setBackgroundColor(formater.getBGRandomColor());
            grid.setWidth(formater.getGridWidth());
            grid.setHeight(formater.getGridHeigh());
            grid.setGravity(Gravity.CENTER);
            grid.setClickable(formater.getClickable());
            if (formater.getClickable()){
                final Lesson currentLesson  = formater.getCurrentLesson();
                grid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LessonGridClickEvent event = new LessonGridClickEvent();
                        event.setLeeson(currentLesson);
                        EventBus.getDefault().post(event);
                    }
                });
            }

            GridLayout.Spec rowSpec = GridLayout.spec(formater.getGridRowSpec(),formater.getGridRowSpan());
            GridLayout.Spec columnSpec = GridLayout.spec(formater.getGridColumnSpec(),formater.getGridColumnSpan());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec,columnSpec);
            params.setGravity(Gravity.CENTER);
            params.setMargins(2, 1, 2, 1);
            mClassTable.addView(grid, params);
            grid.requestLayout();
            mClassTable.requestLayout();
        }


    }

    public void onEventMainThread(LessonGridClickEvent event){
        Lesson lesson = event.getLeeson();
        Toast.makeText(MainActivity.this,lesson.getName(),Toast.LENGTH_SHORT).show();

        Map<String,String> extra = new HashMap<>();
        extra.put("lesson_name",lesson.getName());
        AndroidUtil.startActivityWithExtraStr(MainActivity.this,LessonHomePageActivity.class,extra);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(MainActivity.this);
        super.onDestroy();
    }
}
