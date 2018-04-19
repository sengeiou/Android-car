package com.tgf.kcwc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

import com.tgf.kcwc.logger.Logger;

/**
 * Auther: Scott
 * Date: 2016/12/12 0012
 * E-mail:hekescott@qq.com
 */
public class MyListView extends ListView {

    private static final String TAG = "MyListView";

    public MyListView(Context context) {
        this(context, null);
    }

    public MyListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

//    private int measureHight(int size, int measureSpec) {
//        int result = 0;
//        int specMode = MeasureSpec.getMode(measureSpec);
//        int specSize = MeasureSpec.getSize(measureSpec);
//        if (specMode == MeasureSpec.EXACTLY) {
//            Log.i(TAG, "exactly");
//
//            result = specSize;
//        } else {
//
//            result = size;//最小值是200px ，自己设定
//            if (specMode == MeasureSpec.AT_MOST) {
//                result = Math.min(result, specSize);
//            }
//            Log.i(TAG, "specMode:" + specMode + "--result:" + result);
//        }
//        return result;
//    }

//    /**
//     * 改listview滑到底端了
//     *
//     * @return
//     */
//    public boolean isBottom() {
//        int firstVisibleItem = getFirstVisiblePosition();//屏幕上显示的第一条是list中的第几条
//        int childcount = getChildCount();//屏幕上显示多少条item
//        int totalItemCount = getCount();//一共有多少条
//        if ((firstVisibleItem + childcount) >= totalItemCount) {
//            return true;
//        }
//        return false;
//    }

    /**
     * 改listview在顶端
     *
     * @return
     */
//    public boolean isTop() {
//        int firstVisibleItem = getFirstVisiblePosition();
//        if (firstVisibleItem == 0) {
//            return true;
//        }
//        return false;
//    }

//    float down = 0;
//    float y;


//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                Logger.d("onInterceptTouchEvent    down ==" + ev.getRawY());
//                down = ev.getRawY();
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        Logger.d("dispatchTouchEvent   y ==" + y + "  down ==" + down);
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                down = event.getRawY();
//                Logger.d("ACTION_DOWN");
//                Logger.d("ACTION_DOWN   y ==" + y + "  down ==" + down);
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Logger.d("ACTION_MOVE   isTop()" + isTop());
//                Logger.d("ACTION_MOVE   isBottom()()" + isBottom());
//                y = event.getRawY();
//                Logger.d("ACTION_MOVE   y ==" + y + "  down ==" + down);
//                if (isTop()) {
//                    if (y - down > 1) {
//                        //                        到顶端,向下滑动 把事件教给父类
//                        getParent().requestDisallowInterceptTouchEvent(false);
//                    } else {
//                        //                        到顶端,向上滑动 把事件拦截 由自己处理
//                        getParent().requestDisallowInterceptTouchEvent(true);
//                    }
//                }
//
//                if (isBottom()) {
//                    if (y - down > 1) {
//                        //                        到底端,向下滑动 把事件拦截 由自己处理
//                        getParent().requestDisallowInterceptTouchEvent(true);
//                    } else {
//                        //                        到底端,向上滑动 把事件教给父类
//                        getParent().requestDisallowInterceptTouchEvent(false);
//                    }
//                }
//
//                if(!isBottom()&&!isTop()){
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                Logger.d("ACTION_UP" + event.getRawY());
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                Logger.d("ACTION_CANCEL" + event.getRawY());
//                break;
//            default:
//                Logger.d("dispatchTouchEvent   event ==" + event.getRawY() + event.getAction());
//                break;
//        }
//
//        return super.dispatchTouchEvent(event);
//    }
}
