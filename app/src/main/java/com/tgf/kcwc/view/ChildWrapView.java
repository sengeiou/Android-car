package com.tgf.kcwc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by fuheng on 2016/5/4.
 */
public class ChildWrapView extends ViewGroup {
    private int paddingHor = 0;//水平方向padding
    private int paddingVertical = 0;//垂直方向padding
    private int sideMargin = 40;//左右间距
    private int textMargin = 0;

    /**
     * @param context
     */
    public ChildWrapView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public ChildWrapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * @param context
     * @param attrs
     */
    public ChildWrapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int autualWidth = r - l;
        int x = sideMargin;// 横坐标开始
        int y = 0;//纵坐标开始
        int rows = 1;
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();
            x += width + textMargin;
            if (x > autualWidth) {
                x = width + sideMargin;
                rows++;
            }
            y = rows * (height + textMargin);
            if (i == 0) {
                view.layout(x - width - textMargin, y - height, x - textMargin, y);
            } else {
                view.layout(x - width, y - height, x, y);
            }
        }
    }

    ;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int x = 0;//横坐标
        int y = 0;//纵坐标
        int rows = 1;//总行数
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int actualWidth = specWidth - sideMargin * 2;//实际宽度
        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++) {
            View child = getChildAt(index);
            child.setPadding(paddingHor, paddingVertical, paddingHor, paddingVertical);
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            x += width + textMargin;
            if (x > actualWidth) {//换行
                x = width;
                rows++;
            }
            y = rows * (height + textMargin);
        }
        setMeasuredDimension(specWidth, y);
    }

    public void setParameter(int paddingHor, int paddingVertical, int sideMargin, int textMargin) {
        this.paddingHor = paddingHor;
        this.paddingVertical = paddingVertical;
        this.sideMargin = sideMargin;
        this.textMargin = textMargin;
    }
}