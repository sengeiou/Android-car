package com.tgf.kcwc.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.tgf.kcwc.R;
import com.tgf.kcwc.util.BitmapUtils;

/**
 * Author：Jenny
 * Date:2017/1/4 14:57
 * E-mail：fishloveqin@gmail.com
 */

public class SpecRectView extends View {
    private Paint mBGPaint = new Paint();

    public int getBGColor() {
        return mBGColor;
    }

    public void setBGColor(int mBGColor) {
        this.mBGColor = mBGColor;
        mBGPaint.setColor(mBGColor);
    }

    public void setBGColor(String colorValue) {
        this.mBGColor = Color.parseColor(colorValue);
        mBGPaint.setColor(mBGColor);
        originalColor = colorValue;
        invalidate();
    }

    private int mBGColor = Color.LTGRAY;

    public int getmHeight() {
        return mHeight;
    }

    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
        invalidate();
    }

    private int mHeight;

    public void setCycleStroke(boolean cycleStroke) {
        isCycleStroke = cycleStroke;
    }

    private boolean isCycleStroke = false;

    public void setWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    private int mWidth;
    private boolean isBgStroke;
    private int strokeWidth = 0;

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    private double alpha;
    private Context mContext;

    public SpecRectView(Context context) {
        this(context, null);
        mContext = context;
    }

    public SpecRectView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
        mContext = context;
    }

    public SpecRectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpecRectView);

        isDrawBottomLeft = a.getBoolean(R.styleable.SpecRectView_drawBottomLeft, false);
        isDrawBottomRight = a.getBoolean(R.styleable.SpecRectView_drawBottomRight, false);
        isDrawTopLeft = a.getBoolean(R.styleable.SpecRectView_drawTopLeft, false);
        isDrawTopRight = a.getBoolean(R.styleable.SpecRectView_drawTopRight, false);
        mBGColor = a.getColor(R.styleable.SpecRectView_bgColor, Color.BLUE);
        mCycleColor = a.getColor(R.styleable.SpecRectView_drawCycleColor, Color.WHITE);
        mType = a.getInt(R.styleable.SpecRectView_type, 1);
        isBgStroke = a.getBoolean(R.styleable.SpecRectView_isBgStroke, false);
        isCycleStroke = a.getBoolean(R.styleable.SpecRectView_isCycleStroke, false);
        a.recycle();
        strokeWidth = BitmapUtils.dp2px(context, 1);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mHeight = h;
        mWidth = w;
    }

    private Paint ticketTypePaint = new Paint();
    private Paint cyclePaint = new Paint();
    private Paint dashPaint = new Paint();

    private String originalColor = "";

    private void init() {


        mBGPaint.setStyle(Paint.Style.FILL);//充满
        mBGPaint.setColor(mBGColor);
        mBGPaint.setAntiAlias(true);// 设置画笔的锯齿效果

        ticketTypePaint.setAntiAlias(true);

        cyclePaint.setAntiAlias(true);
        cyclePaint.setColor(mCycleColor);

        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setStrokeWidth(1);
        dashPaint.setAntiAlias(true);
        dashPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float right = mWidth;
        float left = 0;
        float top = 0;
        float bottom = mHeight;
        RectF rf = new RectF(left, top, right, bottom);
        switch (mType) {

            case 1:
                if (isBgStroke) {
                    mBGPaint.setStyle(Paint.Style.STROKE);
                    mBGPaint.setStrokeWidth(strokeWidth);
                }


                // 设置个新的长方形
                canvas.drawRoundRect(rf, 20, 15, mBGPaint);//第二个参数是x半径，第三个参数是y半径

                cyclePaint.setStyle(Paint.Style.FILL);
                cyclePaint.setStrokeWidth(1);
                cyclePaint.setColor(mCycleColor);
                if (isDrawBottomLeft) {
                    canvas.drawCircle(left, mHeight - 10, 20, cyclePaint);
                }
                if (isDrawBottomRight) {
                    canvas.drawCircle(right, mHeight - 10, 20, cyclePaint);
                }


                if (isDrawTopLeft) {
                    canvas.drawCircle(left, top, 20, cyclePaint);
                }
                if (isDrawTopRight) {
                    canvas.drawCircle(right, top, 20, cyclePaint);
                }
                if (isCycleStroke) {
                    cyclePaint.setStyle(Paint.Style.STROKE);
                    cyclePaint.setStrokeWidth(strokeWidth);
                    cyclePaint.setColor(mBGColor);
                    if (isDrawTopLeft) {
                        canvas.drawCircle(left, top, 20, cyclePaint);
                    }
                    if (isDrawTopRight) {
                        canvas.drawCircle(right, top, 20, cyclePaint);
                    }

                    if (isDrawBottomLeft) {
                        canvas.drawCircle(left, mHeight - 10, 20, cyclePaint);
                    }
                    if (isDrawBottomRight) {
                        canvas.drawCircle(right, mHeight - 10, 20, cyclePaint);
                    }
                }
                break;

            case 2:

                canvas.drawRect(rf, mBGPaint);
                right = BitmapUtils.dp2px(mContext, 60);
                RectF rf1 = new RectF(left, top, right, bottom);

                canvas.drawRect(rf1, ticketTypePaint);
                canvas.drawCircle(left, mHeight / 2, 20, cyclePaint);
                canvas.drawCircle(right, top, 20, cyclePaint);
                canvas.drawCircle(right, bottom, 20, cyclePaint);
                canvas.drawCircle(mWidth, right, 20, cyclePaint);

                //设置画虚线，如果之后不再使用虚线，调用paint.setPathEffect(null);
                PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                Path path1 = new Path();
                path1.moveTo(right, top + 20);
                path1.lineTo(right, bottom - 20);
                dashPaint.setPathEffect(effects);
                canvas.drawPath(path1, dashPaint);

                path1.moveTo(right, right * 1.5f);
                path1.lineTo(mWidth, right * 1.5f);
                canvas.drawPath(path1, dashPaint);
                break;

            case 3:

                canvas.drawRect(rf, mBGPaint);
                right = BitmapUtils.dp2px(mContext, 60);
                rf1 = new RectF(mWidth * 0.7f + 5, top, mWidth, bottom);

                canvas.drawRect(rf1, ticketTypePaint);
                canvas.drawCircle(left, mHeight / 3, 20, cyclePaint);
                canvas.drawCircle(mWidth * 0.7f, top + 10, 10, cyclePaint);
                canvas.drawCircle(mWidth * 0.7f, bottom - 10, 10, cyclePaint);
                canvas.drawCircle(mWidth, mHeight / 3, 20, cyclePaint);

                //设置画虚线，如果之后不再使用虚线，调用paint.setPathEffect(null);
                effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                path1 = new Path();
                path1.moveTo(mWidth * 0.7f, top + 20);
                path1.lineTo(mWidth * 0.7f, bottom - 20);
                dashPaint.setPathEffect(effects);
                canvas.drawPath(path1, dashPaint);

//                path1.moveTo(right, right * 1.5f);
//                path1.lineTo(mWidth, right * 1.5f);
//                canvas.drawPath(path1, dashPaint);
                break;

            case 4:


                right = BitmapUtils.dp2px(mContext, 45);
                rf1 = new RectF(left, top, right, bottom);

                canvas.drawRoundRect(rf1, 20, 15, mBGPaint);
                rf1 = new RectF(right, top, mWidth, bottom);

                canvas.drawRoundRect(rf1, 20, 15, mBGPaint);


                int delta = BitmapUtils.dp2px(mContext, 29);

                rf1 = new RectF(left, delta + top, mWidth, bottom - delta);
                //mBGPaint.setColor(Color.parseColor(BitmapUtils.addAlpha(originalColor, 0.4)));
                Paint p1=new Paint();
                p1.setAntiAlias(true);
                p1.setColor(Color.WHITE);
                //mBGPaint.setColor(Color.WHITE);
                //canvas.drawCircle(right, top, 20, cyclePaint);
                //canvas.drawCircle(right, bottom, 20, cyclePaint);
                canvas.drawRect(rf1, p1);

                Paint p2=new Paint();
                p2.setAntiAlias(true);
                rf1 = new RectF(left, delta + top, mWidth, bottom - delta);
                p2.setColor(Color.parseColor(BitmapUtils.addAlpha(originalColor, alpha)));
                //canvas.drawCircle(right, top, 20, cyclePaint);
                //canvas.drawCircle(right, bottom, 20, cyclePaint);
                canvas.drawRect(rf1, p2);


                //设置画虚线，如果之后不再使用虚线，调用paint.setPathEffect(null);
                effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                path1 = new Path();
                path1.moveTo(right, top + 20);
                path1.lineTo(right, bottom - 20);
                dashPaint.setPathEffect(effects);
                canvas.drawPath(path1, dashPaint);


                // path1.moveTo(right, right * 1.5f);
                // path1.lineTo(mWidth, right * 1.5f);
                //canvas.drawPath(path1, dashPaint);

                break;

        }
    }

    public void setDrawBottomLeft(boolean drawBottomLeft) {
        isDrawBottomLeft = drawBottomLeft;
    }

    public void setDrawBottomRight(boolean drawBottomRight) {
        isDrawBottomRight = drawBottomRight;
    }

    public void setDrawTopLeft(boolean drawTopLeft) {
        isDrawTopLeft = drawTopLeft;
    }

    public void setDrawTopRight(boolean drawTopRight) {
        isDrawTopRight = drawTopRight;
    }

    private boolean isDrawBottomLeft = false;
    private boolean isDrawBottomRight = false;
    private boolean isDrawTopLeft = false;
    private boolean isDrawTopRight = false;

    public void setCycleColor(int mCycleColor) {
        cyclePaint.setColor(mCycleColor);
    }

    public void setCycleColor(String mCycleColor) {
        cyclePaint.setColor(Color.parseColor(mCycleColor));
    }

    private int mCycleColor = Color.WHITE;

    public void setDrawTicketTypeColor(int drawTicketTypeColor) {

        ticketTypePaint.setColor(getResources().getColor(drawTicketTypeColor));
    }

    public void setDrawTicketTypeColor(String colorValue) {

        ticketTypePaint.setColor(Color.parseColor(colorValue));
        invalidate();
    }

    private int mType = 1; //类型，主要用于区分要画的形状

}
