package com.tgf.kcwc.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tgf.kcwc.view.chart.Quadrant;

import java.util.Random;

/**
 * @anthor noti
 * @time 2017/10/25
 * @describle
 */
public class StraightChartView extends View {
    //默认宽高
    private final int DEFAULT_WIDTH = dp2px(200);
    private final int DEFAULT_HEIGHT = dp2px(300);
    //背景画笔
    private Paint bgPaint;
    //文字画笔
    private Paint textLeftPaint;//左边文字画笔
    private Paint textMiddlePaint;//中间下面文字画笔
    private Paint textRightPaint;//右边文字画笔
    //中间图层画笔
    private Paint middlePaint;
    //下面文字画笔
    private Paint textBelowPaint;
    //左边文本内容
    String textLeftStr = "0";
    //左边文本高度
    private int textLeftHeight;
    //右边文本内容
    String textRightStr = "6";
    //下面文本数据
    String[] textBelowArr = new String[]{};
    //百分百数据
    double[] percentArr = new double[]{};
    //随机颜色数组
    private int[] colors;
    private Random random = new Random();

    public StraightChartView(Context context) {
        this(context, null);
    }

    public StraightChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StraightChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textLeftPaint = new Paint();
        textLeftPaint.setAntiAlias(true);
        textLeftPaint.setTextSize(dp2px(10));
        textLeftPaint.setTextAlign(Paint.Align.LEFT);

        textMiddlePaint = new Paint();
        textMiddlePaint.setAntiAlias(true);
        textMiddlePaint.setTextSize(dp2px(10));
        textMiddlePaint.setTextAlign(Paint.Align.CENTER);

        textRightPaint = new Paint();
        textRightPaint.setAntiAlias(true);
        textRightPaint.setTextSize(dp2px(6));
        textRightPaint.setTextAlign(Paint.Align.RIGHT);

        textBelowPaint = new Paint();
        textBelowPaint.setAntiAlias(true);
        textBelowPaint.setTextSize(dp2px(6));
        textBelowPaint.setTextAlign(Paint.Align.CENTER);
        textBelowPaint.setColor(Color.parseColor("#E00035"));

        middlePaint = new Paint();
        middlePaint.setAntiAlias(true);
        middlePaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureSize(DEFAULT_WIDTH, widthMeasureSpec),
                measureSize(DEFAULT_HEIGHT, heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制左右文字
        drawLeftRightText(canvas);
        //绘制背景
        drawBg(canvas);
        //绘制中间图层,下面文字
        drawMiddleLayer(canvas);
    }

    private void drawMiddleLayer(Canvas canvas) {
        for (int i = 0; i < textBelowArr.length; i++) {
            colors = new int[textBelowArr.length];
            colors[i] = randomColor();
            middlePaint.setColor(colors[i]);

            String textBelowStr = textBelowArr[i];
            String[] belowStr = textBelowStr.split("-");
            float middleWidth = (float) (quadrant.getQuadrantWidth() * percentArr[i]);
            float s = Float.parseFloat(belowStr[0]) / Float.parseFloat(textRightStr) * quadrant.getQuadrantWidth();
            //绘制中间图层
            canvas.drawRect(s, quadrant.getQuadrantStartY() + textLeftHeight, s + middleWidth, quadrant.getQuadrantPaddingEndY() - dp2px(15), middlePaint);
            //绘制下面文字
            canvas.drawText(textBelowArr[i] + "w", s + middleWidth / 2, quadrant.getQuadrantPaddingEndY(), textBelowPaint);
        }
    }

    private void drawLeftRightText(Canvas canvas) {
        //绘制左边文本
        canvas.drawText(textLeftStr, quadrant.getQuadrantStartX(), quadrant.getQuadrantStartY() + dp2px(5), textLeftPaint);
        //绘制右边文本
        canvas.drawText(textRightStr + "w", quadrant.getQuadrantPaddingEndX(), quadrant.getQuadrantStartY() + dp2px(5), textRightPaint);
    }

    private void drawBg(Canvas canvas) {
        //获取左边文本高度
        Paint.FontMetricsInt fm = textLeftPaint.getFontMetricsInt();
        textLeftHeight = fm.descent - fm.ascent;
        //渐变颜色
        int linearColor1 = Color.parseColor("#7D4504");
        int linearColor2 = Color.parseColor("#7B1E07");
        int linearColor3 = Color.parseColor("#70001A");
        int[] colors = new int[]{linearColor1, linearColor2, linearColor3};
        LinearGradient linearGradient = new LinearGradient(quadrant.getQuadrantStartX(), quadrant.getQuadrantStartY() + textLeftHeight, quadrant.getQuadrantPaddingEndX(), quadrant.getQuadrantPaddingEndY() - dp2px(15), colors, null, Shader.TileMode.CLAMP);
        bgPaint.setShader(linearGradient);
        canvas.drawRect(quadrant.getQuadrantStartX(), quadrant.getQuadrantStartY() + textLeftHeight, quadrant.getQuadrantPaddingEndX(), quadrant.getQuadrantPaddingEndY() - dp2px(15), bgPaint);
    }

    /**
     * 测量后的尺寸
     *
     * @param size
     * @param measureSpec
     * @return
     */
    private int measureSize(int size, int measureSpec) {
        int result = size;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            //具体的值
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            //父控件给子View一个最大的特定值
            case MeasureSpec.AT_MOST:
                result = Math.min(size, specSize);
                break;
            //默认值
            case MeasureSpec.UNSPECIFIED:
                result = size;
        }
        return result;
    }

    private int dp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().density;
        return (int) (v * value + 0.5f);
    }

    private int sp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (v * value + 0.5f);
    }

    Quadrant quadrant = new Quadrant(dp2px(5), dp2px(5), dp2px(5), dp2px(5)) {
        @Override
        public float getQuadrantStartX() {

            return dp2px(10);
        }

        @Override
        public float getQuadrantStartY() {
            return dp2px(10);
        }

        @Override
        public float getQuadrantWidth() {
            return getMeasuredWidth() - dp2px(10) * 2;
        }

        @Override
        public float getQuadrantHeight() {
            return getMeasuredHeight() - dp2px(10) * 2;
        }
    };

    /**
     * 生成随机颜色
     */
    private int randomColor() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return Color.rgb(red, green, blue);
    }
    //初始化数据
    public void setData(String[] textBelowArr, double[] percentArr, double maxDou) {
        this.textBelowArr = textBelowArr;
        this.percentArr = percentArr;
        this.textRightStr = maxDou+"";
        postInvalidate();
    }
}
