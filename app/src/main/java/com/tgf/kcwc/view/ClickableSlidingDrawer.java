package com.tgf.kcwc.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SlidingDrawer;

/**
 * Author:Jenny
 * Date:2017/4/25
 * E-mail:fishloveqin@gmail.com
 * 重写SlidingDrawer解决子控件响应点击事件问题
 */

public class ClickableSlidingDrawer extends SlidingDrawer {
    private static final String TAG_CLICK_INTERCEPTED = "click_intercepted";

    private ViewGroup           mHandleLayout;
    private final Rect          mHitRect              = new Rect();

    public ClickableSlidingDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickableSlidingDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View handle = getHandle();

        if (handle instanceof ViewGroup) {
            mHandleLayout = (ViewGroup) handle;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        System.out.println("event:");
        if (mHandleLayout != null) {
            int clickX = (int) (mHandleLayout.getRight() - event.getX());
            int clickY = (int) (mHandleLayout.getBottom() - event.getY());

            if (isAnyClickableChildHit(mHandleLayout, clickX, clickY)) {
                return false;
            }

        }
        return super.onInterceptTouchEvent(event);
    }

    private boolean isAnyClickableChildHit(ViewGroup viewGroup, int clickX, int clickY) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childView = viewGroup.getChildAt(i);

            if (TAG_CLICK_INTERCEPTED.equals(childView.getTag())) {
                childView.getHitRect(mHitRect);

                if (mHitRect.contains(clickX, clickY)) {
                    return true;
                }
            }

            //主要处理当抽屉handle部分UI隐藏时，剩余控件还能响应拖动事件
            if ("title_tag".equals(childView.getTag())) {

                childView.getHitRect(mHitRect);
                if (mHitRect.contains(clickX, clickY)) {
                    return false;
                }

            }

            if (childView instanceof ViewGroup
                && isAnyClickableChildHit((ViewGroup) childView, clickX, clickY)) {
                return true;
            }
        }
        return false;
    }
}