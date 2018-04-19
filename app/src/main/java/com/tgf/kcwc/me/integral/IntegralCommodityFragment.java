package com.tgf.kcwc.me.integral;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.IntegralConversionGoodDetailsBean;
import com.tgf.kcwc.mvp.model.IntegralExchangeBean;
import com.tgf.kcwc.mvp.model.IntegralGoodListBean;
import com.tgf.kcwc.mvp.model.IntegralUserinfoBean;
import com.tgf.kcwc.mvp.presenter.IntegralConversionPresenter;
import com.tgf.kcwc.mvp.view.IntegralConversionView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.GoodDetailsPopupWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/10/18.
 * 商品兑换
 */

public class IntegralCommodityFragment extends BaseFragment implements IntegralConversionView {

    private IntegralConversionPresenter mIntegralConversionPresenter;
    private GridView mGridView;
    private WrapAdapter<IntegralGoodListBean.DataList> mAdapter;
    private List<IntegralGoodListBean.DataList> datalist = new ArrayList<>();
    private SimpleDraweeView mSimpleDraweeView;

    private TextView mGold; //金币

    private IntegralUserinfoBean integralUserinfoBean;
    int page = 1;
    boolean isRefresh = true;
    private GoodDetailsPopupWindow goodDetailsPopupWindow;

    private TextView mGrade; //等级
    private TextView mName; //名字
    private ImageView mModel; //模特
    private ImageView mConvene; //达人
    private SimpleDraweeView mBrandLogo; //logo

    public TextView mAllText; //全部
    public TextView mAllBelow;

    public TextView mCommodityText; //商品
    public TextView mCommodityBelow;

    public TextView mTicketsText; //门票
    public TextView mTicketsBelow;

    public TextView mVoucherText; //代金券
    public TextView mVouchertextBelow;

    List<TextView> textlist = new ArrayList<>();
    List<TextView> Belowlist = new ArrayList<>();

    IntegralConversionActivity mIntegralConversionActivity;
    public int selectCommodity = 0;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.integralcommodity_fragment;
    }

    @Override
    protected void initView() {
        mIntegralConversionActivity = (IntegralConversionActivity) getActivity();
        textlist.clear();
        Belowlist.clear();
        mIntegralConversionPresenter = new IntegralConversionPresenter();
        mIntegralConversionPresenter.attachView(this);
        initRefreshLayout(mBGDelegate);
        mGridView = findView(R.id.gridview);

        mSimpleDraweeView = findView(R.id.icon);
        mGold = findView(R.id.gold);
        mGrade = findView(R.id.grade);
        mName = findView(R.id.name);
        mModel = findView(R.id.comment_model_tv);
        mConvene = findView(R.id.drivdetails_convene_da);
        mBrandLogo = findView(R.id.drivdetails_convene_brandLogo);

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

        mAdapter = new WrapAdapter<IntegralGoodListBean.DataList>(mContext, R.layout.integralconversion_item, datalist) {
            @Override
            public void convert(ViewHolder helper, final IntegralGoodListBean.DataList item) {
                SimpleDraweeView mIcon = helper.getView(R.id.icon);
                TextView name = helper.getView(R.id.name);
                TextView integral = helper.getView(R.id.integral);
                TextView conversion = helper.getView(R.id.conversion);
                TextView unit = helper.getView(R.id.unit);
                TextView bazaar = helper.getView(R.id.bazaar);
                name.setText(item.name);
                integral.setText(item.needPoints + "");
                mIcon.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 270, 203)));
                bazaar.setText("市场价：￥" + item.marketValue);
                if (item.exchangeType == 1) {
                    unit.setText("金币");
                } else {
                    unit.setText("银元");
                }

                conversion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIntegralConversionPresenter.getGooddetails(IOUtils.getToken(mContext), item.id + "");
                    }
                });
            }
        };
        mGridView.setAdapter(mAdapter);

        mIntegralConversionPresenter.getUserinfo(IOUtils.getToken(mContext), mIntegralConversionActivity.skiptype + "");
        loadMore();
    }

    @Override
    public void userDataSucceed(IntegralUserinfoBean integralUserinfoBean) {
        this.integralUserinfoBean = integralUserinfoBean;
        mSimpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(integralUserinfoBean.data.avatar, 360, 360)));
        if (mIntegralConversionActivity.skiptype == 1) {
            mGrade.setText("LV" + integralUserinfoBean.data.level);
        } else {
            mGrade.setText(integralUserinfoBean.data.businessLevel + "");
        }

        mName.setText(integralUserinfoBean.data.nickname);
        mGold.setText("金币:" + integralUserinfoBean.data.currentPoints + "\t银元:" + integralUserinfoBean.data.currentBusinessPoints);

        //模特
        if (integralUserinfoBean.data.isModel == 1) {
            mModel.setVisibility(View.VISIBLE);
        } else {
            mModel.setVisibility(View.GONE);
        }

        //达人
        if (integralUserinfoBean.data.isDoyen == 1) {
            mConvene.setVisibility(View.VISIBLE);
        } else {
            mConvene.setVisibility(View.GONE);
        }

        //车主

        if (integralUserinfoBean.data.isMaster == 1) {
            mBrandLogo.setVisibility(View.VISIBLE);
            mBrandLogo.setImageURI(Uri.parse(URLUtil.builderImgUrl(integralUserinfoBean.data.logo, 144, 144)));
        } else {
            mBrandLogo.setVisibility(View.GONE);
        }
    }

    @Override
    public void DatalistSucceed(IntegralGoodListBean integralGoodListBean) {
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
    public void DataDetailsSucceed(final IntegralConversionGoodDetailsBean data) {
        goodDetailsPopupWindow = new GoodDetailsPopupWindow(getActivity(), data, integralUserinfoBean.data.currentPoints, integralUserinfoBean.data.currentBusinessPoints, new GoodDetailsPopupWindow.GoodOnClickListener() {
            @Override
            public void goodOnClickListener(IntegralConversionGoodDetailsBean.Details details, int number) {
                mIntegralConversionPresenter.getBuyProduct(builderParams1(details, number));
            }
        });
        goodDetailsPopupWindow.show(getActivity());
    }

    private Map<String, String> builderParams1(IntegralConversionGoodDetailsBean.Details details, int number) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", IOUtils.getToken(mContext));
        params.put("nickname", details.uername);
        params.put("tel", details.tel);
        params.put("product_id", details.id + "");
        params.put("address", details.address);
        params.put("num", number + "");
        return params;
    }

    @Override
    public void DataBuyProductSucceed(IntegralExchangeBean baseBean) {
        if (goodDetailsPopupWindow != null) {
            goodDetailsPopupWindow.dismiss();
        }
        mIntegralConversionPresenter.getUserinfo(IOUtils.getToken(mContext), mIntegralConversionActivity.skiptype + "");
        Map<String, Serializable> args = new HashMap<>();
        args.put(Constants.IntentParams.ID, baseBean.data.id);
        CommonUtils.startNewActivity(mContext, args, IntegralExchangeSucceedActivity.class);

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
            mIntegralConversionPresenter.getUserinfo(IOUtils.getToken(mContext), mIntegralConversionActivity.skiptype + "");
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
        mIntegralConversionPresenter.getGoodList(IOUtils.getToken(mContext), selectCommodity + "", page);
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
                selectCommodity = 2;
                loadMore();
                break;
            case R.id.vouchertext:
                selectText(3);
                page = 1;
                isRefresh = true;
                selectCommodity = 1;
                loadMore();
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
}
