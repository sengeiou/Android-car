package com.tgf.kcwc.adapter;

import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

public abstract class PinnedHeaderAdapter extends BaseAdapter
                                          implements SectionIndexer, OnScrollListener {
    public static final int PINNED_HEADER_GONE      = 0;
    public static final int PINNED_HEADER_PUSHED_UP = 2;
    public static final int PINNED_HEADER_VISIBLE   = 1;
    OnScrollListener        scrollListener;

    public abstract void configurePinnedHeader(View view, int position, int paramInt2);

    public int getCount() {
        return 0;
    }

    public Object getItem(int paramInt) {
        return null;
    }

    public long getItemId(int paramInt) {
        return 0L;
    }

    public int getPinnedHeaderState(int position) {

        return 1;
    }



    public static abstract interface OnScrollListener {
        public abstract void onScroll();
    }
}
