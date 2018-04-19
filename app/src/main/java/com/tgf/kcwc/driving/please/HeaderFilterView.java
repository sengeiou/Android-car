package com.tgf.kcwc.driving.please;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.util.FilterPopwinUtil;
import com.tgf.kcwc.view.SmoothListView.AbsHeaderView;

/**
 * Created by Administrator on 2017/4/18.
 */

public class HeaderFilterView extends AbsHeaderView<Object> implements View.OnClickListener {

    private Activity activity;
    private LinearLayout mType;                                 //选择品牌
    private LinearLayout mRank;                               //选择区域
    private LinearLayout mGoTime;                               //选择时间
    private TextView mMore;                               //选择更多
    private Handler mHandler;

    public HeaderFilterView(Activity activity, Handler mHandler) {
        super(activity);
        this.activity = activity;
        this.mHandler = mHandler;
    }

    @Override
    protected void getView(Object o, ListView listView) {
        View view = mInflate.inflate(R.layout.please_filterview, listView, false);
        mType = (LinearLayout) view.findViewById(R.id.listact_categroy);
        mRank = (LinearLayout) view.findViewById(R.id.listact_rank);
        mGoTime = (LinearLayout) view.findViewById(R.id.listact_time);
        mMore = (TextView) view.findViewById(R.id.more);

        mType.setOnClickListener(this);
        mGoTime.setOnClickListener(this);
        mRank.setOnClickListener(this);
        mMore.setOnClickListener(this);
        FilterPopwinUtil.commonFilterTile(mType, "品牌");
        FilterPopwinUtil.commonFilterTile(mRank, "区域");
        FilterPopwinUtil.commonFilterTile(mGoTime, "时间");
        listView.addHeaderView(view);
    }

    public void setmType(String name) {
        FilterPopwinUtil.commonFilterTile(mType, name);
    }

    public void setmDestination(String name) {
        //FilterPopwinUtil.commonFilterTile(mDestination, name);
    }

    public void setmGoTime(String name) {
        FilterPopwinUtil.commonFilterTile(mGoTime, name);
    }

    public void setmRank(String name) {
        FilterPopwinUtil.commonFilterTile(mRank, name);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.listact_categroy:
                mHandler.sendEmptyMessage(PleasePlayFragment.MTYPENUM);
                break;
            case R.id.more:
                mHandler.sendEmptyMessage(PleasePlayFragment.MORE);
                break;
            case R.id.listact_rank:
                mHandler.sendEmptyMessage(PleasePlayFragment.MGOTIMENUM);
                break;
            case R.id.listact_time:
                mHandler.sendEmptyMessage(PleasePlayFragment.MTIMEENUM);
                break;
        }
    }
}
