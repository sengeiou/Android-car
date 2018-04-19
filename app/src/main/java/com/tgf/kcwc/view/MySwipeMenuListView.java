package com.tgf.kcwc.view;

import android.content.Context;
import android.util.AttributeSet;

import com.baoyz.swipemenulistview.SwipeMenuListView;

/**
 * Auther: Scott
 * Date: 2017/2/13 0013
 * E-mail:hekescott@qq.com
 */

public class MySwipeMenuListView extends SwipeMenuListView {
    public MySwipeMenuListView(Context context) {
        super(context);
    }

    public MySwipeMenuListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MySwipeMenuListView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);


    }
}
