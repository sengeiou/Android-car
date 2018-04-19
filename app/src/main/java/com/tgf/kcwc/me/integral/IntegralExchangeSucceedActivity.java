package com.tgf.kcwc.me.integral;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.coupon.VoucherMainActivity;
import com.tgf.kcwc.mvp.model.IntegralExchangeBean;
import com.tgf.kcwc.mvp.model.IntegralExchangeSucceedBean;
import com.tgf.kcwc.mvp.model.IntegralListBean;
import com.tgf.kcwc.mvp.model.IntegralUserinfoBean;
import com.tgf.kcwc.mvp.presenter.IntegralConversionSucceedPresenter;
import com.tgf.kcwc.mvp.presenter.IntegralPresenter;
import com.tgf.kcwc.mvp.view.IntegralConversionSucceedView;
import com.tgf.kcwc.mvp.view.IntegralView;
import com.tgf.kcwc.ticket.TicketActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/7/4.
 * 兑换成功界面
 */

public class IntegralExchangeSucceedActivity extends BaseActivity implements IntegralConversionSucceedView {

    IntegralConversionSucceedPresenter mIntegralConversionSucceedPresenter;
    protected SimpleDraweeView mSimpleDraweeView;//商品图片
    protected TextView mCommodityName;//商品名字
    protected TextView mCommodityDescribe;//商品描述
    protected TextView mCommodityNum;//商品库存

    protected LinearLayout mReceiveyardLayout;//兑换码布局
    protected TextView mReceiveyard;//兑换码
    protected TextView mReceiveAddress;//兑换地址

    protected LinearLayout mRecipientsLayout;//收件人全部布局
    protected LinearLayout mAddressLayout;//收件人布局
    protected LinearLayout mAddAddress;//新增加收件人
    protected TextView mRecipients;//收件人
    protected TextView mConsigneeAddress;//收件人地址
    protected ImageView mModification;//修改收件人

    protected TextView mMemBercenter;//会员中心
    protected TextView mContinueexChange;//继续兑换
    protected TextView mEmploy;//立即使用

    protected TextView mAccess;//领取方式

    protected LinearLayout mPickupMethod;//领取方式布局

    String ID;
    IntegralExchangeSucceedBean mIntegralExchangeSucceedBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_exchangesucceed);
        ID = getIntent().getStringExtra(Constants.IntentParams.ID);
        mIntegralConversionSucceedPresenter = new IntegralConversionSucceedPresenter();
        mIntegralConversionSucceedPresenter.attachView(this);
        mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.commodityicon);
        mCommodityName = (TextView) findViewById(R.id.commodityname);
        mCommodityDescribe = (TextView) findViewById(R.id.commoditydescribe);
        mCommodityNum = (TextView) findViewById(R.id.commoditynum);
        mReceiveyardLayout = (LinearLayout) findViewById(R.id.receiveyardlayout);
        mReceiveyard = (TextView) findViewById(R.id.receiveyard);
        mReceiveAddress = (TextView) findViewById(R.id.receiveaddress);
        mRecipientsLayout = (LinearLayout) findViewById(R.id.recipientslayout);
        mAddAddress = (LinearLayout) findViewById(R.id.add_address);
        mAddressLayout = (LinearLayout) findViewById(R.id.addresslayout);
        mRecipients = (TextView) findViewById(R.id.recipients);
        mConsigneeAddress = (TextView) findViewById(R.id.consigneeaddress);
        mModification = (ImageView) findViewById(R.id.modification);
        mMemBercenter = (TextView) findViewById(R.id.membercenter);
        mContinueexChange = (TextView) findViewById(R.id.continueexchange);
        mEmploy = (TextView) findViewById(R.id.employ);
        mAccess = (TextView) findViewById(R.id.access);
        mPickupMethod = (LinearLayout) findViewById(R.id.pickupmethod);

        mModification.setOnClickListener(this);
        mMemBercenter.setOnClickListener(this);
        mContinueexChange.setOnClickListener(this);
        mEmploy.setOnClickListener(this);
        mAddAddress.setOnClickListener(this);
        mIntegralConversionSucceedPresenter.getRecordDetail(ID);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.modification:
            case R.id.add_address:
                if (mIntegralExchangeSucceedBean != null) {
                    Map<String, Serializable> args = new HashMap<>();
                    args.put(Constants.IntentParams.ID, ID);
                    CommonUtils.startNewActivity(mContext, args, ModificationAddressActivity.class);
                }
                break;
            case R.id.membercenter:
                break;
            case R.id.continueexchange:
                finish();
                break;
            case R.id.employ:
                if (mIntegralExchangeSucceedBean != null) {
                    if (mIntegralExchangeSucceedBean.data.receiveType == 1) {
                        Map<String, Serializable> args = new HashMap<>();
                        args.put(Constants.IntentParams.INDEX, 3);
                        CommonUtils.startNewActivity(mContext, args, VoucherMainActivity.class);
                    } else if (mIntegralExchangeSucceedBean.data.receiveType == 2) {
                        Map<String, Serializable> args = new HashMap<>();
                        CommonUtils.startNewActivity(mContext, null, TicketActivity.class);

                    }
                }
                break;
        }
    }

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("兑换成功");
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
    public void DataBuyProductSucceed(IntegralExchangeSucceedBean baseBean) {
        mIntegralExchangeSucceedBean = baseBean;
        mSimpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(baseBean.data.cover, 360, 360)));//item图片
        mCommodityName.setText(baseBean.data.name);
        mCommodityNum.setText("数量：" + baseBean.data.num);
        int productType = baseBean.data.productType;
        if (productType == 1) {
            if (!TextUtil.isEmpty(baseBean.data.code)) {
                mPickupMethod.setVisibility(View.VISIBLE);
                mReceiveyardLayout.setVisibility(View.VISIBLE);
                mRecipientsLayout.setVisibility(View.GONE);
                mAccess.setText("凭领取码现场领取");
            } else {
                mPickupMethod.setVisibility(View.GONE);
            }
        } else if (productType == 2) {
            mPickupMethod.setVisibility(View.VISIBLE);
            mReceiveyardLayout.setVisibility(View.GONE);
            mRecipientsLayout.setVisibility(View.VISIBLE);
            mAccess.setText("看车玩车平台邮寄");
        } else {
            mPickupMethod.setVisibility(View.GONE);
        }
        mReceiveyard.setText(baseBean.data.code);
        mReceiveAddress.setText(baseBean.data.address);
        mRecipients.setText(baseBean.data.nickname + "(" + baseBean.data.tel + ")");
        mConsigneeAddress.setText(baseBean.data.address);
        int receiveType = baseBean.data.receiveType;
        if (receiveType == 1) {
            mContinueexChange.setVisibility(View.VISIBLE);
            mEmploy.setVisibility(View.VISIBLE);
        } else {
            mContinueexChange.setVisibility(View.VISIBLE);
            mEmploy.setVisibility(View.GONE);
        }
    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
