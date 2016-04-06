package com.sharpdeep.assistant_android.util;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by bear on 16-4-6.
 */
public class ToastUtil {
    /**
     * @param context Context
     * @param string  内容
     */
    public static void show(final Activity context, final String string) {
        //判断是否为主线程
        if ("main".equals(Thread.currentThread().getName())) {
            Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
        } else {//如果不是，就用该方法使其在ui线程中运行
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
