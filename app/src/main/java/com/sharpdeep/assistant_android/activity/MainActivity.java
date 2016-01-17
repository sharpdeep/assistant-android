package com.sharpdeep.assistant_android.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.helper.DataCacher;
import com.sharpdeep.assistant_android.model.dbModel.AppInfo;
import com.sharpdeep.assistant_android.view.SyncHorizontalScrollView;
import com.sharpdeep.assistant_android.view.SyncScrollView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnPageChange;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
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

        //init Scroll
        mDayHoriScrollView.setView(mRowHoriScrollView);
        mRowHoriScrollView.setView(mDayHoriScrollView);
        mDayHoriScrollView.setHorizontalScrollBarEnabled(false);

        mTimeScrollView.setView(mColumnScrollView);
        mColumnScrollView.setView(mTimeScrollView);
        mTimeScrollView.setVerticalScrollBarEnabled(false);
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

}
