package com.sharpdeep.assistant_android.activity.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.activity.LessonHomePageActivity;
import com.sharpdeep.assistant_android.api.AssistantService;
import com.sharpdeep.assistant_android.helper.DataCacher;
import com.sharpdeep.assistant_android.helper.RetrofitHelper;
import com.sharpdeep.assistant_android.model.resultModel.Student;
import com.sharpdeep.assistant_android.model.resultModel.StudentListResult;
import com.sharpdeep.assistant_android.util.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by bear on 16-1-22.
 */
public class StudentListFragment extends Fragment {

    RecyclerView mViewStudentList;
    private List<Object> mContentItems = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private List<Student> mStudentList = new ArrayList<>();

    public StudentListFragment(List<Student> studentList){
        this.mStudentList = studentList;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lesson_studentlist,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewStudentList = (RecyclerView) view.findViewById(R.id.recyclerview_studentlist);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mViewStudentList.setLayoutManager(layoutManager);
        mViewStudentList.setHasFixedSize(true);

        mAdapter = new RecyclerViewMaterialAdapter(new RecyclerViewAdapter(mContentItems));
        mViewStudentList.setAdapter(mAdapter);

        for (int i = 0; i < mStudentList.size(); ++i)
            mContentItems.add(new Object());

        mAdapter.notifyDataSetChanged();

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mViewStudentList, null);
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<StudentViewHolder> {

        List<Object> contents;

        static final int TYPE_HEADER = 0;
        static final int TYPE_CELL = 1;

        public RecyclerViewAdapter(List<Object> contents) {
            this.contents = contents;
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
            return contents.size();
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

}
