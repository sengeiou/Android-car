package com.tgf.kcwc.view.rangeseekbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.TypedValue;

import com.tgf.kcwc.R;
import com.tgf.kcwc.util.DensityUtils;


public class Thumb {

    // 私有静态变量

    // The radius (in dp) of the touchable area around the thumb. We are basing
    // this value off of the recommended 48dp Rhythm. See:
    // http://developer.android.com/design/style/metrics-grids.html#48dp-rhythm
    private static final float MINIMUM_TARGET_RADIUS_DP = 24;

    // Sets the default values for radius, normal, pressed if circle is to be
    // drawn but no value is given.
    private static final float DEFAULT_THUMB_RADIUS_DP = 14;

    // Corresponds to android.R.color.holo_blue_light.
    private static final int DEFAULT_THUMB_COLOR_NORMAL = 0xff33b5e5;
    private static final int DEFAULT_THUMB_COLOR_PRESSED = 0xff33b5e5;


    // Radius (in pixels) of the touch area of the thumb.
    private final float mTargetRadiusPx;

    // The normal and pressed images to display for the thumbs.
    private final Bitmap mImageNormal;
    private final Bitmap mImagePressed;

    // Variables to store half the width/height for easier calculation.
    private final float mHalfWidthNormal;
    private final float mHalfHeightNormal;

    private final float mHalfWidthPressed;
    private final float mHalfHeightPressed;

    // Indicates whether this thumb is currently pressed and active.
    private boolean mIsPressed = false;

    // The y-position of the thumb in the parent view. This should not change.
    private final float mY;

    // The current x-position of the thumb in the parent view.
    private float mX;

    // mPaint to draw the thumbs if attributes are selected
    private Paint mPaintNormal;
    private Paint mPaintPressed;

    // Radius of the new thumb if selected
    private float mThumbRadiusPx;

    // Toggle to select bitmap thumbImage or not
    private boolean mUseBitmap;

    // Colors of the thumbs if they are to be drawn
    private int mThumbColorNormal;
    private int mThumbColorPressed;
private  Context mContext;

    public Thumb(Context ctx,
          float y,
          int thumbColorNormal,
          int thumbColorPressed,
          float thumbRadiusDP,
          int thumbImageNormal,
          int thumbImagePressed) {

        final Resources res = ctx.getResources();
        mContext = ctx;
        mImageNormal = BitmapFactory.decodeResource(res, thumbImageNormal);
        mImagePressed = BitmapFactory.decodeResource(res, thumbImagePressed);

        // If any of the attributes are set, toggle bitmap off
        if (thumbRadiusDP == -1 && thumbColorNormal == -1 && thumbColorPressed == -1) {

            mUseBitmap = true;

        } else {

            mUseBitmap = false;

            // If one of the attributes are set, but the others aren't, set the
            // attributes to default
            if (thumbRadiusDP == -1)
                mThumbRadiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        DEFAULT_THUMB_RADIUS_DP,
                        res.getDisplayMetrics());
            else
                mThumbRadiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        thumbRadiusDP,
                        res.getDisplayMetrics());

            if (thumbColorNormal == -1)
                mThumbColorNormal = DEFAULT_THUMB_COLOR_NORMAL;
            else
                mThumbColorNormal = thumbColorNormal;

            if (thumbColorPressed == -1)
                mThumbColorPressed = DEFAULT_THUMB_COLOR_PRESSED;
            else
                mThumbColorPressed = thumbColorPressed;

            // Creates the paint and sets the Paint values
            mPaintNormal = new Paint();
            mPaintNormal.setColor(mThumbColorNormal);
            mPaintNormal.setAntiAlias(true);

            mPaintPressed = new Paint();
            mPaintPressed.setColor(mThumbColorPressed);
            mPaintPressed.setAntiAlias(true);
        }

        mHalfWidthNormal = mImageNormal.getWidth() / 2f;
        mHalfHeightNormal = mImageNormal.getHeight() / 2f;

        mHalfWidthPressed = mImagePressed.getWidth() / 2f;
        mHalfHeightPressed = mImagePressed.getHeight() / 2f;

        // Sets the minimum touchable area, but allows it to expand based on
        // image size
        int targetRadius = (int) Math.max(MINIMUM_TARGET_RADIUS_DP, thumbRadiusDP);

        mTargetRadiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                targetRadius,
                res.getDisplayMetrics());

        mX = mHalfWidthNormal;
        mY = y;
    }


    public float getHalfWidth() {
        return mHalfWidthNormal;
    }

    public float getHalfHeight() {
        return mHalfHeightNormal;
    }

    public void setX(float x) {
        mX = x;
    }

    public float getX() {
        return mX;
    }

    public boolean isPressed() {
        return mIsPressed;
    }

    public void press() {
        mIsPressed = true;
    }

    public void release() {
        mIsPressed = false;
    }

    /**
     * Determines if the input coordinate is close enough to this thumb to
     * consider it a press.
     *
     * @param x the x-coordinate of the user touch
     * @param y the y-coordinate of the user touch
     * @return true if the coordinates are within this thumb's target area;
     *         false otherwise
     */
    public boolean isInTargetZone(float x, float y) {

        if (Math.abs(x - mX) <= mTargetRadiusPx && Math.abs(y - mY) <= mTargetRadiusPx) {
            return true;
        }
        return false;
    }

    /**
     * Draws this thumb on the provided canvas.
     *
     * @param canvas Canvas to draw on; should be the Canvas passed into {#link
     *            View#onDraw()}
     */
    public void draw(Canvas canvas,int index) {

        // If a bitmap is to be printed. Determined by thumbRadius attribute.
        if (mUseBitmap) {

            final Bitmap bitmap = (mIsPressed) ? mImagePressed : mImageNormal;

            final float topPressed = mY - mHalfHeightPressed;
            final float leftPressed = mX - mHalfWidthPressed;
            canvas.drawBitmap(bitmap, leftPressed, topPressed, null);
            Paint paint =  new Paint();
//            mContext.getResources().getColor(R.color.text_nav_def_color);
            paint.setColor(mContext.getResources().getColor(R.color.tab_text_s_color));
           int textSize=  DensityUtils.sp2px(mContext,12);
            paint.setTextSize(textSize);
            int margintopseekbar = DensityUtils.dp2px(mContext,20);
            String text =""+(index+1);
            canvas.drawText(text,mX-(text.length()*textSize)/4,mY-margintopseekbar,paint);


//            if (mIsPressed) {
//                final float topPressed = mY - mHalfHeightPressed;
//                final float leftPressed = mX - mHalfWidthPressed;
//                canvas.drawBitmap(bitmap, leftPressed, topPressed, null);
//                Paint paint =  new Paint();
//                paint.setColor(Color.BLACK);
//                paint.setTextSize((float)22);
//                canvas.drawText(""+index,mX-6,topPressed-3,paint);
//            } else {
//                final float topNormal = mY - mHalfHeightNormal;
//                final float leftNormal = mX - mHalfWidthNormal;
//                Paint paint =  new Paint();
//                paint.setColor(Color.BLACK);
//                paint.setTextSize((float)22);
//                canvas.drawText(""+index,40,40,paint);
//                canvas.drawBitmap(bitmap, leftNormal, topNormal, null);
//            }

        } else {
            if (mIsPressed)
                canvas.drawCircle(mX, mY, mThumbRadiusPx, mPaintPressed);
            else
                canvas.drawCircle(mX, mY, mThumbRadiusPx, mPaintNormal);
        }
    }
}
