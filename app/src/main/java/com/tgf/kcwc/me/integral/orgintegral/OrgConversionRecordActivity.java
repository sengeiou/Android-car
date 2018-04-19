package com.tgf.kcwc.me.integral.orgintegral;

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
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.IntegralPurchaseRecordListBean;
import com.tgf.kcwc.mvp.model.IntegralRecordBean;
import com.tgf.kcwc.mvp.model.OrgIntegralRecordListBean;
import com.tgf.kcwc.mvp.presenter.OrgIntegralRecordPresenter;
import com.tgf.kcwc.mvp.view.OrgIntegralRecordView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.GoodDetailsRecordPopupWindow;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/10/25.
 * 机构积分兑换纪录
 */

public class OrgConversionRecordActivity extends BaseActivity implements OrgIntegralRecordView {

    OrgIntegralRecordPresenter mOrgIntegralRecordPresenter;
    private ListView mListView;
    private WrapAdapter<OrgIntegralRecordListBean.DataList> mAdapter;
    private List<OrgIntegralRecordListBean.DataList> datalist = new ArrayList<>();

    private TextView mAdd; //增加
    private TextView mSubtract; //减少

    int page = 1;
    boolean isRefresh = true;
    GoodDetailsRecordPopupWindow mGoodDetailsRecordPopupWindow;

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("兑换纪录");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_orgexchangerecord);
        initRefreshLayout(mBGDelegate);
        mOrgIntegralRecordPresenter = new OrgIntegralRecordPresenter();
        mOrgIntegralRecordPresenter.attachView(this);
        mListView = (ListView) findViewById(R.id.list);
        mAdd = (TextView) findViewById(R.id.add);
        mSubtract = (TextView) findViewById(R.id.subtract);
        setmAdapter();
        mOrgIntegralRecordPresenter.getOrgLogRecords(IOUtils.getToken(mContext), page);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //mOrgIntegralRecordPresenter.getRecordDetail(IOUtils.getToken(mContext), datalist.get(i).id + "");
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }


    public void setmAdapter() {
        mAdapter = new WrapAdapter<OrgIntegralRecordListBean.DataList>(mContext, R.layout.integral_recordlistview_item, datalist) {
            @Override
            public void convert(ViewHolder helper, OrgIntegralRecordListBean.DataList item) {
                TextView mTitle = helper.getView(R.id.title);
                TextView mTime = helper.getView(R.id.time);
                TextView mBonuspoint = helper.getView(R.id.bonuspoint);
                Account account = IOUtils.getAccount(mContext);

                if (account.org.id == item.buyOrgId) {
                    mTitle.setText("买入");
                    if (item.pointType == 1) {
                        mBonuspoint.setText("+" + item.points + "金币");
                    } else if (item.pointType == 2) {
                        mBonuspoint.setText("+" + item.points + "银元");
                    } else {
                        mBonuspoint.setText("+" + item.points + "钻石");
                    }
                    mBonuspoint.setTextColor(getResources().getColor(R.color.text_color36));
                } else {
                    mTitle.setText("售出");
                    if (item.pointType == 1) {
                        mBonuspoint.setText("-" + item.points + "金币");
                    } else if (item.pointType == 2) {
                        mBonuspoint.setText("-" + item.points + "银元");
                    } else {
                        mBonuspoint.setText("-" + item.points + "钻石");
                    }
                    mBonuspoint.setTextColor(getResources().getColor(R.color.tab_text_s_color));
                }
                mTime.setText(item.orderTime);
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
        mOrgIntegralRecordPresenter.getOrgLogRecords(IOUtils.getToken(mContext), page);
    }

    @Override
    public void DatalistSucceed(OrgIntegralRecordListBean integralListBean) {
        stopRefreshAll();
        if (page == 1) {
            datalist.clear();
            mAdd.setText(integralListBean.data.getPoints + "");
            mSubtract.setText(integralListBean.data.usedPoints + "");
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
    public void RecordSucceed(IntegralRecordBean integralListBean) {
        mGoodDetailsRecordPopupWindow = new GoodDetailsRecordPopupWindow(mContext, integralListBean.data);
        mGoodDetailsRecordPopupWindow.show(this);
    }

    @Override
    public void PurchaseRecordSucceed(IntegralPurchaseRecordListBean integralListBean) {

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
