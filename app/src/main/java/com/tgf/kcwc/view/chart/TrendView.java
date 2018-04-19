package com.tgf.kcwc.view.chart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.tgf.kcwc.R;
import com.tgf.kcwc.mvp.model.TripBookMapModel;

/**
 * @anthor noti
 * @time 2017/11/2 13:46
 */

public class TrendView extends ChartView {
    //初始值
    private int mDisplayFrom;
    //总数
    private int mDisplayNumber;
    //昨收价格
    private double mPrvClose;
    //当前canvas中最大值
    private double mMaxValue;
    //当前canvas中最小值
    private double mMinValue;
    //当前canvas中最大值
    private double mMaxValue2;
    //当前canvas中最小值
    private double mMinValue2;
    //速度线条信息
    private Line mLine;
    //手指当前坐标
    private float mCurrentX = -1;

    private Path mPath;
    private Paint mTrendLinePaint;
    private Paint mAreaPaint;
    private Paint mVerticalPaint;
    private Paint bgPaint;
    private Paint selectBgPaint;
    //是否是第一次
    private boolean isFirst;
    public OnInvalidateCallBack onInvalidateCallBack;
    public interface OnInvalidateCallBack{
        void onInvalidateCall(int index);
    }

    public void setOnInvalidateCallBack(OnInvalidateCallBack onInvalidateCallBack) {
        this.onInvalidateCallBack = onInvalidateCallBack;
    }

    public TrendView(Context context) {
        super(context);
    }

    public TrendView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TrendView(Context context, AttributeSet attrs,
                     int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setupPaint() {
        super.setupPaint();
        mPath = new Path();
        mTrendLinePaint = new Paint();
        mTrendLinePaint.setAntiAlias(true);
        mTrendLinePaint.setStyle(Paint.Style.STROKE);
        mAreaPaint = new Paint();
        mVerticalPaint = new Paint();
        mVerticalPaint.setAntiAlias(true);
        mVerticalPaint.setColor(getResources().getColor(R.color.style_bg5));
        mVerticalPaint.setStrokeWidth(dp2px(1f));

        selectBgPaint = new Paint();
        selectBgPaint.setAntiAlias(true);
        selectBgPaint.setColor(getResources().getColor(R.color.text_color18));
        selectBgPaint.setStrokeWidth(dp2px(4f));

        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(getResources().getColor(R.color.style_bg7));
        bgPaint.setStrokeWidth(dp2px(4f));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Shader shader = new LinearGradient(0, 0, 0, h, new int[]{0xA0688FDB, 0x20688FDB}, null,
                Shader.TileMode.REPEAT);
        mAreaPaint.setShader(shader);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCurrentX = filterX(event.getX());
                break;
            case MotionEvent.ACTION_UP:
                mCurrentX = filterX(event.getX());
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentX = filterX(event.getX());
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calcMaxNMin();
        //绘制速度分时折线
        drawTrend(canvas);
        //绘制海拔分时折线
        drawElevationTrend(canvas);
//    drawArea(canvas);
        if (mCurrentX > 0) {
            drawVerticalLine(canvas);
//            drawVerticalCircle(canvas);
//            drawVerticalElevationCircle(canvas);
            drawSeek(canvas);
        }
        if (null != onInvalidateCallBack){
            onInvalidateCallBack.onInvalidateCall(getIndex());
        }
    }

//  /**
//   * 绘制从上往下的阴影
//   */
//  private void drawArea(Canvas canvas) {
//    mPath.reset();
//    for (int i = mDisplayFrom; i < mDisplayFrom + mDisplayNumber; i++) {
//      if (i == mDisplayFrom) {
//        mPath.moveTo(getValueX(i), mQuadrant.getQuadrantPaddingEndY());
//        mPath.lineTo(getValueX(i), getValueY(i));
//      } else if (i == mDisplayFrom + mDisplayNumber - 1) {
//        mPath.lineTo(getValueX(i), getValueY(i));
//        mPath.lineTo(getValueX(i), mQuadrant.getQuadrantPaddingEndY());
//        mPath.lineTo(getValueX(mDisplayFrom), mQuadrant.getQuadrantPaddingEndY());
//      } else {
//        mPath.lineTo(getValueX(i), getValueY(i));
//      }
//    }
//    canvas.drawPath(mPath, mAreaPaint);
//  }

    /**
     * 绘制速度分时折线
     */
    private void drawTrend(Canvas canvas) {
        mPath.reset();
        for (int i = mDisplayFrom + 1; i < mDisplayFrom + mDisplayNumber; i++) {
            if (i == mDisplayFrom + 1) {
                if (!isFirst) {
                    mCurrentX = getValueX(i - 1);
                    isFirst = true;
                }
                mPath.moveTo(getValueX(i - 1), getValueY(i - 1));
            }
            mPath.lineTo(getValueX(i), getValueY(i));
        }
        canvas.drawPath(mPath, mTrendLinePaint);
    }

    /**
     * 绘制海拔分时折线
     */
    private void drawElevationTrend(Canvas canvas) {
        mPath.reset();
        for (int i = mDisplayFrom + 1; i < mDisplayFrom + mDisplayNumber; i++) {
            if (i == mDisplayFrom + 1) {
                if (!isFirst) {
                    mCurrentX = getValueX(i - 1);
                    isFirst = true;
                }
                mPath.moveTo(getValueX(i - 1), getElevationValueY(i - 1));
            }
            mPath.lineTo(getValueX(i), getElevationValueY(i));
        }
        canvas.drawPath(mPath, mTrendLinePaint);
    }

    /**
     * 绘制竖线
     */
    private void drawVerticalLine(Canvas canvas) {
        canvas.drawLine(mCurrentX, mQuadrant.getQuadrantStartY(), mCurrentX,
                mQuadrant.getQuadrantEndY(), mVerticalPaint);
    }

    /**
     * 绘制小圆圈
     */
    private void drawVerticalCircle(Canvas canvas) {
        canvas.drawCircle(mCurrentX, getValueY(getIndex()), dp2px(4), mVerticalPaint);
    }

    /**
     * 绘制海拔小圆圈
     */
    private void drawVerticalElevationCircle(Canvas canvas) {
        canvas.drawCircle(mCurrentX, getElevationValueY(getIndex()), dp2px(4), mVerticalPaint);
    }

    /**
     * 绘制seekbar背景
     *
     * @param canvas
     */
    private void drawSeek(Canvas canvas) {
        //滑块
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.icon_thumb);
        //背景
        RectF bgRect = new RectF(getValueX(mDisplayFrom),mQuadrant.getQuadrantEndY()-dp2px(5)+thumb.getHeight()/2-dp2px(2),getValueX(mDisplayFrom+mDisplayNumber-1),mQuadrant.getQuadrantEndY()-dp2px(5)+thumb.getHeight()-dp2px(3));
        canvas.drawRoundRect(bgRect,dp2px(3f),dp2px(3f),bgPaint);
        //选中层背景
        RectF selectBgRect = new RectF(getValueX(mDisplayFrom),mQuadrant.getQuadrantEndY()-dp2px(5)+thumb.getHeight()/2-dp2px(2),mCurrentX,mQuadrant.getQuadrantEndY()-dp2px(5)+thumb.getHeight()-dp2px(3));
        canvas.drawRoundRect(selectBgRect,dp2px(3f),dp2px(3f),selectBgPaint);
        //绘制滑块
        canvas.drawBitmap(thumb, mCurrentX - thumb.getWidth()/2, mQuadrant.getQuadrantEndY()-dp2px(5), mVerticalPaint);
        if (thumb.isRecycled()) {
            thumb.recycle();
        }
    }

    /**
     * 计算当前canvas中的最大值和最小值
     */
    private void calcMaxNMin() {
        double maxValue = Double.MIN_VALUE;
        double minValue = Double.MAX_VALUE;
        //海拔相关
        double maxValue2 = Double.MIN_VALUE;
        double minValue2 = Double.MAX_VALUE;
        for (int i = mDisplayFrom; i < mDisplayFrom + mDisplayNumber; i++) {
            double value = getValue(i);
            double value2 = getElevationValue(i);

            if (value < minValue) {
                minValue = value;
            }
            if (value > maxValue) {
                maxValue = value;
            }
            //海拔相关
            if (value2 < minValue2) {
                minValue2 = value2;
            }
            if (value2 > maxValue2) {
                maxValue2 = value2;
            }
        }
        //取最大值
        double diff = Math.abs(maxValue - mPrvClose) > Math.abs(minValue - mPrvClose) ? Math.abs(
                maxValue - mPrvClose) : Math.abs(minValue - mPrvClose);
        double ElevationDiff = Math.abs(maxValue2 - mPrvClose) > Math.abs(minValue2 - mPrvClose) ? Math.abs(
                maxValue2 - mPrvClose) : Math.abs(minValue2 - mPrvClose);
        mMaxValue = mPrvClose + diff;
        mMinValue = mPrvClose - diff;
        mMaxValue2 = mPrvClose + ElevationDiff;
        mMinValue2 = mPrvClose - ElevationDiff;
    }

    /**
     * 计算第index个数据点的x坐标
     * X = （当前index – from）*（象限宽度 / (number-1)）+startX
     */
    private float getValueX(int index) {
        return (index - mDisplayFrom) * (mQuadrant.getQuadrantPaddingWidth() / (mDisplayNumber - 1))
                + mQuadrant.getQuadrantPaddingStartX();
    }

    /**
     * 计算第index个数据点的y坐标
     * Y = (1 - (value-min) / (max-min)) * height + startY
     */
    private float getValueY(int index) {
        return (float) (1 - ((getValue(index) - mMinValue) / (mMaxValue - mMinValue)))
                * mQuadrant.getQuadrantPaddingHeight()/2
                + mQuadrant.getQuadrantPaddingStartY();
    }

    /**
     * 计算第index个数据点的y坐标（海拔）
     * Y = (1 - (value-min) / (max-min)) * height + startY
     */
    private float getElevationValueY(int index) {
        return (float) (1 - ((getElevationValue(index) - mMinValue2) / (mMaxValue2 - mMinValue2)))
                * mQuadrant.getQuadrantPaddingHeight()/2
                + mQuadrant.getQuadrantPaddingStartY()+mQuadrant.getQuadrantPaddingHeight()/2+dp2px(2);
    }

    /**
     * 第index个数据点所对应的速度值
     */
    private double getValue(int index) {
//    return mLine.getLineData().get(index).getValue();
        return Double.parseDouble(mLine.getLineData().get(index).speed);
    }

    /**
     * 第index个数据点所对应的海拔值
     */
    private double getElevationValue(int index) {
//    return mLine.getLineData().get(index).getValue();
        return mLine.getLineData().get(index).altitude;
    }

    /**
     * 过滤x坐标
     */
    private float filterX(float x) {
        if (x < mQuadrant.getQuadrantPaddingStartX()) {
            x = mQuadrant.getQuadrantPaddingStartX();
        } else if (x > mQuadrant.getQuadrantPaddingEndX()) {
            x = mQuadrant.getQuadrantPaddingEndX();
        }
        return x;
    }

    /**
     * 获得mCurrentX所对应的index
     */
    private int getIndex() {
        //ratio = (x - startX) / width
        float ratio =
                (mCurrentX - mQuadrant.getQuadrantPaddingStartX()) / mQuadrant.getQuadrantPaddingWidth();
        //index = ratio * number + from
        int index = (int) (ratio * mDisplayNumber) + mDisplayFrom;
        if (index < mDisplayFrom) {
            index = mDisplayFrom;
        } else if (index > mDisplayFrom + mDisplayNumber - 1) {
            index = mDisplayFrom + mDisplayNumber - 1;
        }
        return index;
    }

    public TrendView withDisplayFrom(int from) {
        mDisplayFrom = from;
        return this;
    }

    public TrendView withDisplayNumber(int number) {
        mDisplayNumber = number;
        return this;
    }

    public TrendView withPrevClose(double prevClose) {
        mPrvClose = prevClose;
        return this;
    }

    public TrendView withLine(Line line) {
        mLine = line;
        mTrendLinePaint.setColor(getResources().getColor(R.color.text_selected));
        mTrendLinePaint.setStrokeWidth(3f);
        return this;
    }

    public void show() {
        invalidate();
        setVisibility(VISIBLE);
    }
}
