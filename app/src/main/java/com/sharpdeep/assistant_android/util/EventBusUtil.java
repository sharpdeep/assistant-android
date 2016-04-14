package com.sharpdeep.assistant_android.util;

import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by bear on 16-4-14.
 */
public class EventBusUtil {
    public static void delayPost(final Object event, long delay, TimeUnit unit){
        Observable.just(true)
                .delay(delay,unit)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        EventBus.getDefault().post(event);
                    }
                });
    }
}
