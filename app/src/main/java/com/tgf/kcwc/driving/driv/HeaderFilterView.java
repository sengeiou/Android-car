package com.tgf.kcwc.driving.driv;

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
    private LinearLayout mType;                                 //选择类型
    private LinearLayout mDestination;                          //选择目的地
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
        View view = mInflate.inflate(R.layout.activity_drivi_filter, listView, false);
        mType = (LinearLayout) view.findViewById(R.id.listact_categroy);
        mDestination = (LinearLayout) view.findViewById(R.id.listact_kilometer);
        mGoTime = (LinearLayout) view.findViewById(R.id.listact_rank);
        mMore = (TextView) view.findViewById(R.id.more);

        mType.setOnClickListener(this);
        mDestination.setOnClickListener(this);
        mGoTime.setOnClickListener(this);
        mMore.setOnClickListener(this);
        FilterPopwinUtil.commonFilterTile(mType, "出发地");
        FilterPopwinUtil.commonFilterTile(mDestination, "目的地");
        FilterPopwinUtil.commonFilterTile(mGoTime, "出发时间");
        listView.addHeaderView(view);
    }

    public void setmType(String name) {
        FilterPopwinUtil.commonFilterTile(mType, name);
    }

    public void setmDestination(String name) {
        FilterPopwinUtil.commonFilterTile(mDestination, name);
    }

    public void setmGoTime(String name) {
        FilterPopwinUtil.commonFilterTile(mGoTime, name);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.listact_categroy:
                mHandler.sendEmptyMessage(DrivingFragment.MTYPENUM);
                break;
            case R.id.listact_kilometer:
                mHandler.sendEmptyMessage(DrivingFragment.MDESTINATIONNUM);
                break;
            case R.id.listact_rank:
                mHandler.sendEmptyMessage(DrivingFragment.MGOTIMENUM);
                break;
            case R.id.more:
                mHandler.sendEmptyMessage(DrivingFragment.MORE);
                break;
        }
    }
}
