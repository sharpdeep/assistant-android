package com.sharpdeep.assistant_android.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.animatetext.HText;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.api.AssistantService;
import com.sharpdeep.assistant_android.helper.DataCacher;
import com.sharpdeep.assistant_android.helper.RetrofitHelper;
import com.sharpdeep.assistant_android.listener.AddLessonListener;
import com.sharpdeep.assistant_android.model.resultModel.Lesson;
import com.sharpdeep.assistant_android.model.resultModel.LessonInfoResult;
import com.sharpdeep.assistant_android.model.resultModel.Schedule;
import com.sharpdeep.assistant_android.util.L;
import com.sharpdeep.assistant_android.util.ToastUtil;

import net.qiujuer.genius.widget.GeniusTextView;

import me.drakeet.materialdialog.MaterialDialog;
import retrofit.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by bear on 16-4-10.
 */
public class LessonSearchDialog {

    private Context mContext;
    private MaterialDialog mDialog;
    private View mContentView;
    private EditText mEditText;
    private Button mSearchBtn;
    private HTextView mTxtHint;
    private GeniusTextView mTxtInfo;

    private AddLessonListener mListener;
    private boolean getLesson = false;
    private Lesson mLesson;

    public LessonSearchDialog(Context context){
        this.mContext = context;

        setupView();

    }

    private void setupView() {
        mContentView = LayoutInflater.from(this.mContext).inflate(R.layout.item_lesson_search_dialog,null);

        mEditText = (EditText) mContentView.findViewById(R.id.edittext_lesson_search_dialog);
        mSearchBtn = (Button) mContentView.findViewById(R.id.btn_lesson_search_dialog);
        mTxtHint = (HTextView) mContentView.findViewById(R.id.txt_lesson_search_dialog);
        mTxtInfo = (GeniusTextView) mContentView.findViewById(R.id.txt_lessoninfo_lesson_search_dialog);

        mTxtHint.animateText(mContext.getString(R.string.lesson_search_dialog_hint));

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lessonid = mEditText.getText().toString();
                if (lessonid.isEmpty()) {
                    mEditText.setError("请输入课程id");
                    return;
                }
                showLessonInfo(lessonid);
            }
        });


    }





    public void show(){
        mDialog = new MaterialDialog(this.mContext)
                .setTitle("添加课程")
                .setContentView(mContentView)
                .setPositiveButton("添加课程", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getLesson) {
                            if (mListener != null) {
                                mListener.onAddLesson(mLesson);
                                mDialog.dismiss();
                            }
                        } else {
                            mTxtHint.animateText("请先找到一个课程");
                        }
                    }
                })
                .setCanceledOnTouchOutside(true);

        mDialog.show();
    }

    public void dismiss(){
        mDialog.dismiss();
    }


    private void showLessonInfo(final String lessonid) {
        Retrofit retrofit = RetrofitHelper.getRetrofit(mContext);
        retrofit.create(AssistantService.class)
                .getLessonInfo(lessonid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LessonInfoResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show((Activity) mContext, "服务器出了点问题或者网络有误");
                        L.d(DataCacher.getInstance().getToken());
                        L.d(e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(LessonInfoResult lessonInfoResult) {
                        if (lessonInfoResult.isSuccess()) {
                            mTxtHint.animateText("获取到以下课程：");
                            mLesson = lessonInfoResult.getLesson();
                            showLessonInfoTxt(mLesson);
                            getLesson = true;
                        } else {
                            mTxtHint.animateText("不存在该课程");
                            mTxtInfo.setText("");
                            getLesson = false;
                        }

                    }
                });
    }

    private void showLessonInfoTxt(Lesson lesson){
        Schedule schedule = lesson.getSchedule();
        mTxtInfo.setText(
                "课程id:\t" + lesson.getId() + "\n" +
                        "课程名:\t" + lesson.getName() + "\n" +
                        "教师:\t" + lesson.getTeacher() + "\n" +
                        "教室:\t" + lesson.getClassroom() + "\n" +
                        "学分:\t" + lesson.getCredit() + "\n" +
                        "上课周:\t" + lesson.getStartWeek() + "-" + lesson.getEndWeek() + "周" + "\n\n" +
                        "周一:\t" + schedule.get1() + "\n" +
                        "周二:\t" + schedule.get2() + "\n" +
                        "周三:\t" + schedule.get3() + "\n" +
                        "周四:\t" + schedule.get4() + "\n" +
                        "周五:\t" + schedule.get5() + "\n" +
                        "周六:\t" + schedule.get6() + "\n" +
                        "周日:\t" + schedule.get0()
        );
    }

    public LessonSearchDialog setOnAddLessonListener(AddLessonListener listener){
        this.mListener = listener;
        return this;
    }

}
