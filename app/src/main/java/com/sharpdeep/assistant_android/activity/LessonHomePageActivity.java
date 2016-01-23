package com.sharpdeep.assistant_android.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.activity.fragment.StudentListFragment;
import com.sharpdeep.assistant_android.api.AssistantService;
import com.sharpdeep.assistant_android.helper.DataCacher;
import com.sharpdeep.assistant_android.helper.RetrofitHelper;
import com.sharpdeep.assistant_android.model.resultModel.Student;
import com.sharpdeep.assistant_android.model.resultModel.StudentListResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import retrofit.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by bear on 16-1-22.
 */
public class LessonHomePageActivity  extends AppCompatActivity{
    @Bind(R.id.viewpager_lesson_homepage)
    MaterialViewPager mViewPager;
    @Bind(R.id.textview_header_lesson_homepage)
    TextView mHeader;

    private int mLessonColor;
    private String mLessonName;
    private List<String> mTabTitleList;
    private String mLessonId;
    private String mLessonTeacher;

    private List<Student> mStudentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_homepage);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
//        EventBus.getDefault().register(LessonHomePageActivity.this);

        Bundle bundle = this.getIntent().getExtras();
        mLessonName = bundle.getString("lesson_name");
        mLessonColor = Integer.valueOf(bundle.getString("lesson_color"));
        mLessonId = bundle.getString("lesson_id");
        mLessonTeacher = bundle.getString("lesson_teacher");
        mHeader.setText(mLessonName.substring(mLessonName.indexOf("]")+1));

        mTabTitleList = new ArrayList<>();
        mTabTitleList.add(getString(R.string.lesson_homepage_page1));
        mTabTitleList.add(getString(R.string.lesson_homepage_page2));
        mTabTitleList.add(getString(R.string.lesson_homepage_page3));

        getStudentList();
    }

    private void getStudentList() {
        Retrofit retrofit = RetrofitHelper.getRetrofit(LessonHomePageActivity.this);
        retrofit.create(AssistantService.class)
                .getStudentListByClassid(DataCacher.getInstance().getToken(),mLessonId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StudentListResult>() {
                    @Override
                    public void onCompleted() {
                        setupViewPager();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LessonHomePageActivity.this,"网络出了点问题",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(StudentListResult studentListResult) {
                        if (studentListResult.isSuccess()){
                            mStudentList = studentListResult.getStudents();
                        }else{
                            Toast.makeText(LessonHomePageActivity.this,studentListResult.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setupViewPager() {
        mViewPager.getViewPager().setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorAndUrl(
                                mLessonColor,
                                "https://fs01.androidpit.info/a/63/0e/android-l-wallpapers-630ea6-h900.jpg");
                    case 1:
                        return HeaderDesign.fromColorAndUrl(
                                mLessonColor,
                                "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg");
                    case 2:
                        return HeaderDesign.fromColorAndUrl(
                                mLessonColor,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)
                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter{

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new StudentListFragment(mStudentList);
        }

        @Override
        public int getCount() {
            return mTabTitleList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position >= mTabTitleList.size()){
                return "";
            }
            return mTabTitleList.get(position);
        }
    }

    @Override
    protected void onDestroy() {
//        EventBus.getDefault().unregister(LessonHomePageActivity.this);
        super.onDestroy();
    }

    public String getLessonId(){
        return this.mLessonId;
    }
}
