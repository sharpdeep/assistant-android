package com.sharpdeep.assistant_android.activity.fragment.lessonpage;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.activity.LessonHomePageActivity;
import com.sharpdeep.assistant_android.api.AssistantService;
import com.sharpdeep.assistant_android.helper.DataCacher;
import com.sharpdeep.assistant_android.helper.RetrofitHelper;
import com.sharpdeep.assistant_android.listener.WindowFocusChangedListener;
import com.sharpdeep.assistant_android.model.resultModel.BaseResult;
import com.sharpdeep.assistant_android.model.resultModel.Student;
import com.sharpdeep.assistant_android.model.resultModel.StudentListResult;
import com.sharpdeep.assistant_android.util.L;
import com.sharpdeep.assistant_android.view.AlertDialog;
import com.sharpdeep.assistant_android.view.AskLeaveDialog;
import com.sharpdeep.assistant_android.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by bear on 16-1-22.
 */
public class StudentListFragment extends Fragment {

    RecyclerView mViewStudentList;
    private RecyclerView.Adapter mAdapter;
    private List<Student> mStudentList = new ArrayList<>();

    private String mLessonId;
    private Boolean mBMBInit = false;

    @Bind(R.id.check_in_boom_menu)
    BoomMenuButton mBMBCheckInBtn;

    public StudentListFragment(String lessonId){
        this.mLessonId = lessonId;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_studentlist,container,false);
        ButterKnife.bind(this, view);
        setupStudenListView(view);

        ((LessonHomePageActivity)getContext()).setOnWindowFocusChangedListener(new WindowFocusChangedListener() {
            @Override
            public void onWindowFocusChangedListener() {
                if (mBMBInit) {
                    return;
                }
                initBMB();
            }
        });

        getStudentListThenUpdate(view);
        return view;
    }


    private void setupStudenListView(View view){
        mViewStudentList = (RecyclerView) view.findViewById(R.id.recyclerview_studentlist);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mViewStudentList.setLayoutManager(layoutManager);
        mViewStudentList.setHasFixedSize(true);

        mAdapter = new RecyclerViewMaterialAdapter(new RecyclerViewAdapter());
        mViewStudentList.setAdapter(mAdapter);

//        for (int i = 0; i < mStudentList.size(); ++i)
//            mContentItems.add(new Object());

//        mAdapter.notifyDataSetChanged();

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mViewStudentList, null);
    }

    private void initBMB() {
        int[] drawablesResource = new int[]{
                R.drawable.ic_image_sign_white_128,
                R.drawable.ic_image_leave_white_128
        };
        String[] subBtnTexts = new String[]{"签到","请假"};
        int[][] subBtnColors = new int[drawablesResource.length][2];
        Drawable[] subBtnDrawables = new Drawable[drawablesResource.length];

        //init subBtnDrawables
        for(int i = 0; i < drawablesResource.length; i++){
            subBtnDrawables[i] = ContextCompat.getDrawable(getContext(),drawablesResource[i]);
        }
        //init subBtnColors
        for (int i = 0; i < drawablesResource.length; i++){
            subBtnColors[i][1] = ContextCompat.getColor(getContext(), R.color.material_blue_300);
            subBtnColors[i][0] = Util.getInstance().getPressedColor(subBtnColors[i][1]);
        }

        mBMBCheckInBtn.init(
                subBtnDrawables, // The drawables of images of sub buttons. Can not be null.
                subBtnTexts,     // The texts of sub buttons, ok to be null.
                subBtnColors,    // The colors of sub buttons, including pressed-state and normal-state.
                ButtonType.CIRCLE,     // The button type.
                BoomType.PARABOLA,  // The boom type.
                PlaceType.CIRCLE_2_1,  // The place type.
                null,               // Ease type to move the sub buttons when showing.
                null,               // Ease type to scale the sub buttons when showing.
                null,               // Ease type to rotate the sub buttons when showing.
                null,               // Ease type to move the sub buttons when dismissing.
                null,               // Ease type to scale the sub buttons when dismissing.
                null,               // Ease type to rotate the sub buttons when dismissing.
                null                // Rotation degree.
        );

        mBMBCheckInBtn.setTextViewColor(ContextCompat.getColor(getContext(), R.color.white));
        mBMBCheckInBtn.setOnSubButtonClickListener(new BoomMenuButton.OnSubButtonClickListener() {
            @Override
            public void onClick(int buttonIndex) {
                switch (buttonIndex){
                    case 0:
                        L.d("签到");
                        signin();
                        break;
                    case 1:
                        L.d("请假");
                        askLeave();
                        break;
                }
            }
        });
        mBMBInit = true;
    }


    public void getStudentListThenUpdate(final View view) {
        Retrofit retrofit = RetrofitHelper.getRetrofit(getContext());
        retrofit.create(AssistantService.class)
                .getStudentListByClassid(DataCacher.getInstance().getToken(),mLessonId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StudentListResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), "网络出了点问题", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(StudentListResult studentListResult) {
                        if (studentListResult.isSuccess()) {
                            mStudentList = studentListResult.getStudents();
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), studentListResult.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //签到
    private void signin(){
        Retrofit retrofit = RetrofitHelper.getRetrofit(getContext());

        LoadingDialog dialog = new LoadingDialog(getContext());
        dialog.setDismissObservable(
                retrofit.create(AssistantService.class)
                        .signin(DataCacher.getInstance().getToken(), getLessonId(), getDeviceId(), getWifiMac())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Func1<BaseResult, Boolean>() {
                            @Override
                            public Boolean call(BaseResult baseResult) {
                                L.d(baseResult.getMsg());
                                new AlertDialog(getContext()).alert(baseResult.getMsg());
                                return true;
                            }
                        })
        );
        dialog.setErrorHandler(new LoadingDialog.ErrorHandler() {
            @Override
            public void handler() {
                new AlertDialog(getContext()).alert("服务器或网络出了点问题");
            }
        });

        dialog.show("正在签到");
    }

    //请假
    private void askLeave(){
        final Retrofit retrofit = RetrofitHelper.getRetrofit(getContext());

        AskLeaveDialog askLeaveDialog = new AskLeaveDialog(getContext());

        final LoadingDialog dialog = new LoadingDialog(getContext());
        dialog.setErrorHandler(new LoadingDialog.ErrorHandler() {
            @Override
            public void handler() {
                new AlertDialog(getContext()).alert("服务器或者网络出了点问题");
            }
        });

        askLeaveDialog.setOnLeaveAskCompleteListener(new AskLeaveDialog.OnLeaveAskCompleteListener() {
            @Override
            public void onLeaveAskComplete(int leave_type, String leave_date, String leave_reason) {
                L.d(leave_date);
                dialog.setDismissObservable(
                        retrofit.create(AssistantService.class)
                .askLeave(DataCacher.getInstance().getToken(),getLessonId(),leave_type,leave_date,leave_reason)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseResult, Boolean>() {
                    @Override
                    public Boolean call(BaseResult baseResult) {
                        L.d(baseResult.getMsg());
                        new AlertDialog(getContext()).alert(baseResult.getMsg());
                        return true;
                    }
                }));
                dialog.show("正在申请请假...");
            }
        });
        askLeaveDialog.show();

    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<StudentViewHolder> {


        static final int TYPE_HEADER = 0;
        static final int TYPE_CELL = 1;


        public RecyclerViewAdapter(){

        }

        @Override
        public int getItemViewType(int position) {
            switch (position) {
                case 0:
                    return TYPE_HEADER;
                default:
                    return TYPE_CELL;
            }
        }

        @Override
        public int getItemCount() {
//            return contents.size();
            return mStudentList.size();
        }

        @Override
        public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;

            switch (viewType) {
                case TYPE_HEADER: {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_studentlist, parent, false);
                    return new StudentViewHolder(view) {
                    };
                }
                case TYPE_CELL: {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_studentlist, parent, false);
                    return new StudentViewHolder(view) {
                    };
                }
            }
            return null;
        }


        @Override
        public void onBindViewHolder(StudentViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case TYPE_HEADER:
                case TYPE_CELL:
                    Student student = mStudentList.get(position);
                    holder.studentName.setText(student.getName());
                    holder.studentId.setText(student.getId());
                    holder.studentMajor.setText(student.getMajor());
                    break;
            }
        }

    }

    class StudentViewHolder extends RecyclerView.ViewHolder{
        TextView studentName;
        TextView studentId;
        TextView studentMajor;
        CircleImageView studentAvator;

        public StudentViewHolder(View itemView) {
            super(itemView);
            studentName = (TextView) itemView.findViewById(R.id.textview_studentlist_studentname);
            studentId = (TextView) itemView.findViewById(R.id.textview_studentlist_studentid);
            studentMajor = (TextView) itemView.findViewById(R.id.textview_studentlist_studentmajor);
            studentAvator = (CircleImageView) itemView.findViewById(R.id.imageview_studentlist_studentavator);
        }
    }

    private String getLessonId(){
        return this.mLessonId;
    }

    private String getDeviceId(){
        return "123";
    }

    private String getWifiMac(){
        return "mac";
    }
}
