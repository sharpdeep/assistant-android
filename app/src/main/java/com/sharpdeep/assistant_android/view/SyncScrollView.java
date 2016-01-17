package com.sharpdeep.assistant_android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by bear on 16-1-15.
 */
public class SyncScrollView extends ScrollView{
    private View mView;

    public SyncScrollView(Context context) {
        super(context);
    }

    public SyncScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (this.mView != null){
            this.mView.scrollTo(l,t);
        }
    }

    public void setView(View view){
        this.mView = view;
    }
}
