package com.sharpdeep.assistant_android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.model.resultModel.Student;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.materialdialog.MaterialDialog;
import rx.Observable;

/**
 * Created by bear on 16-4-15.
 */
public class RandomStudentPickerDialog {
    private Context mContext;
    private MaterialDialog mDialog;

    public RandomStudentPickerDialog(Context context){
        this.mContext = context;
    }

    public void show(List<Student> studentList){
        mDialog = new MaterialDialog(this.mContext);

        View contentView = LayoutInflater.from(this.mContext).inflate(R.layout.item_studentlist,null);
        CircleImageView avator = (CircleImageView) contentView.findViewById(R.id.imageview_studentlist_studentavator);
        TextView studentId = (TextView) contentView.findViewById(R.id.textview_studentlist_studentid);
        TextView studentName = (TextView) contentView.findViewById(R.id.textview_studentlist_studentname);
        TextView studentMajor = (TextView) contentView.findViewById(R.id.textview_studentlist_studentmajor);

        int randomIndex = (int)(Math.random()*(studentList.size()));
        Student student = studentList.get(randomIndex);

        studentId.setText(student.getId());
        studentName.setText(student.getName());
        studentMajor.setText(student.getMajor());

        mDialog.setTitle("已点到:")
                .setContentView(contentView)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                })
                .show();

    }

    public void dismiss(){
        mDialog.dismiss();
    }
}
