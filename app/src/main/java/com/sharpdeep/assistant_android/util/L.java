package com.sharpdeep.assistant_android.util;

/**
 * Created by bear on 16-1-9.
 * util for log
 */

import com.orhanobut.logger.Logger;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class L {
    private static boolean mIsOpen = true;
    private L() {
    }
    public static void init() {
        // 更多请参考：https://github.com/orhanobut/logger
        Logger.init("").smartTag(true).hideThreadInfo().setMethodOffset(1);
    }
    public static void setOpen(boolean isOpen) {
        mIsOpen = isOpen;
    }
    public static boolean isOpened() {
        return mIsOpen;
    }
    public static void v(String info, Object... args) {
        if (!mIsOpen) {
            return;
        }
        Logger.v(info, args);
    }
    public static void v(String tag, String info, Object... args) {
        if (!mIsOpen) {
            return;
        }
        Logger.t(tag).v(info, args);
    }
    public static void i(String info, Object... args) {
        if (!mIsOpen) {
            return;
        }
        Logger.i(info, args);
    }
    public static void i(String tag, String info, Object... args) {
        if (!mIsOpen) {
            return;
        }
        Logger.t(tag).i(info, args);
    }
    public static void d(String info, Object... args) {
        if (!mIsOpen) {
            return;
        }
        Logger.d(info, args);
    }
    public static void d(@NonNull String tag, @Nullable String info, Object... args) {
        if (!mIsOpen) {
            return;
        }
        Logger.t(tag).d(info, args);
    }
    public static void e(String info, Object... args) {
        if (!mIsOpen) {
            return;
        }
        Logger.e(info, args);
    }
    public static void e(@NonNull String tag, String info, Object... args) {
        if (!mIsOpen) {
            return;
        }
        Logger.t(tag).e(info, args);
    }
    public static void json(String json) {
        if (!mIsOpen) {
            return;
        }
        Logger.json(json);
    }
    public static void object(Object object) {
        if (!mIsOpen) {
            return;
        }
        Logger.object(object);
    }
}
