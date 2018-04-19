package com.tgf.kcwc.coupon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.text.SpannableString;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.CouponInfo;
import com.tgf.kcwc.mvp.model.BasePageModel;
import com.tgf.kcwc.mvp.model.CouponDetailModel;
import com.tgf.kcwc.mvp.model.MyCouponModel;
import com.tgf.kcwc.mvp.model.TransactionModel;
import com.tgf.kcwc.mvp.presenter.CouponDetailPresenter;
import com.tgf.kcwc.mvp.presenter.CouponPresenter;
import com.tgf.kcwc.mvp.view.CouponDetailView;
import com.tgf.kcwc.mvp.view.CouponView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.PayUtil;
import com.tgf.kcwc.util.RxBus;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Auther: Scott
 * Date: 2017/1/17 0017
 * E-mail:hekescott@qq.com
 */

public class CouponOrderActivity extends BaseActivity implements CouponView<TransactionModel>, CouponDetailView {

    private TextView couponOrderNumTv;

    private int mCouponId = -1;

    private IWXAPI mIWXApi;
    private CouponPresenter mPresenter;

    private CouponInfo mCouponInfo;
    private SimpleDraweeView couponCoverIV;
    private TextView couponTitleTv;
    private RelativeLayout buyCouponRl;
    private TextView couponNowpirceTV;
    private TextView couponOldPriceTv;
    private TextView couponLimitTv;
    private TextView couponDealersTv;
    private TextView paypriceTv;
    private Intent fromIntent;
    private CouponDetailPresenter couponDetailPresenter;
    private KPlayCarApp kPlayCarApp;
    private TextView textTitle;
    private TextView submitTv;
    private String distributeId = "";
    private final String TAG = "CouponOrderActivity";
    private int getlimit;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        textTitle = text;

        if (mCouponInfo != null) {
            initViewData();
        } else {
            mCouponId = fromIntent.getIntExtra(Constants.IntentParams.ID, -1);
            getlimit = fromIntent.getIntExtra(Constants.IntentParams.COUNT, -1);
            if (mCouponId != -1) {
                couponDetailPresenter = new CouponDetailPresenter();
                couponDetailPresenter.attachView(this);
                couponDetailPresenter.getCouponDetail(mCouponId, null, null, IOUtils.getToken(mContext));
            }
        }
        distributeId = fromIntent.getStringExtra(Constants.IntentParams.ID2);
//        textTitle = text;
    }

    @Override
    protected void setUpViews() {
        kPlayCarApp = (KPlayCarApp) getApplication();
        fromIntent = getIntent();
        mCouponInfo = (CouponInfo) fromIntent.getSerializableExtra(Constants.IntentParams.DATA);
        findViewById(R.id.couponorder_reduce).setOnClickListener(this);
        findViewById(R.id.couponorder_add).setOnClickListener(this);
        findViewById(R.id.couponorder_submit).setOnClickListener(this);
        submitTv = (TextView) findViewById(R.id.couponorder_submit);
        couponOrderNumTv = (TextView) findViewById(R.id.couponoreder_number);
        couponCoverIV = (SimpleDraweeView) findViewById(R.id.couponbuy_cover);
        couponTitleTv = (TextView) findViewById(R.id.couponbuy_title);
        buyCouponRl = (RelativeLayout) findViewById(R.id.buyCouponRl);
        couponNowpirceTV = (TextView) findViewById(R.id.couponbuy_nowprice);
        couponOldPriceTv = (TextView) findViewById(R.id.couponbuy_oldprice);
        couponLimitTv = (TextView) findViewById(R.id.couponbuy_limit);
        couponDealersTv = (TextView) findViewById(R.id.buycoupon_dealers);
        paypriceTv = (TextView) findViewById(R.id.buycoupon_payprice);


        mPresenter = new CouponPresenter();
        mPresenter.attachView(this);

    }

    private void initViewData() {
        String coverURL = URLUtil.builderImgUrl(mCouponInfo.cover, 270, 203);
        mCouponId = Integer.parseInt(mCouponInfo.id);
        couponCoverIV.setImageURI(Uri.parse(coverURL));
        couponTitleTv.setText(mCouponInfo.title);
        couponDealersTv.setText(mCouponInfo.dealerName);

        SpannableString oldPrice = SpannableUtil.getDelLineString("￥" + mCouponInfo.denomination);
        couponOldPriceTv.setText(oldPrice);


        if (mCouponInfo.price != 0) {
            textTitle.setText("代金券购买");
            paypriceTv.setText("￥" + mCouponInfo.price);
            couponNowpirceTV.setText("￥" + mCouponInfo.price);
            couponLimitTv.setText("数量  限购 " + mCouponInfo.limit + "张");
        } else {
            //隐藏应付栏
            buyCouponRl.setVisibility(View.GONE);
            submitTv.setText("免费领取");
            textTitle.setText("代金券领取");
            paypriceTv.setText("免费");
            couponNowpirceTV.setText("免费");
            couponLimitTv.setText("数量  限领 " + mCouponInfo.limit + "张");
        }

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIWXApi = ((KPlayCarApp) getApplication()).getMsgApi();
        setContentView(R.layout.activity_couponorder);
        RxBus.$().register(TAG).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                Integer type = (Integer) o;
                if (type==RESULT_OK) {
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.couponorder_reduce:
                int reduce = Integer.parseInt(couponOrderNumTv.getText() + "") - 1;

                if (reduce <= 0) {
                    reduce++;
                    CommonUtils.showToast(getContext(), "不能再减少咯");
                    return;
                }
                if (mCouponInfo.price != 0) {
                    paypriceTv.setText("￥" + mCouponInfo.price * reduce);
                }
                couponOrderNumTv.setText(String.valueOf(reduce));

                break;
            case R.id.couponorder_add:
                int add = Integer.parseInt(couponOrderNumTv.getText() + "") + 1;
                if (add > mCouponInfo.limit) {
                    add--;
                    CommonUtils.showToast(getContext(), "已达到限制");
                    return;
                }
                if (mCouponInfo.price != 0) {
                    paypriceTv.setText("￥" + mCouponInfo.price * add);
                }
                couponOrderNumTv.setText(String.valueOf(add));
                break;
            case R.id.couponorder_submit:
                String num = couponOrderNumTv.getText().toString();
                mPresenter.getCouponPayData("" + mCouponId, distributeId, "APP", num,
                        IOUtils.getToken(mContext));
                break;
            default:
                break;
        }
    }

    @Override
    public void showDatas(TransactionModel transactionModel) {

        int needPay = transactionModel.needPay;
        kPlayCarApp.setOrderId(transactionModel.orderId + "");
        if (Constants.PayStatus.NEED_PAY == needPay) {
            PayUtil.weixinPay(mIWXApi, transactionModel.transactionParam.appId,
                    transactionModel.transactionParam.partnerId,
                    transactionModel.transactionParam.prepayId,
                    transactionModel.transactionParam.wxPackage,
                    transactionModel.transactionParam.nonceStr,
                    transactionModel.transactionParam.timeStamp,
                    transactionModel.transactionParam.paySign, 2 + "");
            KPlayCarApp.putValue(Constants.KeyParams.WX_PAY_DATA, transactionModel);
        } else if (Constants.PayStatus.UN_NEED == needPay) {
            CommonUtils.startNewActivity(mContext, CouponGetSuccesAcitivity.class);
        }

    }

    @Override
    public void shwoFailed(String statusMessage) {
        CommonUtils.showToast(this, statusMessage);
    }

    @Override
    public void showMyCouponList(ArrayList<MyCouponModel.MyCouponOrder> list) {

    }

    @Override
    public void shoMyCouponCount(BasePageModel.Pagination pagination) {

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        if (couponDetailPresenter != null) {
            couponDetailPresenter.detachView();
        }
        RxBus.$().unregister(TAG);
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void showHead(CouponDetailModel couponDetailModel) {
        mCouponInfo = new CouponInfo();
        mCouponInfo.title = couponDetailModel.title;
        mCouponInfo.id = couponDetailModel.id;
        if(getlimit ==-1){
            mCouponInfo.limit = couponDetailModel.getLimit;
        }else {
            mCouponInfo.limit = getlimit;
        }
        mCouponInfo.price = couponDetailModel.price;
        mCouponInfo.denomination = couponDetailModel.denomination;
        mCouponInfo.cover = couponDetailModel.cover;
        mCouponInfo.dealerName = couponDetailModel.dealers.get(0).name;
        initViewData();
    }

    @Override
    public void showRule(List<CouponDetailModel.Rrule> rules) {

    }

    @Override
    public void showDealers(List<CouponDetailModel.Dealers> dealrs) {

    }

    @Override
    public void showDesc(String detail) {

    }
}
