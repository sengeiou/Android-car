package com.tgf.kcwc.view.autoscrolltext;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Author:Jenny
 * Date:2017/11/7
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 循环滚动的ListView
 */

public class AutoCircleScrollListView extends ListView implements AbsListView.OnScrollListener {
    public final static int REFRESH_TIMES = 1000;//滚动频率,单位毫秒
    public final static int SCROLL_DISTANCE = 10;//每次滚动距离，单位是pixels
    public final static int REPEAT_TIMES = 3 * 1000 * 1000;//重复添加数据的次数，主要用于模拟无限循环滚动
    private Handler mHandler = new Handler();
    private RefreshRunnable mRefreshRunnable;
    private boolean isStop = false;

    public AutoCircleScrollListView(Context context) {
        super(context);
        init();
    }

    public AutoCircleScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //设置滚动监听器
        setOnScrollListener(this);
        //不显示滚动条
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        //不可触摸
        setEnabled(false);
    }

    /**
     * 开始滚动
     */
    public void startScroll() {
        isStop = false;
        count = getCount();
        setSelection(getCount() / REPEAT_TIMES);
        if (mRefreshRunnable == null) {
            mRefreshRunnable = new RefreshRunnable();
        }
        mHandler.postDelayed(mRefreshRunnable, REFRESH_TIMES);
    }

    int count = 0;

    /**
     * 停止滚动
     */
    private void stopScroll() {
        isStop = true;
        if (mRefreshRunnable != null) {
            mHandler.removeCallbacks(mRefreshRunnable);
        }
        mRefreshRunnable = null;
    }


    /**
     * 更新UI线程
     */
    private class RefreshRunnable implements Runnable {


        @Override
        public void run() {
            if (!isStop) {
                int height = getHeight();
                int count = getCount() / REPEAT_TIMES;
                int avg = REFRESH_TIMES / count;
                smoothScrollBy(height / count, avg);
                mHandler.postDelayed(this, REFRESH_TIMES);
            }
        }

    }

    private int index;
    private int totalDistance;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem <= 2) {//到顶部
            int listSize = getCount() / REPEAT_TIMES;
            setSelection(listSize + 2);
        } else if (firstVisibleItem + visibleItemCount > getCount() - 2) {//到底部
            int listSize = getCount() / REPEAT_TIMES;
            setSelection(firstVisibleItem - listSize);

        }


    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {


    }

    public void onStop() {
        if (!isStop) {
            stopScroll();
        }

    }

    public void onResume() {
        if (isStop) {
            startScroll();
        }
    }


}