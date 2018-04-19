package com.tgf.kcwc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Auther: Scott 嵌套滑动的listview
 * Date: 2016/12/12 0012
 * E-mail:hekescott@qq.com
 */
public class InnerListView extends ListView {

    private static final String TAG  = "InnerListView";
    private int                 flag = 3;
    private boolean             isLast;
private  Context mContext;
    public InnerListView(Context context) {
        super(context);
        mContext = context;
    }

    public InnerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public InnerListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isLast)
                    setParentScrollAble(false);//当手指触到listview的时候，让父ScrollView交出ontouch权限，也就是让父scrollview停住不能滚动

            case MotionEvent.ACTION_MOVE:

                break;

            case MotionEvent.ACTION_UP:

            case MotionEvent.ACTION_CANCEL:

                setParentScrollAble(true);//当手指松开时，让父ScrollView重新拿到onTouch权限

                break;
            default:
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 是否把滚动事件交给父scrollview
     *
     * @param
    flag
     */
    private void setParentScrollAble(boolean flag) {
        this.getParent().requestDisallowInterceptTouchEvent(!flag);//这里的parentScrollView就是listview外面的那个scrollview

    }

    public void setScrollLast(boolean isLast) {
        this.isLast = isLast;
    }

    //    我写了一个参数叫maxHeight.设置listview的最大高度。是为了确保ListView的高度固定。

    private int maxHeight;

    public int getMaxHeight() {
        return maxHeight;
    }
    //    public void setMaxHeight(int maxHeight) {
    //
    //        this.maxHeight = maxHeight;
    //    }

    public void setFlag(int maxHeight) {

        this.flag = maxHeight;
    }
    //    @Override
    //    protected void
    //    onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //        if (maxHeight > -1) {
    //
    //            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight,
    //                    MeasureSpec.AT_MOST);
    //        }
    //        super.onMeasure(widthMeasureSpec,
    //                heightMeasureSpec);
    //        System.out.println(getChildAt(0));
    //    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //        int expandSpec1 = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
        //
        //                MeasureSpec.AT_MOST);

        //        super.onMeasure(widthMeasureSpec, expandSpec);
        //        Logger.d(TAG,"expandSpec1 == "+expandSpec1);

        if (flag == 1 || flag == 2) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

            super.onMeasure(widthMeasureSpec, expandSpec);

        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }

    @Override
    public void addFooterView(View v) {
        TextView tv =new TextView(mContext);
        tv.setText("123123234");
        super.addFooterView(tv);
    }
}
