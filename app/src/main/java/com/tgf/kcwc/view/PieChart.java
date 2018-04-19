package com.tgf.kcwc.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tgf.kcwc.R;
import com.tgf.kcwc.entity.PieDataEntity;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CalculateUtil;
import com.tgf.kcwc.util.DataFormatUtil;

import java.util.List;

/**
 * Author:Jenny
 * Date:2017/10/17
 * E-mail:fishloveqin@gmail.com
 */

public class PieChart extends View {
    /**
     * 视图的宽和高
     */
    private int mTotalWidth, mTotalHeight;
    /**
     * 绘制区域的半径
     */
    private float mRadius;

    private Paint mPaint, mLinePaint, mTextPaint, mLinePaint2, mLinePaint3, mTextPaint2, mTextPaint3, mTextPaint4;

    private Path mPath;
    /**
     * 扇形的绘制区域
     */
    private RectF mRectF;
    /**
     * 点击之后的扇形的绘制区域
     */
    private RectF mRectFTouch;

    private List<PieDataEntity> mDataList;
    /**
     * 所有的数据加起来的总值
     */
    private float mTotalValue;
    /**
     * 起始角度的集合
     */
    private float[] angles;
    /**
     * 手点击的部分的position
     */
    private int position = -1;
    /**
     * 点击监听
     */
    private OnItemPieClickListener mOnItemPieClickListener;

    private int margin = 0;

    public void setOnItemPieClickListener(OnItemPieClickListener onItemPieClickListener) {
        mOnItemPieClickListener = onItemPieClickListener;
    }

    public interface OnItemPieClickListener {
        void onClick(int position);
    }

    public PieChart(Context context) {
        super(context);
        init(context);
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mRectF = new RectF();
        mRectFTouch = new RectF();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(2);
        mLinePaint.setColor(context.getResources().getColor(R.color.text_color17));


        mLinePaint2 = new Paint();
        mLinePaint2.setAntiAlias(true);
        mLinePaint2.setStyle(Paint.Style.FILL);
        mLinePaint2.setStrokeWidth(BitmapUtils.dp2px(context, 5));
        mLinePaint2.setColor(Color.WHITE);


        mLinePaint3 = new Paint();
        mLinePaint3.setAntiAlias(true);
        mLinePaint3.setStyle(Paint.Style.FILL);
        mLinePaint3.setStrokeWidth(BitmapUtils.dp2px(context, 10));
        mLinePaint3.setColor(Color.WHITE);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(24);


        int tSize = BitmapUtils.sp2px(context, 14);
        mTextPaint2 = new Paint();
        mTextPaint2.setAntiAlias(true);
        mTextPaint2.setStyle(Paint.Style.FILL);
        mTextPaint2.setTextSize(tSize);
        mTextPaint2.setColor(context.getResources().getColor(R.color.text_color2));


        mTextPaint3 = new Paint();
        mTextPaint3.setAntiAlias(true);
        mTextPaint3.setStyle(Paint.Style.FILL);
        mTextPaint3.setTextSize(tSize);
        mTextPaint3.setColor(context.getResources().getColor(R.color.text_color10));

        mTextPaint4 = new Paint();
        mTextPaint4.setAntiAlias(true);
        mTextPaint4.setStyle(Paint.Style.FILL);
        mTextPaint4.setTextSize(tSize);
        mTextPaint4.setColor(context.getResources().getColor(R.color.text_color18));
        margin = BitmapUtils.dp2px(getContext(), 30);
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w - getPaddingLeft() - getPaddingRight();
        mTotalHeight = h - getPaddingTop() - getPaddingBottom();

        mRadius = (float) (Math.min(mTotalWidth, mTotalHeight) / 2 * 0.7);
        mRadius = mRadius - 20;
        mRectF.left = -mRadius;
        mRectF.top = -mRadius;
        mRectF.right = mRadius;
        mRectF.bottom = mRadius;

        mRectFTouch.left = -mRadius - 16;
        mRectFTouch.top = -mRadius - 16;
        mRectFTouch.right = mRadius + 16;
        mRectFTouch.bottom = mRadius + 16;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDataList == null)
            return;
        canvas.translate(mTotalWidth / 2, mTotalHeight / 2);
        //绘制饼图的每块区域
        drawPiePath(canvas);
    }

    /**
     * 绘制饼图的每块区域 和文本
     *
     * @param canvas
     */
    private void drawPiePath(Canvas canvas) {
        //起始地角度
        float startAngle = 0;
        int size = mDataList.size();
        for (int i = 0; i < size; i++) {
            PieDataEntity entity = mDataList.get(i);
            float sweepAngle = entity.getValue() / mTotalValue * 360;//每个扇形的角度
            mPaint.setColor(entity.getColor());
            //*******下面的两种方法选其一就可以 一个是通过画路径来实现 一个是直接绘制扇形***********
//            mPath.moveTo(0,0);
//            if(position-1==i){
//                mPath.arcTo(mRectFTouch,startAngle,sweepAngle);
//            }else {
//                mPath.arcTo(mRectF,startAngle,sweepAngle);
//            }
//            canvas.drawPath(mPath,mPaint);
//            mPath.reset();
//            canvas.drawArc(mRectF,startAngle,sweepAngle,true,mPaint);
//            if (position - 1 == i) {
//                canvas.drawArc(mRectFTouch, startAngle, sweepAngle, true, mPaint);
//            } else {
//
//            }
            canvas.drawArc(mRectF, startAngle, sweepAngle, true, mPaint);
            Log.i("toRadians", (startAngle + sweepAngle / 2) + "****" + Math.toRadians(startAngle + sweepAngle / 2));
            //确定直线的起始和结束的点的位置
            float pxs = (float) (mRadius * Math.cos(Math.toRadians(startAngle + sweepAngle / 2)));
            float pys = (float) (mRadius * Math.sin(Math.toRadians(startAngle + sweepAngle / 2)));
            float pxt = (float) ((mRadius + 30) * Math.cos(Math.toRadians(startAngle + sweepAngle / 2)));
            float pyt = (float) ((mRadius + 30) * Math.sin(Math.toRadians(startAngle + sweepAngle / 2)));
            float pxs1 = (float) (mRadius * Math.cos(Math.toRadians(startAngle)));
            float pys1 = (float) (mRadius * Math.sin(Math.toRadians(startAngle)));

            float pxs2 = (float) (mRadius * Math.cos(Math.toRadians(startAngle + sweepAngle)));
            float pys2 = (float) (mRadius * Math.sin(Math.toRadians(startAngle + sweepAngle)));



            //绘制线和文本
            // canvas.drawLine(pxs, pys, pxt, pyt, mLinePaint);

            float x = (Math.abs(mRectF.right) - Math.abs(mRectF.left)) / 2;
            float y = (Math.abs(mRectF.bottom) - Math.abs(mRectF.top)) / 2;
            if (i % 2 == 0) {
                canvas.drawLine(x, y, pxs2, pys2, mLinePaint2);
                canvas.drawLine(x, y, pxs1, pys1, mLinePaint2);

            }


            float res = entity.getValue() / mTotalValue * 100;
            //提供精确的小数位四舍五入处理。
            double resToRound = CalculateUtil.round(res, 2);
            float v = startAngle % 360;
//            if (startAngle % 360.0 >= 90.0 && startAngle % 360.0 <= 270.0) {//2 3 象限
//                canvas.drawLine(pxt, pyt, pxt + margin, pyt, mLinePaint);
//                //canvas.drawText(resToRound + "%", pxt - mTextPaint.measureText(resToRound + "%") - 30, pyt, mTextPaint);
//                canvas.drawText(entity.getName(), pxt, pyt - margin, mTextPaint2);
//
//            } else {
//                canvas.drawLine(pxt, pyt, pxt-margin, pyt, mLinePaint);
//                //canvas.drawText(resToRound + "%", pxt + 30, pyt, mTextPaint);
//                canvas.drawText(entity.getName(), pxt, pyt - margin, mTextPaint2);
//            }


            float pxt3 = (float) ((mRadius + 30) * Math.cos(Math.toRadians(startAngle)));
            float pyt3 = (float) ((mRadius + 30) * Math.sin(Math.toRadians(startAngle + sweepAngle / 2)));

            float pxt4 = (float) ((mRadius + 30) * Math.cos(Math.toRadians(startAngle)));
            float pyt4 = (float) ((mRadius + 30) * Math.sin(Math.toRadians(startAngle + sweepAngle / 2)));
            if (i == 0) {
                // canvas.drawText("￥" + entity.getValue() + "", pxt3 + margin, pyt3 + margin * 2f, mTextPaint3);
                canvas.drawLine( pxt / 2, pyt/2, mRectF.right, mRectF.bottom, mLinePaint);
                drawOutlay(canvas, entity);
            } else if (i == 1) {
                //canvas.drawText("￥" + entity.getValue() + "", pxt, pyt + margin * 2f, mTextPaint4);
                canvas.drawLine(pxt / 2, pyt/2, mRectF.right, mRectF.top, mLinePaint);

                drawIncome(canvas, entity);
            }

            angles[i] = startAngle;
            startAngle += sweepAngle;
        }

    }

    private void drawIncome(Canvas canvas, PieDataEntity entity) {

        canvas.drawText(entity.getName(), mRectF.right, mRectF.top - margin / 3, mTextPaint2);
        canvas.drawText("￥" + DataFormatUtil.getFormatMoney(entity.getValue() + ""), mRectF.right, mRectF.top + margin * 0.6f, mTextPaint3);
        canvas.drawLine(mRectF.right, mRectF.top, mRectF.right + margin * 2.5f, mRectF.top, mLinePaint);
    }

    private void drawOutlay(Canvas canvas, PieDataEntity entity) {
        canvas.drawText(entity.getName(), mRectF.right, mRectF.bottom - margin / 3, mTextPaint2);
        canvas.drawText("￥" + DataFormatUtil.getFormatMoney(entity.getValue() + ""), mRectF.right, mRectF.bottom + margin * 0.6f, mTextPaint4);
        canvas.drawLine(mRectF.right, mRectF.bottom, mRectF.right + margin * 2.5f, mRectF.bottom, mLinePaint);
    }

    public void setDataList(List<PieDataEntity> dataList) {
        this.mDataList = dataList;
        mTotalValue = 0;
        for (PieDataEntity pieData : mDataList) {
            mTotalValue += pieData.getValue();
        }
        angles = new float[mDataList.size()];
        invalidate();
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                float x = event.getX() - (mTotalWidth / 2);
//                float y = event.getY() - (mTotalHeight / 2);
//                float touchAngle = 0;
//                if (x < 0 && y < 0) {  //2 象限
//                    touchAngle += 180;
//                } else if (y < 0 && x > 0) {  //1象限
//                    touchAngle += 360;
//                } else if (y > 0 && x < 0) {  //3象限
//                    touchAngle += 180;
//                }
//                //Math.atan(y/x) 返回正数值表示相对于 x 轴的逆时针转角，返回负数值则表示顺时针转角。
//                //返回值乘以 180/π，将弧度转换为角度。
//                touchAngle += Math.toDegrees(Math.atan(y / x));
//                if (touchAngle < 0) {
//                    touchAngle = touchAngle + 360;
//                }
//                float touchRadius = (float) Math.sqrt(y * y + x * x);
//                if (touchRadius < mRadius) {
//                    position = -Arrays.binarySearch(angles, (touchAngle)) - 1;
//                    invalidate();
//                    if (mOnItemPieClickListener != null) {
//                        mOnItemPieClickListener.onClick(position - 1);
//                    }
//                }
//                break;
//        }
//        return super.onTouchEvent(event);
//    }
}