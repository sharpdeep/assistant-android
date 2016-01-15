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

import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.helper.DataCacher;
import com.sharpdeep.assistant_android.model.dbModel.AppInfo;

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
public class MainActivity extends AppCompatActivity implements MaterialTabListener{
    @Bind(R.id.toolbar_main)
    Toolbar mToolBar;
    @Bind(R.id.tabHost)
    MaterialTabHost mTabHost;
    @Bind(R.id.pager)
    ViewPager mViewPager;
    ViewPagerAdapter mAdapter;

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

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        for (int i = 0; i < mAdapter.getCount(); i++) {
            mTabHost.addTab(
                    mTabHost.newTab()
                            .setText(mAdapter.getPageTitle(i))
                            .setTabListener(this)
            );

        }
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

    @OnPageChange(R.id.pager)
    void onPageChange(int position){
        mTabHost.setSelectedNavigationItem(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }


    @Override
    public void onTabSelected(MaterialTab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        public Fragment getItem(int num) {
            return new SyllabusFragment();// TODO: 16-1-13
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Sezione " + position;
        }

    }
}
