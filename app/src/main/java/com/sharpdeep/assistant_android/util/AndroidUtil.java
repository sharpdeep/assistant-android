package com.sharpdeep.assistant_android.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;

import com.sharpdeep.assistant_android.MyApplication;

import java.io.FileReader;
import java.util.Map;
import java.util.Set;

/**
 * Created by bear on 16-1-13.
 */
public class AndroidUtil {
    public final static void startActivity(Context from,Class to){
        Intent it = new Intent(from,to);
        from.startActivity(it);
    }

    public final static void  startActivityWithExtraStr(Context from, Class to,Map<String,String> extra){
        Intent it = new Intent(from,to);
        Bundle bundle = new Bundle();
        Set<Map.Entry<String,String>> entries = extra.entrySet();

        for (Map.Entry<String,String> entry : entries){
            bundle.putString(entry.getKey(),entry.getValue());
        }

        it.putExtras(bundle);
        from.startActivity(it);
    }

    public final static String getDeviceId(){
        TelephonyManager manager = (TelephonyManager) MyApplication.getContext().getSystemService(MyApplication.getContext().TELEPHONY_SERVICE);
        return manager.getDeviceId();
    }
}
