package com.wlison.view.nicelibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Author: Simon
 * Date：8/30/2019 16:48
 * Desc:
 */
public class NiceScrollView extends ScrollView {

    public NiceScrollView(Context context) {
        super(context);
    }

    public NiceScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NiceScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollCallbacks != null){
            mOnScrollCallbacks.onScrollChanged(l,t,oldl,oldt);
        }
    }

    private OnScrollCallbacks mOnScrollCallbacks;

    public void setOnScrollCallbacks(OnScrollCallbacks callbacks){
        this.mOnScrollCallbacks = callbacks;
    }

    public interface OnScrollCallbacks{
        void onScrollChanged(int x, int y, int oldl, int oldt);
    }
}
