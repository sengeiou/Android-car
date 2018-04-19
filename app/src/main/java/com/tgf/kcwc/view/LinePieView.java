package com.tgf.kcwc.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.tgf.kcwc.R;

import java.util.Random;

/**
 * @anthor noti
 * @time 2017/8/7 11:41
 * 折线数据类型饼状统计图
 */
public class LinePieView extends View {

    /**
     * 使用wrap_content时默认的尺寸
     */
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 150;

    /**
     * 斜线长度
     */
    private static final int SlASH_LINE_OFFSET = 130;

    /**
     * 横线长度
     */
    private int HOR_LINE_LENGTH = 180;

    /**
     * 横线上文字的横向偏移量
     */
    private static final int X_OFFSET = 20;

    /**
     * 横线上文字的纵向偏移量
     */
    private static final int Y_OFFSET = 10;

    /**
     * 中心坐标
     */
    private int centerX;
    private int centerY;

    /**
     * 半径
     */
    private float radius;

    /**
     * 弧形外接矩形
     */
    private RectF rectF;

    /**
     * 中间文本的大小
     */
    private Rect centerTextBound = new Rect();

    /**
     * 数据文本的大小
     */
    private Rect dataTextBound = new Rect();

    /**
     * 扇形画笔
     */
    private Paint mArcPaint;

    /**
     * 中心文本画笔
     */
    private Paint centerTextPaint;

    /**
     * 数据画笔
     */
    private Paint dataPaint;

    /**
     * 圆球画笔
     */
    private Paint circlePaint;
    /**
     * 点，线画笔
     */
    private Paint mLinePaint;

    /**
     * 数据源数字数组
     */
    private double[] numbers;

    /**
     * 数据源名称数组
     */
    private String[] names;

    /**
     * 数据源总和
     */
    private int sum;

    /**
     * 颜色数组
     */
    private int[] colors;

    private Random random = new Random();

    //自定义属性 Start

    /**
     * 中间字体大小
     */
    private float centerTextSize = 80;

    /**
     * 数据字体大小
     */
    private float dataTextSize = 40;

    /**
     * 中间字体颜色
     */
    private int centerTextColor = Color.BLACK;

    /**
     * 数据字体颜色
     */
    private int dataTextColor = Color.BLACK;

    /**
     * 圆圈的宽度
     */
    private float circleWidth = 50;
    /**
     * 圆球的半径
     */
    private float circleRadius = 4;

    public LinePieView(Context context) {
        super(context);
        init();
    }

    public LinePieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinePieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LinePieView);
        centerTextSize = typedArray.getDimension(R.styleable.LinePieView_centerTextSize, centerTextSize);
        dataTextSize = typedArray.getDimension(R.styleable.LinePieView_dataTextSize, dataTextSize);
        circleWidth = typedArray.getDimension(R.styleable.LinePieView_circleWidth, circleWidth);
        centerTextColor = typedArray.getColor(R.styleable.LinePieView_centerTextColor, centerTextColor);
        dataTextColor = typedArray.getColor(R.styleable.LinePieView_dataTextColor, dataTextColor);
        typedArray.recycle();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mArcPaint = new Paint();
//        mArcPaint.setStrokeWidth(circleWidth);
        mArcPaint.setStrokeWidth(sp2px(20));
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);

        centerTextPaint = new Paint();
        centerTextPaint.setTextSize(centerTextSize);
        centerTextPaint.setAntiAlias(true);
        centerTextPaint.setColor(centerTextColor);

        dataPaint = new Paint();
//        dataPaint.setStrokeWidth(2);
        dataPaint.setTextSize(sp2px(12));
        dataPaint.setAntiAlias(true);
        dataPaint.setColor(getResources().getColor(R.color.sevicecity_tilefontopen));

        circlePaint = new Paint();
        circlePaint.setStrokeWidth(dataTextSize);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(getResources().getColor(R.color.text_more));

        mLinePaint = new Paint();
//        mLinePaint.setStrokeWidth(circleWidth);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint.setColor(getResources().getColor(R.color.text_color33));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (measureWidthMode == MeasureSpec.AT_MOST
                && measureHeightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        } else if (measureWidthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_WIDTH, measureHeightSize);
        } else if (measureHeightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(measureWidthSize, DEFAULT_HEIGHT);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;
        //设置半径为宽高最小值的1/4
        radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 4;
        //设置扇形外接矩形
        rectF = new RectF(centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculateAndDraw(canvas);
    }

    /**
     * 计算比例并且绘制扇形和数据
     */
    private void calculateAndDraw(Canvas canvas) {
        if (numbers == null || numbers.length == 0) {
            return;
        }
        //扇形开始度数
        int startAngle = 0;
        //所占百分比
        double percent;
        //所占度数
        float angle;
        for (int i = 0; i < numbers.length; i++) {
//            percent = numbers[i] / (float) sum;
            percent = numbers[i];
            //获取百分比在360中所占度数
            if (i == numbers.length - 1) {//保证所有度数加起来等于360
                angle = 360 - startAngle;
            } else {
                angle = (float) Math.ceil(percent * 360);
            }

            //绘制第i段扇形
            drawArc(canvas, startAngle, angle, colors[i]);
            startAngle += angle;

            //绘制数据
            if (numbers[i] <= 0) {
                continue;
            }
            //当前扇形弧线相对于纵轴的中心点度数,由于扇形的绘制是从三点钟方向开始，所以加90
            float arcCenterDegree = 90 + startAngle - angle / 2;
            drawData(canvas, arcCenterDegree, i, percent);
        }
        //绘制中心数字总和
        //暂无需求
//        canvas.drawText(sum + "", centerX - centerTextBound.width() / 2, centerY + centerTextBound.height() / 2, centerTextPaint);
    }

    /**
     * 计算每段弧度的中心坐标
     *
     * @param degree 当前扇形中心度数
     */
    private float[] calculatePosition(float degree) {
        //由于Math.sin(double a)中参数a不是度数而是弧度，所以需要将度数转化为弧度
        //而Math.toRadians(degree)的作用就是将度数转化为弧度
        //sin 一二正，三四负 sin（180-a）=sin(a)
        //扇形弧线中心点距离圆心的x坐标
        float x = (float) (Math.sin(Math.toRadians(degree)) * radius);
        //cos 一四正，二三负
        //扇形弧线中心点距离圆心的y坐标
        float y = (float) (Math.cos(Math.toRadians(degree)) * radius);

        //每段弧度的中心坐标(扇形弧线中心点相对于view的坐标)
        float startX = centerX + x;
        float startY = centerY - y;

        float[] position = new float[2];
        position[0] = startX;
        position[1] = startY;
        return position;
    }

    /**
     * 绘制数据
     *
     * @param canvas  画布
     * @param degree  第i段弧线中心点相对于纵轴的夹角度数
     * @param i       第i段弧线
     * @param percent 数据百分比
     */
    private void drawData(Canvas canvas, float degree, int i, double percent) {
        //弧度中心坐标
        float startX = calculatePosition(degree)[0];
        float startY = calculatePosition(degree)[1];
        //斜线结束坐标
        float endX = 0;
        float endY = 0;
        //横线结束坐标
        float horEndX = 0;
        float horEndY = 0;
        //数字开始坐标
        float numberStartX = 0;
        float numberStartY = 0;
        //文本开始坐标
        float textStartX = 0;
        float textStartY = 0;
        //圆球的圆心坐标
        float circleX = 0;
        float circleY = 0;

        float numWidth = dataPaint.measureText(numbers[i]+"");
        float nameWidth = dataPaint.measureText(names[i]+"");
        float finalWidth = (numWidth > nameWidth) ? numWidth : nameWidth;
        HOR_LINE_LENGTH = (int) finalWidth + 30;
        //根据每个弧度的中心点坐标绘制数据
        dataPaint.getTextBounds(names[i], 0, names[i].length(), dataTextBound);
        //根据角度判断象限，并且计算各个坐标点
        if (degree > 90 && degree < 180) {//二象限
            endX = startX + SlASH_LINE_OFFSET;
            endY = startY + SlASH_LINE_OFFSET;

            horEndX = endX + HOR_LINE_LENGTH;
            horEndY = endY;

            numberStartX = endX + X_OFFSET;
            numberStartY = endY - Y_OFFSET;

            textStartX = endX + X_OFFSET;
            textStartY = endY + dataTextBound.height() + Y_OFFSET / 2;

            circleX = horEndX - circleRadius;
            circleY = horEndY;
        } else if (degree == 180) {
            startX = centerX;
            startY = centerY + radius;
            endX = startX + SlASH_LINE_OFFSET;
            endY = startY + SlASH_LINE_OFFSET;

            horEndX = endX + HOR_LINE_LENGTH;
            horEndY = endY;

            numberStartX = endX + X_OFFSET;
            numberStartY = endY - Y_OFFSET;

            textStartX = endX + X_OFFSET;
            textStartY = endY + dataTextBound.height() + Y_OFFSET / 2;

            circleX = horEndX - circleRadius;
            circleY = horEndY;
        } else if (degree > 180 && degree < 270) {//三象限
            endX = startX - SlASH_LINE_OFFSET;
            endY = startY + SlASH_LINE_OFFSET;

            horEndX = endX - HOR_LINE_LENGTH;
            horEndY = endY;

            numberStartX = endX - HOR_LINE_LENGTH + X_OFFSET;
            numberStartY = endY - Y_OFFSET;

            textStartX = endX - HOR_LINE_LENGTH + X_OFFSET;
            textStartY = endY + dataTextBound.height() + Y_OFFSET / 2;

            circleX = horEndX - circleRadius;
            circleY = horEndY;
        } else if (degree == 270) {
            startX = centerX - radius;
            startY = centerY;
            endX = startX - SlASH_LINE_OFFSET;
            endY = startY - SlASH_LINE_OFFSET;

            horEndX = endX - HOR_LINE_LENGTH;
            horEndY = endY;

            numberStartX = endX - HOR_LINE_LENGTH + X_OFFSET;
            numberStartY = endY - Y_OFFSET;

            textStartX = endX - HOR_LINE_LENGTH + X_OFFSET;
            textStartY = endY + dataTextBound.height() + Y_OFFSET / 2;

            circleX = horEndX - circleRadius;
            circleY = horEndY;
        } else if (degree > 270 && degree < 360) {//四象限
            endX = startX - SlASH_LINE_OFFSET;
            endY = startY - SlASH_LINE_OFFSET;

            horEndX = endX - HOR_LINE_LENGTH;
            horEndY = endY;

            numberStartX = endX - HOR_LINE_LENGTH + X_OFFSET;
            numberStartY = endY - Y_OFFSET;

            textStartX = endX - HOR_LINE_LENGTH + X_OFFSET;
            textStartY = endY + dataTextBound.height() + Y_OFFSET / 2;

            circleX = horEndX + circleRadius;
            circleY = horEndY;
        } else if (degree == 360) {
            startX = centerX;
            startY = centerY - radius;
            endX = startX - SlASH_LINE_OFFSET;
            endY = startY - SlASH_LINE_OFFSET;

            horEndX = endX - HOR_LINE_LENGTH;
            horEndY = endY;

            numberStartX = endX - HOR_LINE_LENGTH + X_OFFSET;
            numberStartY = endY - Y_OFFSET;

            textStartX = endX - HOR_LINE_LENGTH + X_OFFSET;
            textStartY = endY + dataTextBound.height() + Y_OFFSET / 2;

            circleX = horEndX + circleRadius;
            circleY = horEndY;
        } else if (degree > 360) {//一象限
            endX = startX + SlASH_LINE_OFFSET;
            endY = startY - SlASH_LINE_OFFSET;

            horEndX = endX + HOR_LINE_LENGTH;
            horEndY = endY;

            numberStartX = endX + X_OFFSET;
            numberStartY = endY - Y_OFFSET;

            textStartX = endX + X_OFFSET;
            textStartY = endY + dataTextBound.height() + Y_OFFSET / 2;

            circleX = horEndX + circleRadius;
            circleY = horEndY;
        }
        //绘制数字
        canvas.drawText(numbers[i] + "", numberStartX, textStartY, dataPaint);
        //绘制文字
        canvas.drawText(names[i] + "", textStartX, numberStartY, dataPaint);

        //绘制折线
        canvas.drawLine(startX, startY, endX, endY, mLinePaint);
        //绘制横线
        canvas.drawLine(endX, endY, horEndX, horEndY, mLinePaint);
//        mLinePaint.setColor(getResources().getColor(R.color.voucher_gray));
        //绘制灰球
        canvas.drawCircle(circleX, circleY, circleRadius, mLinePaint);
    }

    /**
     * 绘制扇形
     *
     * @param canvas     画布
     * @param startAngle 开始度数
     * @param angle      扇形的度数
     * @param color      颜色
     */
    private void drawArc(Canvas canvas, float startAngle, float angle, int color) {
        mArcPaint.setColor(color);
        //-0.5和+0.5是为了让每个扇形之间没有间隙
        canvas.drawArc(rectF, startAngle - 0.5f, angle + 0.5f, false, mArcPaint);
//        canvas.drawArc(rectF, startAngle - 1.0f, 0.5f, false, mLinePaint);
    }

    /**
     * 生成随机颜色
     */
    private int randomColor() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return Color.rgb(red, green, blue);
    }

    /**
     * 设置数据(使用随机颜色)
     *
     * @param numbers 数字数组
     * @param names   名称数组
     */
    public void setData(double[] numbers, String[] names) {
        if (numbers == null || numbers.length == 0 || names == null || names.length == 0) {
            return;
        }
        if (numbers.length != names.length) {
            return;
        }
        this.numbers = numbers;
        this.names = names;
        colors = new int[numbers.length];
        sum = 0;
        for (int i = 0; i < this.numbers.length; i++) {
            //计算总和
            sum += numbers[i];
            //随机颜色
            colors[i] = randomColor();
        }
        //计算总和数字的宽高
        centerTextPaint.getTextBounds(sum + "", 0, (sum + "").length(), centerTextBound);
        invalidate();
    }

//    /**
//     * 设置数据(自定义颜色)
//     *
//     * @param numbers 数字数组
//     * @param names   名称数组
//     * @param colors  颜色数组
//     */
//    public void setData(int[] numbers, String[] names, int[] colors) {
//        if (numbers == null || numbers.length == 0
//                || names == null || names.length == 0
//                || colors == null || colors.length == 0) {
//            return;
//        }
//        if (numbers.length != names.length || numbers.length != colors.length) {
//            return;
//        }
//        this.numbers = numbers;
//        this.names = names;
//        this.colors = colors;
//        sum = 0;
//        for (int i = 0; i < this.numbers.length; i++) {
//            //计算总和
//            sum += numbers[i];
//        }
//        //计算总和数字的宽高
//        centerTextPaint.getTextBounds(sum + "", 0, (sum + "").length(), centerTextBound);
//        invalidate();
//    }
    private int dp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().density;
        return (int) (v * value + 0.5f);
    }

    private int sp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (v * value + 0.5f);
    }
}
