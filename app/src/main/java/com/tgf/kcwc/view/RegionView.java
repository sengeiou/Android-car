package com.tgf.kcwc.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tgf.kcwc.R;

/**
 * @anthor noti
 * @time 2017/8/7 11:14
 * 倾向价格展示
 */

public class RegionView extends View{
    /**
     * 左侧字体画笔
     */
    private Paint leftPaint;
    /**
     * 右侧及中间字体画笔
     */
    private Paint rightPaint;
    /**
     * 左侧颜色
     */
    private int leftColor;
    /**
     * 右侧及中间字体颜色
     */
    private int rightColor;
    /**
     * 所有字体大小
     */
    private int textSize;

    public RegionView(Context context) {
        this(context,null);
    }

    public RegionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RegionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //整个渐变图
        Drawable drawable = getResources().getDrawable(R.drawable.region_shape);
        Rect rect = new Rect();
        drawable.setBounds(rect);
        drawable.draw(canvas);
    }

    private void init() {
        //初始化颜色
        leftColor = getResources().getColor(R.color.colorAccent);
        rightColor = getResources().getColor(R.color.colorAccent);
        //初始化字体大小
        textSize = sp2px(18);
        //初始化画笔
        leftPaint = new Paint();
        leftPaint.setColor(leftColor);
        leftPaint.setTextAlign(Paint.Align.LEFT);
        leftPaint.setAntiAlias(true);
        leftPaint.setTextSize(textSize);

        rightPaint = new Paint();
        rightPaint.setColor(rightColor);
        rightPaint.setAntiAlias(true);
        rightPaint.setTextAlign(Paint.Align.RIGHT);
        rightPaint.setTextSize(textSize);
    }
    private int dp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().density;
        return (int) (v * value + 0.5f);
    }

    private int sp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (v * value + 0.5f);
    }
}
