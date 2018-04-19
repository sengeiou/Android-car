package com.tgf.kcwc.view;

/**
 * Created by Administrator on 2017/5/11.
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

/**
 * 设置高度的ViewPager
 */
public class WrapContentHeightViewPager extends ViewPager {
    public WrapContentHeightViewPager(Context context) {
        super(context);
    }

    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int heights = wm.getDefaultDisplay().getHeight();

        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) height = h;
        }

        if (height<heights){
            height=heights;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
