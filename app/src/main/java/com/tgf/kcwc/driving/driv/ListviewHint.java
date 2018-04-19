package com.tgf.kcwc.driving.driv;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.view.SmoothListView.AbsHeaderView;

/**
 * Created by Administrator on 2017/4/19.
 */

public class ListviewHint extends LinearLayout {

    private Context mContext;

    public ListviewHint(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        RelativeLayout moreView = (RelativeLayout) LayoutInflater.from(mContext) .inflate(R.layout.bottom_hint_layout, null);
        moreView .setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        addView(moreView);

    }
}
