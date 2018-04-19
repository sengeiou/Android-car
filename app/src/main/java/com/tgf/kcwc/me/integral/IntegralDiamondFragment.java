package com.tgf.kcwc.me.integral;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.IntegralDiamondListBean;
import com.tgf.kcwc.mvp.model.IntegralPurchaseBean;
import com.tgf.kcwc.mvp.model.IntegralUserinfoBean;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.presenter.IntegralDiamondPresenter;
import com.tgf.kcwc.mvp.view.IntegralDiamondView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.PurchaseGoodDetailsPopupWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/10/18.
 * 积分交易
 */

public class IntegralDiamondFragment extends BaseFragment implements IntegralDiamondView {

    IntegralConversionActivity mIntegralConversionActivity;
    IntegralDiamondPresenter mIntegralDiamondPresenter;

    private ListView mListView;
    private WrapAdapter<IntegralDiamondListBean.DataList> mAdapter;
    private List<IntegralDiamondListBean.DataList> datalist = new ArrayList<>();
    int page = 1;
    boolean isRefresh = true;
    public int selectCommodity = 0;
    private IntegralUserinfoBean integralUserinfoBean;

    private SimpleDraweeView mSimpleDraweeView;//头像
    private TextView mName; //名字
    private ImageView mModel; //模特
    private ImageView mConvene; //达人
    private SimpleDraweeView mBrandLogo; //logo
    public TextView mLooseChange;//零钱

    public TextView mAllText; //全部
    public TextView mAllBelow;

    public TextView mCommodityText; //商品
    public TextView mCommodityBelow;

    public TextView mTicketsText; //门票
    public TextView mTicketsBelow;

    public TextView mVoucherText; //代金券
    public TextView mVouchertextBelow;

    public TextView mFound; //发布交易信息


    List<TextView> textlist = new ArrayList<>();
    List<TextView> Belowlist = new ArrayList<>();

    @Override
    protected void updateData() {
        mIntegralDiamondPresenter.getUserinfo(IOUtils.getToken(mContext), mIntegralConversionActivity.skiptype + "");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.integraldiamond_fragment;
    }

    @Override
    protected void initView() {
        textlist.clear();
        Belowlist.clear();
        mIntegralConversionActivity = (IntegralConversionActivity) getActivity();
        mIntegralDiamondPresenter = new IntegralDiamondPresenter();
        mIntegralDiamondPresenter.attachView(this);
        initRefreshLayout(mBGDelegate);
        mListView = findView(R.id.listview);
        mSimpleDraweeView = findView(R.id.icon);
        mName = findView(R.id.name);
        mModel = findView(R.id.comment_model_tv);
        mConvene = findView(R.id.drivdetails_convene_da);
        mBrandLogo = findView(R.id.drivdetails_convene_brandLogo);
        mLooseChange = findView(R.id.loosechange);
        mFound = findView(R.id.found);

        mAllText = findView(R.id.alltext);
        mAllBelow = findView(R.id.allbelow);
        textlist.add(mAllText);
        Belowlist.add(mAllBelow);
        mCommodityText = findView(R.id.commoditytext);
        mCommodityBelow = findView(R.id.commoditybelow);
        textlist.add(mCommodityText);
        Belowlist.add(mCommodityBelow);
        mTicketsText = findView(R.id.ticketstext);
        mTicketsBelow = findView(R.id.ticketsbelow);
        textlist.add(mTicketsText);
        Belowlist.add(mTicketsBelow);
        mVoucherText = findView(R.id.vouchertext);
        mVouchertextBelow = findView(R.id.vouchertextbelow);
        textlist.add(mVoucherText);
        Belowlist.add(mVouchertextBelow);

        mAllText.setOnClickListener(this);
        mCommodityText.setOnClickListener(this);
        mTicketsText.setOnClickListener(this);
        mVoucherText.setOnClickListener(this);
        mFound.setOnClickListener(this);


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
                        mIntegralDiamondPresenter.getGoodsDetail(IOUtils.getToken(mContext), item.id + "");
                    }
                });
            }
        };
        mListView.setAdapter(mAdapter);

        // mIntegralDiamondPresenter.getUserinfo(IOUtils.getToken(mContext), mIntegralConversionActivity.skiptype + "");
        loadMore();
    }

    @Override
    public void userDataSucceed(IntegralUserinfoBean integralUserinfoBean) {
        this.integralUserinfoBean = integralUserinfoBean;
        mSimpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(integralUserinfoBean.data.avatar, 360, 360)));
        mName.setText(integralUserinfoBean.data.nickname);
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
                mIntegralDiamondPresenter.getBuyGoods(IOUtils.getToken(mContext), details.id + "");
            }
        });
        mPurchaseGoodDetailsPopupWindow.show(getActivity());
    }

    @Override
    public void DataOrdeSucceed(ResponseMessage<String> baseBean) {
        Map<String, Serializable> args = new HashMap<String, Serializable>();
        args.put(Constants.IntentParams.ID, baseBean.data);
        CommonUtils.startNewActivity(mContext, args, IntegralOrderListActivity.class);
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

    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.alltext:
                selectText(0);
                page = 1;
                isRefresh = true;
                selectCommodity = 0;
                loadMore();
                break;
            case R.id.commoditytext:
                selectText(1);
                page = 1;
                isRefresh = true;
                selectCommodity = 3;
                loadMore();
                break;
            case R.id.ticketstext:
                selectText(2);
                page = 1;
                isRefresh = true;
                selectCommodity = 1;
                loadMore();
                break;
            case R.id.vouchertext:
                selectText(3);
                page = 1;
                isRefresh = true;
                selectCommodity = 2;
                loadMore();
                break;
            case R.id.found:
                Map<String, Serializable> args = new HashMap<>();
                args.put(Constants.IntentParams.ID, integralUserinfoBean.data.currentPoints);
                args.put(Constants.IntentParams.ID2, integralUserinfoBean.data.currentBusinessPoints);
                CommonUtils.startNewActivity(mContext, args, PublishPurchaseActivity.class);
                break;
        }
    }

    public void selectText(int num) {
        for (int i = 0; i < textlist.size(); i++) {
            if (i == num) {
                textlist.get(i).setTextColor(getResources().getColor(R.color.tab_text_s_color));
                Belowlist.get(i).setVisibility(View.VISIBLE);
            } else {
                textlist.get(i).setTextColor(getResources().getColor(R.color.text_color15));
                Belowlist.get(i).setVisibility(View.INVISIBLE);
            }
        }
    }

    public void loadMore() {
        mIntegralDiamondPresenter.getGoodList(IOUtils.getToken(mContext), selectCommodity + "", page);
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
            //mIntegralDiamondPresenter.getUserinfo(IOUtils.getToken(mContext), mIntegralConversionActivity.skiptype + "");
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
}
