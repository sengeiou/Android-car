package com.tgf.kcwc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Author:Jenny
 * Date:2017/4/24
 * E-mail:fishloveqin@gmail.com
 * 自定义子布局，主要用于处理软键盘显示、隐藏监听
 */

public class ExRelativeLayout extends RelativeLayout {
    private OnSoftKeyboardListener mSoftKeyboardListener;

    public ExRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mSoftKeyboardListener.onSoftKeyboardChange();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setSoftKeyboardListener(OnSoftKeyboardListener listener) {
        mSoftKeyboardListener = listener;
    }

    public interface OnSoftKeyboardListener {
        public void onSoftKeyboardChange();
    }

}