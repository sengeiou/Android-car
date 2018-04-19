package com.tgf.kcwc.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.tgf.kcwc.R;
import com.tgf.kcwc.util.DensityUtils;

/**
 * Auther: Scott
 * Date: 2016/12/12 0012
 * E-mail:hekescott@qq.com
 */
public class LoadView extends View {
    private Paint paint;
    private int maxRadius = 16;
    private ValueAnimator valueAnimator;
    private boolean init = false;
    private float radiu = 10;
    private boolean isRun =true ;
    private  Context mContext;
    public LoadView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public LoadView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {

        mContext = context;
        paint = new Paint();
//        paint.setColor(Color.RED);
        radiu = DensityUtils.dp2px(context,10);
        maxRadius = DensityUtils.dp2px(context,16);
        paint.setColor(getResources().getColor(R.color.app_main_color2));
    }

    private int width;
    private int height;
    private float pi2;
    private float r;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!init) {
            init = true;
            start();
            width = getWidth() / 2;
            height = getHeight() / 2;
            pi2 = 2*(float)Math.PI;
            r = width-maxRadius;
        }

        canvas.drawCircle((float) (width + r * Math.sin(0)), (float) (height + r * Math.cos(0)), f(radiu+0), paint);
        canvas.drawCircle((float) (width + r * Math.sin(pi2 /8)), (float) (height + r * Math.cos(pi2 /8)), f(radiu+2), paint);
        canvas.drawCircle((float) (width + r * Math.sin(pi2 /8*2)), (float) (height + r * Math.cos(pi2 /8*2)), f(radiu+4), paint);
        canvas.drawCircle((float) (width + r * Math.sin(pi2 /8*3)), (float) (height + r * Math.cos(pi2 /8*3)), f(radiu+6), paint);

        canvas.drawCircle((float) (width + r * Math.sin(pi2 /8*4)), (float) (height + r * Math.cos(pi2 /8*4)), f(radiu+8), paint);
        canvas.drawCircle((float) (width + r * Math.sin(pi2 /8*5)), (float) (height + r * Math.cos(pi2 /8*5)), f(radiu+10), paint);
        canvas.drawCircle((float) (width + r * Math.sin(pi2 /8*6)), (float) (height + r * Math.cos(pi2 /8*6)), f(radiu+12), paint);
        canvas.drawCircle((float) (width + r * Math.sin(pi2 /8*7)), (float) (height + r * Math.cos(pi2 /8*7)), f(radiu+14), paint);

        if (valueAnimator.isRunning()) {
            radiu = (float) valueAnimator.getAnimatedValue();
            invalidate();
        }


    }


    private void start() {
        if(!isRun){
            return;
        }
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(0, maxRadius);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setDuration(1000);
            valueAnimator.start();
        }else {
            valueAnimator.start();
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                start();
                invalidate();
            }
        }, valueAnimator.getDuration());
        invalidate();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        switch (visibility){
            case View.GONE:
            case View.INVISIBLE:
                stop();
                break;
            case View.VISIBLE:
                isRun =true;
                start();
                break;

        }
    }

    private void stop() {
        isRun = false;
    }

    //分段函数
    private float f(float x) {
        if (x <=maxRadius / 2) {
            return x;
        } else if(x<maxRadius){
            return maxRadius - x;
        }else
        if(x<maxRadius*3/2)
        {
            return x-maxRadius;
        }else {
            return 2*maxRadius-x;
        }
    }

    public void setWidth(int width) {
        this.width = DensityUtils.dp2px(mContext,width);
    }

    public void setHeight(int height) {
        this.height = DensityUtils.dp2px(mContext,height);
    }
}
