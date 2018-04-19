package com.tgf.kcwc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class VSScrollView extends HorizontalScrollView {

    public VSScrollView(Context paramContext) {
        super(paramContext);
    }

    public VSScrollView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public VSScrollView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        super.onScrollChanged(l, t, oldl, oldt);
        if (mTouchView == this) {
            mListener.onScrollChanged(l, t, oldl, oldt);
        }

    }

    public interface ScrollChangedListener {

        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    private ScrollChangedListener mListener = null;

    public void setScrollChangedListener(ScrollChangedListener listener) {
        this.mListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mTouchView = this;
        return super.onTouchEvent(ev);
    }

    public HorizontalScrollView mTouchView;
}
