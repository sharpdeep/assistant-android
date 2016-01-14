package com.sharpdeep.assistant_android.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by bear on 16-1-13.
 */
public class AndroidUtil {
    public final static void startActivity(Context from,Class to){
        Intent it = new Intent(from,to);
        from.startActivity(it);
    }
}
