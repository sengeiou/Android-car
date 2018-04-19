package com.tgf.kcwc.me.integral;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.mvp.model.IntegralPurchaseRecordListBean;
import com.tgf.kcwc.mvp.model.IntegralRecordBean;
import com.tgf.kcwc.mvp.model.IntegralRecordListBean;
import com.tgf.kcwc.mvp.presenter.IntegralRecordPresenter;
import com.tgf.kcwc.mvp.view.IntegralRecordView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.GoodDetailsRecordPopupWindow;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/10/25.
 * 积分交易纪录
 */

public class ConversionPurchaseRecordActivity extends BaseActivity implements IntegralRecordView {

    IntegralRecordPresenter mIntegralRecordPresenter;
    private ListView mListView;
    private WrapAdapter<IntegralPurchaseRecordListBean.DataList> mAdapter;
    private List<IntegralPurchaseRecordListBean.DataList> datalist = new ArrayList<>();
    int page = 1;
    boolean isRefresh = true;

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("交易纪录");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_exchangerecord);
        initRefreshLayout(mBGDelegate);
        mIntegralRecordPresenter = new IntegralRecordPresenter();
        mIntegralRecordPresenter.attachView(this);
        mListView = (ListView) findViewById(R.id.list);
        setmAdapter();
        mIntegralRecordPresenter.getLogRecords(IOUtils.getToken(mContext), page);
        //mIntegralRecordPresenter.getLogRecords("wvLlKLu48D9oMaWjLjzNfl2CCby3SGXx", page);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }


    public void setmAdapter() {
        mAdapter = new WrapAdapter<IntegralPurchaseRecordListBean.DataList>(mContext, R.layout.integral_purchase_recordlistview_item, datalist) {
            @Override
            public void convert(ViewHolder helper, IntegralPurchaseRecordListBean.DataList item) {
                TextView mTitle = helper.getView(R.id.title);
                TextView mName = helper.getView(R.id.name);
                TextView mTime = helper.getView(R.id.time);
                TextView mBonuspoint = helper.getView(R.id.bonuspoint);


                if (item.pointType == 1) {
                    mTitle.setText(item.points + "金币");
                } else if (item.pointType == 2) {
                    mTitle.setText(item.points + "银元");
                } else {
                    mTitle.setText(item.points + "钻石");
                }
                mTime.setText(item.payTime);
                mBonuspoint.setText("￥" + item.price + "分");

            }
        };
        mListView.setAdapter(mAdapter);
    }


    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            // 在这里加载最新数据

            //            if (HttpUtils.isConnectNetwork(getApplicationContext())) {
            //                // 如果网络可用，则加载网络数据
            //
            //                loadMore();
            //            } else {
            //                // 网络不可用，结束下拉刷新
            //                HttpUtils.registerNWReceiver(getApplicationContext());
            //                mRefreshLayout.endRefreshing();
            //            }
            // mPageIndex = 1;
            page = 1;
            isRefresh = true;
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以

            //            if (HttpUtils.isConnectNetwork(getApplicationContext())) {
            //                loadMore();
            //                return true;
            //            } else {
            //                // 网络不可用，返回false，不显示正在加载更多
            //                HttpUtils.registerNWReceiver(getApplicationContext());
            //                mRefreshLayout.endRefreshing();
            //                return false;
            //            }
            //loadMore();
            if (isRefresh) {
                page++;
                loadMore();
            }
            return false;
        }
    };


    public void loadMore() {
        mIntegralRecordPresenter.getLogRecords(IOUtils.getToken(mContext), page);
    }

    @Override
    public void DatalistSucceed(IntegralRecordListBean integralListBean) {

    }

    @Override
    public void RecordSucceed(IntegralRecordBean integralListBean) {
    }

    @Override
    public void PurchaseRecordSucceed(IntegralPurchaseRecordListBean integralListBean) {
        stopRefreshAll();
        if (page == 1) {
            datalist.clear();
        }
        if (integralListBean.data.list != null && integralListBean.data.list.size() != 0) {
            datalist.addAll(integralListBean.data.list);
        } else {
            isRefresh = false;
        }
        if (page == 1) {
            setmAdapter();
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {
        CommonUtils.showToast(mContext, "系统异常");
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
