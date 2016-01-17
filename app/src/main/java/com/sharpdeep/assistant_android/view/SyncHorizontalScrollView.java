package com.sharpdeep.assistant_android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * Created by bear on 16-1-15.
 */
public class SyncHorizontalScrollView extends HorizontalScrollView{
    private View mView;

    public SyncHorizontalScrollView(Context context) {
        super(context);
    }

    public SyncHorizontalScrollView(Context context, AttributeSet attrs) {
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
