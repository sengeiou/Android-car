package com.tgf.kcwc.me.integral.orgintegral;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.IntegralDiamondListBean;
import com.tgf.kcwc.mvp.model.IntegralPurchaseBean;
import com.tgf.kcwc.mvp.model.OrgDetailsBean;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.presenter.OrgIntegralDiamondPresenter;
import com.tgf.kcwc.mvp.view.OrgIntegralDiamondView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PurchaseGoodDetailsPopupWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/11/1.
 * 机构购买主页
 */

public class OrgIntegralConcersionActivity extends BaseActivity implements OrgIntegralDiamondView {

    OrgIntegralDiamondPresenter mOrgIntegralDiamondPresenter;
    private ListView mListView;
    private WrapAdapter<IntegralDiamondListBean.DataList> mAdapter;
    private List<IntegralDiamondListBean.DataList> datalist = new ArrayList<>();
    private TextView mFound;
    private SimpleDraweeView mSimpleDraweeView;//头像
    private TextView mName; //名字
    private ImageView mModel; //模特
    private ImageView mConvene; //达人
    private SimpleDraweeView mBrandLogo; //logo
    public TextView mLooseChange;//零钱

    int page = 1;
    boolean isRefresh = true;
    public int selectCommodity = 0;

    OrgDetailsBean mOrgDetailsBean;

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("机构积分交易");
        function.setTextResource("纪录", R.color.white, 15);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.startNewActivity(mContext, OrgConversionRecordActivity.class);
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orgintegral_transaction);
        mOrgIntegralDiamondPresenter = new OrgIntegralDiamondPresenter();
        mOrgIntegralDiamondPresenter.attachView(this);
        initRefreshLayout(mBGDelegate);
        mListView = findViewById(R.id.listview);
        mFound = findViewById(R.id.found);
        mSimpleDraweeView = findViewById(R.id.icon);
        mName = findViewById(R.id.name);
        mName = findViewById(R.id.name);
        mModel = findViewById(R.id.comment_model_tv);
        mConvene = findViewById(R.id.drivdetails_convene_da);
        mBrandLogo = findViewById(R.id.drivdetails_convene_brandLogo);
        mLooseChange = findViewById(R.id.loosechange);

        mAdapter = new WrapAdapter<IntegralDiamondListBean.DataList>(mContext, R.layout.integraldiamon_item, datalist) {
            @Override
            public void convert(ViewHolder helper, final IntegralDiamondListBean.DataList item) {
                ImageView type = helper.getView(R.id.type);
                TextView number = helper.getView(R.id.number);
                TextView price = helper.getView(R.id.price);
                TextView platform = helper.getView(R.id.platform);
                TextView purchase = helper.getView(R.id.purchase);
                if (item.pointType == 3) {
                    type.setImageResource(R.drawable.icon_zhuanshi);
                    number.setText(item.points + "钻石");
                } else if (item.pointType == 2) {
                    type.setImageResource(R.drawable.icon_yinyuan);
                    number.setText(item.points + "银元");
                } else {
                    type.setImageResource(R.drawable.icon_jinbi2);
                    number.setText(item.points + "金币");
                }
                price.setText("￥" + item.price);
                platform.setText(item.nikename);
                purchase.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOrgIntegralDiamondPresenter.getGoodsDetail(IOUtils.getToken(mContext), item.id + "");
                    }
                });
            }
        };
        mListView.setAdapter(mAdapter);
        mFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrgDetailsBean != null) {
                    Map<String, Serializable> args = new HashMap<>();
                    args.put(Constants.IntentParams.ID, mOrgDetailsBean.data.currentPoints);
                    CommonUtils.startNewActivity(mContext, args, OrgPublishPurchaseActivity.class);
                }
            }
        });
        Account account = IOUtils.getAccount(mContext);
        mOrgIntegralDiamondPresenter.getOrgDetail(IOUtils.getToken(mContext), account.org.id + "", 2 + "");
        loadMore();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {
        CommonUtils.showToast(mContext, "系统异常");
    }

    @Override
    public void userDataSucceed(OrgDetailsBean integralUserinfoBean) {
        mOrgDetailsBean = integralUserinfoBean;
        mSimpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(integralUserinfoBean.data.avatar, 360, 360)));
        mName.setText(integralUserinfoBean.data.name);
        mLooseChange.setText(integralUserinfoBean.data.userMoney);
    }

    @Override
    public void DatalistSucceed(IntegralDiamondListBean integralGoodListBean) {
        stopRefreshAll();
        if (page == 1) {
            datalist.clear();
        }
        if (integralGoodListBean.data.list != null && integralGoodListBean.data.list.size() != 0) {
            datalist.addAll(integralGoodListBean.data.list);
        } else {
            isRefresh = false;
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void DataPurchaseSucceed(IntegralPurchaseBean baseBean) {
        PurchaseGoodDetailsPopupWindow mPurchaseGoodDetailsPopupWindow = new PurchaseGoodDetailsPopupWindow(mContext, baseBean.data, new PurchaseGoodDetailsPopupWindow.GoodOnClickListener() {
            @Override
            public void goodOnClickListener(IntegralPurchaseBean.Data details) {
                mOrgIntegralDiamondPresenter.getBuyGoods(IOUtils.getToken(mContext),details.id+"");
            }
        });
        mPurchaseGoodDetailsPopupWindow.show(this);
    }

    @Override
    public void DataOrdeSucceed(ResponseMessage<String> baseBean) {

    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public Context getContext() {
        return mContext;
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
            Account account = IOUtils.getAccount(mContext);
            mOrgIntegralDiamondPresenter.getOrgDetail(IOUtils.getToken(mContext), account.org.id + "", 2 + "");
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
        mOrgIntegralDiamondPresenter.getGoodList(IOUtils.getToken(mContext), selectCommodity + "", page);
    }


}
