package com.tgf.kcwc.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tgf.kcwc.R;

/**
 * @anthor noti
 * @time 2017/8/7 9:05
 */

public class HistogramView extends View {
    /**
     * 横线的画笔
     */
    private Paint hLinePaint;
    /**
     * y轴指数的画笔
     */
    private Paint yNumPaint;
    /**
     * x轴车型的画笔
     */
    private Paint xModelPaint;
    /**
     * 图的画笔
     */
    private Paint picPaint;
    /**
     * 横线的颜色
     */
    private int hLineColor;
    /**
     * y轴指数的颜色
     */
    private int yNumColor;
    /**
     * x轴车型的颜色
     */
    private int xModelColor;
    /**
     * 横线的画笔大小
     */
    private int hLineSize;
    /**
     * y轴指数的画笔大小
     */
    private int yNumSize;
    /**
     * x轴车型的画笔大小
     */
    private int xModelSize;
    /**
     * 图的画笔大小
     */
    private int picSize;
    /**
     * 车型数据
     */
    private String[] modelData;
    /**
     * 车型指数数据
     */
    private int[] numData = new int[]{100, 50, 0};
    /**
     * 进度值数据
     */
    private String[] progressData;
    /**
     * 柱状图宽度
     */
    private float histogramWidth;
    /**
     * 柱状图间宽度
     */
    private float histogramBetweenWidth;
    /**
     * 数据的个数
     */
    private Paint.FontMetricsInt fontMetrics;
    private int zeroLine;
    private int xCount;

    public HistogramView(Context context) {
        this(context, null);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        startDraw(canvas);
    }

    private void init() {
        //初始化颜色
        hLineColor = getResources().getColor(R.color.bg_1);
        xModelColor = getResources().getColor(R.color.text_color9);
        yNumColor = getResources().getColor(R.color.sevicecity_tilefontopen);
        //初始化宽度
        hLineSize = dp2px(2);
        xModelSize = dp2px(40);
        yNumSize = dp2px(40);
        picSize = dp2px(40);
        //设置画笔
        hLinePaint = new Paint();
        hLinePaint.setColor(hLineColor);
        hLinePaint.setStyle(Paint.Style.FILL);
        hLinePaint.setAntiAlias(true);
//        hLinePaint.setTextSize(28);
        hLinePaint.setStrokeWidth(1);

        xModelPaint = new Paint();
        xModelPaint.setColor(xModelColor);
        xModelPaint.setStyle(Paint.Style.FILL);
        xModelPaint.setAntiAlias(true);
        xModelPaint.setTextSize(60);
//        xModelPaint.setStrokeWidth(xModelSize);
        xModelPaint.setTextAlign(Paint.Align.RIGHT);

        yNumPaint = new Paint();
        yNumPaint.setColor(yNumColor);
        yNumPaint.setStyle(Paint.Style.FILL);
        yNumPaint.setAntiAlias(true);
        yNumPaint.setTextSize(60);
//        yNumPaint.setStrokeWidth(yNumSize);
        yNumPaint.setTextAlign(Paint.Align.RIGHT);

        picPaint = new Paint();
//        picPaint.setColor(hLineColor);
        picPaint.setStyle(Paint.Style.FILL);
        picPaint.setAntiAlias(true);
//        picPaint.setStrokeWidth(picSize);
    }

    private void startDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int left = dp2px(5);
        int top = dp2px(20);
        int buttom = dp2px(20);
        int right = dp2px(40);
        //渐变图宽度
        int picWidth = dp2px(8);
        int lineLeft = dp2px(40);
        //y轴数组长度
        int count = numData.length;

        //Y轴间距
        int numHeightBetween = (height - buttom - top) / count;
        for (int i = 0; i < count; i++) {
            //绘制Y轴指数
            canvas.drawText(numData[i] + "", lineLeft - left, top + numHeightBetween * i, yNumPaint);
            Paint.FontMetricsInt fontMetrics = yNumPaint.getFontMetricsInt();
            int baseLineY = top + numHeightBetween * i;
            int center = baseLineY - (fontMetrics.bottom - fontMetrics.top) / 2 + fontMetrics.bottom;
            //绘制横线
            canvas.drawLine(lineLeft, center, width - sp2px(10), center, hLinePaint);
            if (i == count - 1) {
                zeroLine = center;
            }
        }
        //柱状图的渐变
        GradientDrawable drawable = (GradientDrawable) getResources().getDrawable(R.drawable.histogram_gradient_shape);
        //绘制柱状图
        //X轴间距
        int xBetween = (int) ((float) (width - lineLeft - right) / xCount - picWidth);
        for (int i = 0; i < xCount; i++) {
            int rectLeft = -1;
            if (i == 0) {
                rectLeft = xBetween;
            } else {
                rectLeft = xBetween * (i + 1) + picWidth * i;
            }
            Rect rect = new Rect(lineLeft + rectLeft, zeroLine - (int) ((Float.parseFloat(progressData[i]) / 20 * numHeightBetween)), lineLeft + (xBetween + picWidth) * (i + 1), zeroLine);
            drawable.setBounds(rect);
            drawable.draw(canvas);
        }
        int angle = -45;

        //绘制X轴车型
        for (int i = 0; i < xCount; i++) {
            canvas.save();
            canvas.rotate(angle, lineLeft + (xBetween + picWidth) * (i + 1)+sp2px(5), zeroLine + sp2px(10));
            canvas.drawText(modelData[i], lineLeft + (xBetween + picWidth) * (i + 1)+sp2px(5),zeroLine + sp2px(10), xModelPaint);
            canvas.restore();
        }
    }

    /**
     * 设置数据
     *
     * @param progressData
     * @param modelData
     */
    public void setData(String[] progressData, String[] modelData) {
        if (progressData.length == 0 && progressData == null && modelData.length == 0 && modelData == null){
            return;
        }
        if (progressData.length != modelData.length){
            return;
        }
        this.progressData = progressData;
        this.modelData = modelData;
        xCount = progressData.length;
        postInvalidate();
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