package com.sharpdeep.assistant_android.view;

import android.content.Context;
import android.view.View;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by bear on 16-4-1.
 */
public class AlertDialog {
    private Context mContext;
    private MaterialDialog mDialog;

    public AlertDialog(Context context){
        this.mContext = context;
    }

    public void alert(String msg){
        mDialog = new MaterialDialog(this.mContext)
                .setTitle("alert")
                .setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDialog != null){
                            mDialog.dismiss();
                        }
                    }
                })
                .setCanceledOnTouchOutside(true)
                .setMessage(msg);

        mDialog.show();
    }
}
