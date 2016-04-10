package com.sharpdeep.assistant_android.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.core.AbstractWheelDecor;
import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.view.WheelCurvedPicker;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.api.AssistantService;
import com.sharpdeep.assistant_android.helper.Constant;
import com.sharpdeep.assistant_android.helper.DataCacher;
import com.sharpdeep.assistant_android.helper.DrawerHelper;
import com.sharpdeep.assistant_android.helper.RetrofitHelper;
import com.sharpdeep.assistant_android.helper.SyllabusFormater;
import com.sharpdeep.assistant_android.listener.AddLessonListener;
import com.sharpdeep.assistant_android.model.dbModel.AppInfo;
import com.sharpdeep.assistant_android.model.dbModel.User;
import com.sharpdeep.assistant_android.model.eventModel.ImportDialogEvent;
import com.sharpdeep.assistant_android.model.eventModel.LessonGridClickEvent;
import com.sharpdeep.assistant_android.model.resultModel.BaseResult;
import com.sharpdeep.assistant_android.model.resultModel.Lesson;
import com.sharpdeep.assistant_android.model.resultModel.SyllabusResult;
import com.sharpdeep.assistant_android.util.AndroidUtil;
import com.sharpdeep.assistant_android.util.L;
import com.sharpdeep.assistant_android.util.ProjectUtil;
import com.sharpdeep.assistant_android.util.ToastUtil;
import com.sharpdeep.assistant_android.view.BottomSheetDialog;
import com.sharpdeep.assistant_android.view.LessonSearchDialog;
import com.sharpdeep.assistant_android.view.SemesterSelector;
import com.sharpdeep.assistant_android.view.SyncHorizontalScrollView;
import com.sharpdeep.assistant_android.view.SyncScrollView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import de.greenrobot.event.EventBus;
import me.drakeet.materialdialog.MaterialDialog;
import retrofit.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
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
    @Bind(R.id.toolbar_title_main)
    TextView mTxtToolbarTitle;
    Snackbar mSnackbar;

    private BottomSheetDialog mBottomSheetDialog;
    private Drawer mDrawer;

    private boolean mBackClicked = false;
    private HashMap<Integer,Integer> mDrawerItemIdentify = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        init();

    }

    private void init() {
        ButterKnife.bind(this); //butterknife init
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

        //检查默认学期是否有缓存课表
        showSyllabusIfHasCache();

        setupToolBar();

        //init DrawMenu
        setupDrawer();

    }

    private void setupToolBar() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add_lesson:
                        showAddLessonDialog();
                        break;
                }
                return true;
            }
        });
    }

    private void setupDrawer(){
        mDrawer = DrawerHelper.getHelper()
                .buildin(MainActivity.this,mToolBar)
                .updateBadge()
                .getDrawer();
    }

    //注销账号
    public void logout() {
        //修改appInfo
        Observable.timer(1, TimeUnit.MICROSECONDS)
                .observeOn(Schedulers.io())
                .map(new Func1<Long, Object>() {
                    @Override
                    public Object call(Long aLong) {
                        AppInfo info = Select.from(AppInfo.class).first();
                        info.logout();
                        DataCacher.getInstance().logout();
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        L.d("exit error");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object o) {
                        AndroidUtil.startActivity(MainActivity.this, LoginActivity.class);
                        MainActivity.this.finish();
                    }
                });
    }

    private void showSyllabusIfHasCache() {
        SyllabusResult result = DataCacher.getInstance().getCurrentUser().getSyllabusResult();
        if (result == null){
            L.d("no such syllabus cache");
            mTxtToolbarTitle.setText(R.string.toolbar_title_main);
            return;
        }
        showSyllabus(result);
        //更新DataCacher的showing变量
        DataCacher.getInstance().setShowingYear(DataCacher.getInstance().getCurrentYear());
        DataCacher.getInstance().setShowingSemester(DataCacher.getInstance().getCurrentSemester());
        DataCacher.getInstance().setShowingSyllabus(result.toJson());

        mTxtToolbarTitle.setText(DataCacher.getInstance().getShowingYear() + convertSemester2Str(DataCacher.getInstance().getShowingSemester()));
        L.d("缓存中有" + DataCacher.getInstance().getShowingYear() + DataCacher.getInstance().getShowingSemester() + "的课表");
    }

    @OnClick(R.id.fab)
    void onFabCilck(View view){
        mFab.hide(true);
    }

    @OnLongClick(R.id.classGridLayout)
    boolean onClassTableLongClcik(View view){
        showBottomSheet();
        return false;
    }

    public void showBottomSheet(){
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.menu_bottom_sheet,null);
        TextView TxtBottomSheetImportSyllabus = (TextView) view.findViewById(R.id.txt_bottom_sheet_01);
        TextView TxtBottomSheetDefaultSyllabus = (TextView) view.findViewById(R.id.txt_bottom_sheet_02);

        mBottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        mBottomSheetDialog.setContentView(view)
                .setCancelable(true)
                .show();

        TxtBottomSheetImportSyllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImportDialog();
                mBottomSheetDialog.dismiss();
            }
        });

        TxtBottomSheetDefaultSyllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                //设置默认课表
                setDefaultSyllabus(DataCacher.getInstance().getShowingYear(),
                        DataCacher.getInstance().getShowingSemester(),
                        DataCacher.getInstance().getShowingSyllabus());
            }
        });
    }

    private void setDefaultSyllabus(String showingYear,int showingSemester,String syllabusResult) {
        Observable.just(new String[]{showingYear, String.valueOf(showingSemester), syllabusResult})
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<String[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        L.d("设置默认课表出了点问题");
                    }

                    @Override
                    public void onNext(String[] s) {
                        User user = Select.from(User.class)
                                .where(Condition.prop("username").eq(DataCacher.getInstance().getCurrentUser().getUsername()))
                                .first();
                        user.setCurrentYear(s[0]);
                        user.setCurrentSemester(Integer.valueOf(s[1]));
                        user.setSyllabusResult(s[2]);
                        user.saveAndUpdateCache();
                        L.d("成功设置" + s[0] + s[1] + "为默认课表");
                    }
                });
    }

    void showAddLessonDialog(){
        Boolean errorFlag = false;
        String errorMsg = "";
        if(!ProjectUtil.isTeacher(DataCacher.getInstance().getIdentify())){
            errorFlag = true;
            errorMsg = "你不是教师用户，没有该功能！";
        } else if("".equals(DataCacher.getInstance().getShowingYear()) || 0 == DataCacher.getInstance().getShowingSemester()){

            SemesterSelector selector = new SemesterSelector(MainActivity.this,"请先设置当前学期","确定");
            selector.setOnSelectedListener(new SemesterSelector.OnSelectedListener() {
                @Override
                public void onSelected(String selectedYear, int selectedSemester) {
                    L.d("seletor -> "+selectedYear+":"+selectedSemester);
                    DataCacher.getInstance().setShowingYear(selectedYear);
                    DataCacher.getInstance().setShowingSemester(selectedSemester);
                    mTxtToolbarTitle.setText(selectedYear  + convertSemester2Str(selectedSemester));
                }
            });
            selector.show();

        } else {
            new LessonSearchDialog(MainActivity.this)
                    .setOnAddLessonListener(new AddLessonListener() {
                        @Override
                        public void onAddLesson(Lesson lesson) {
                            ToastUtil.show(MainActivity.this,"add lesson "+lesson.getName());
                        }
                    })
                    .show();
        }
        if(errorFlag){
            final MaterialDialog dialog = new MaterialDialog(MainActivity.this);
            dialog.setTitle("Error")
                    .setMessage(errorMsg)
                    .setPositiveButton("返回", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    })
                    .setCanceledOnTouchOutside(true)
                    .show();
        }



        L.d("showing ->" + DataCacher.getInstance().getShowingYear() + DataCacher.getInstance().getShowingSemester() +
                ";current ->" + DataCacher.getInstance().getCurrentYear() + DataCacher.getInstance().getCurrentSemester());
    }

    void showImportDialog(){
        SemesterSelector selector = new SemesterSelector(MainActivity.this,"导入课表","导入");
        selector.setOnSelectedListener(new SemesterSelector.OnSelectedListener() {
            @Override
            public void onSelected(String seletedYear, int selectedSemester) {
                ImportDialogEvent event = new ImportDialogEvent();
                event.setSelectYear(seletedYear);
                event.setSelectSemester(selectedSemester);
                EventBus.getDefault().post(event);
            }
        });
        selector.show();
    }

    //获取课表
    public void onEvent(ImportDialogEvent event){
        L.d("get " + event.getSelectYear() + " and " + event.getSelectSemester() + " from event");
        final String selectedYear = event.getSelectYear();
        final int selectedSemester = event.getSelectSemester();
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
                        Toast.makeText(MainActivity.this, "服务器好像出了点问题，稍后再试", Toast.LENGTH_SHORT).show();
                        L.d(e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(SyllabusResult syllabusResult) {
                        if (syllabusResult.isSuccess()) {
                            //显示课表(todo)
                            mSnackbar = Snackbar.make(mSnackbarContanier.getRootView(), "成功获取课表", Snackbar.LENGTH_SHORT);
                            showSyllabus(syllabusResult);
                            //更新DataCache中的showing变量
                            DataCacher.getInstance().setShowingYear(selectedYear);
                            DataCacher.getInstance().setShowingSemester(selectedSemester);
                            DataCacher.getInstance().setShowingSyllabus(syllabusResult.toJson());
                            mTxtToolbarTitle.setText(selectedYear + convertSemester2Str(selectedSemester));
                        } else {
                            mSnackbar = Snackbar.make(mSnackbarContanier.getRootView(), "获取课表失败", Snackbar.LENGTH_SHORT);
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
            final int lessonColor = formater.getBGRandomColor();
            grid.setBackgroundColor(lessonColor);
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
                        event.setLessonColor(lessonColor);
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

    //缓存课表
    private void cacheSyllabus(final String year, final int semester,SyllabusResult result){
        Observable.just(result)
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<SyllabusResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        L.d("缓存课表失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(SyllabusResult result) {
                        L.d(result.toJson());
                        User user = DataCacher.getInstance().getCurrentUser();
                        user.setSyllabusResult(result.toJson());
                        user.save();
                        DataCacher.getInstance().setCurrentUser(user);
                        L.d("课表缓存成功");
                    }
                });
    }

    //课程盒子点击后触发事件
    public void onEventMainThread(LessonGridClickEvent event){
        Lesson lesson = event.getLeeson();
        Toast.makeText(MainActivity.this,lesson.getName(),Toast.LENGTH_SHORT).show();

        Map<String,String> extra = new HashMap<>();
        extra.put("lesson_name",lesson.getName());
        extra.put("lesson_color",String.valueOf(event.getLessonColor()));
        extra.put("lesson_id",lesson.getId());
        extra.put("lesson_teacher", lesson.getTeacher());

        AndroidUtil.startActivityWithExtraStr(MainActivity.this, LessonHomePageActivity.class, extra);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(MainActivity.this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU){
            showBottomSheet();
        }else if (keyCode == KeyEvent.KEYCODE_BACK){
            if (mDrawer!= null && mDrawer.isDrawerOpen()){
                mDrawer.closeDrawer();
            }
            clickBackTwiceToExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void clickBackTwiceToExit() {
        if (!mBackClicked){
            mBackClicked = true;
            ToastUtil.show(MainActivity.this,"再按一次退出程序");
            Observable.timer(2,TimeUnit.SECONDS)
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            mBackClicked = false;
                        }
                    });
        }else{
            this.finish();
            System.exit(0);
        }
    }

    //some util
    private String convertSemester2Str(int semester){
        if (semester == 1){
            return "秋";
        }else if (semester == 2){
            return "春";
        }else if(semester == 3){
            return "夏";
        }

        return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(this.isFinishing()){
            DataCacher.getInstance().free();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mDrawer.setSelection(DrawerHelper.IDENTIFY_HOME);
    }
}
