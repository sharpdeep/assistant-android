package com.sharpdeep.assistant_android.helper;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

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
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.activity.MainActivity;
import com.sharpdeep.assistant_android.activity.StudentLeavelogActivity;
import com.sharpdeep.assistant_android.activity.StudentSignlogActivity;
import com.sharpdeep.assistant_android.api.AssistantService;
import com.sharpdeep.assistant_android.model.resultModel.BaseResult;
import com.sharpdeep.assistant_android.util.AndroidUtil;
import com.sharpdeep.assistant_android.util.L;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by bear on 16-4-7.
 */
public class DrawerHelper {
    private Drawer mDrawer;
    private Activity mActivity;
    private static DrawerHelper mHelper;
    public static final int IDENTIFY_HOME = 1;
    public static final int IDENTIFY_OA = 2;
    public static final int IDENTIFY_SIGNLOG = 3;
    public static final int IDENTIFY_LEAVELOG = 4;
    public static final int IDENTIFY_SETTING = 5;
    public static final int IDENTIFY_SUGGESTION = 6;
    public static final int IDENTIFY_EXIT = 7;

    private DrawerHelper(){}

    public static DrawerHelper getHelper(){
        if (mHelper == null){
            mHelper = new DrawerHelper();
        }
        return mHelper;
    }

    public DrawerHelper buildin(final Activity activity,Toolbar toolbar){
        this.mActivity = activity;

        PrimaryDrawerItem homeItem = new PrimaryDrawerItem()
                .withName(R.string.home_page)
                .withIdentifier(IDENTIFY_HOME)
                .withIcon(R.drawable.ic_action_home)
                .withSelectedIcon(R.drawable.ic_action_home_selected);

        PrimaryDrawerItem oaItem = new PrimaryDrawerItem()
                .withName(R.string.oa_page)
                .withIdentifier(IDENTIFY_OA)
                .withIcon(R.drawable.ic_oa)
                .withSelectedIcon(R.drawable.ic_oa_selected);

        PrimaryDrawerItem signlogItem = new PrimaryDrawerItem()
                .withName(R.string.drawer_signlog)
                .withIdentifier(IDENTIFY_SIGNLOG)
                .withIcon(R.drawable.ic_action_signlog)
                .withSelectedIcon(R.drawable.ic_action_signlog_selected)
                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_blue_700))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (!(activity instanceof StudentSignlogActivity)) {
                            AndroidUtil.startActivity(activity, StudentSignlogActivity.class);
                        }
                        return false;
                    }
                });

        PrimaryDrawerItem leavelogItem = new PrimaryDrawerItem()
                .withName(R.string.drawer_leavelog)
                .withIdentifier(IDENTIFY_LEAVELOG)
                .withIcon(R.drawable.ic_action_leavelog)
                .withSelectedIcon(R.drawable.ic_action_leavelog_selected)
                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_blue_700))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (!(activity instanceof StudentLeavelogActivity)){
                            AndroidUtil.startActivity(activity,StudentLeavelogActivity.class);
                        }
                        return false;
                    }
                });

        PrimaryDrawerItem settingItem = new PrimaryDrawerItem()
                .withName(R.string.setting_page)
                .withIdentifier(IDENTIFY_SETTING)
                .withIcon(R.drawable.ic_action_settings)
                .withSelectedIcon(R.drawable.ic_action_settings_selected);

        PrimaryDrawerItem suggestionItem = new PrimaryDrawerItem()
                .withName(R.string.suggestion_page)
                .withIdentifier(IDENTIFY_SUGGESTION)
                .withIcon(R.drawable.ic_suggestion)
                .withSelectedIcon(R.drawable.ic_suggestion_selected);

        PrimaryDrawerItem exitItem = new PrimaryDrawerItem()
                .withName(R.string.exit)
                .withIdentifier(IDENTIFY_EXIT)
                .withIcon(R.drawable.ic_exit)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (activity instanceof MainActivity) {
                            ((MainActivity) activity).logout();
                        }
                        return false;
                    }
                })
                .withSelectedIcon(R.drawable.ic_exit_selected);

        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .withOnlyMainProfileImageVisible(true)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        //显示个人主页(todo)
                        Toast.makeText(activity, "onclick", Toast.LENGTH_SHORT).show();
                        return false;//false 为 click后close
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        Toast.makeText(activity,"快别按了，我要窒息了！",Toast.LENGTH_SHORT).show();
                        return true; //true为出发long click就不触发click
                    }
                })
                .addProfiles(new ProfileDrawerItem()
                        .withName(DataCacher.getInstance().getCurrentUser().getUsername())
                        .withIcon(R.drawable.profile))
                .build();

        mDrawer = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        homeItem,
                        oaItem,
                        new DividerDrawerItem(),
                        signlogItem,
                        leavelogItem,
                        new DividerDrawerItem(),
                        settingItem,
                        suggestionItem,
                        new DividerDrawerItem(),
                        exitItem
                )
                .build();
        return this;
    }

    public Drawer getDrawer(){
        return mDrawer;
    }

    public DrawerHelper updateBadge(){
        updateStudentSignlogBadge();
        updateStudentLeaveLogBadge();
        return this;
    }

    private void updateStudentSignlogBadge(){
        RetrofitHelper.getRetrofit(this.mActivity)
                .create(AssistantService.class)
                .getStudentSignlogCount(DataCacher.getInstance().getCurrentUser().getUsername(),"all")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        L.d("获取学生签到数目时出错");
                    }

                    @Override
                    public void onNext(BaseResult baseResult) {
                        if (baseResult.isSuccess() && !baseResult.getMsg().equals("0")){
                            mDrawer.updateBadge(IDENTIFY_SIGNLOG,new StringHolder(baseResult.getMsg()));
                        }
                    }
                });
    }

    private void updateStudentLeaveLogBadge(){
        RetrofitHelper.getRetrofit(this.mActivity)
                .create(AssistantService.class)
                .getStudentLeavelogCount(DataCacher.getInstance().getCurrentUser().getUsername(),"all")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        L.d("获取学生请假记录数目时出错");
                    }

                    @Override
                    public void onNext(BaseResult baseResult) {
                        if (baseResult.isSuccess() && !baseResult.getMsg().equals("0")){
                            mDrawer.updateBadge(IDENTIFY_LEAVELOG,new StringHolder(baseResult.getMsg()));
                        }
                    }
                });
    }
}
