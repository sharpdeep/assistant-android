package com.sharpdeep.assistant_android.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sharpdeep.assistant_android.R;
import com.sharpdeep.assistant_android.activity.MainActivity;

/**
 * Created by bear on 16-1-27.
 */
public class BottomSheetDialog {
    private Dialog mDialog;


    public BottomSheetDialog(Context context) {
        mDialog = new Dialog(context,R.style.MaterialDialogSheet);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public BottomSheetDialog setContentView(View view){
        mDialog.setContentView(view);
        return this;
    }

    public BottomSheetDialog setContentView(int layoutResId){
        mDialog.setContentView(layoutResId);
        return this;
    }

    public BottomSheetDialog setContentView(View view,ViewGroup.LayoutParams params){
        mDialog.setContentView(view,params);
        return this;
    }

    public BottomSheetDialog setCancelable(Boolean cancelable){
        mDialog.setCancelable(cancelable);
        return this;
    }


    public void show(){
        mDialog.show();
    }

    public void dismiss(){
        mDialog.dismiss();
    }
}
