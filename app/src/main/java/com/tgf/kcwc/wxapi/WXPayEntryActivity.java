package com.tgf.kcwc.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.coupon.VoucherMainActivity;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.me.mybalance.WXPrePaidActivity;
import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.CouponPayModel;
import com.tgf.kcwc.mvp.model.OrderModel;
import com.tgf.kcwc.mvp.model.OrderPayParam;
import com.tgf.kcwc.mvp.model.PayParamModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TransactionModel;
import com.tgf.kcwc.mvp.presenter.IntegralOrderPaysPresenter;
import com.tgf.kcwc.mvp.presenter.OrderPayPresenter;
import com.tgf.kcwc.mvp.presenter.WxStatusPresenter;
import com.tgf.kcwc.mvp.view.IntegraOrderPaysView;
import com.tgf.kcwc.mvp.view.OrderPayView;
import com.tgf.kcwc.mvp.view.WxStatusView;
import com.tgf.kcwc.ticket.TicketActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.PayUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.List;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler, OrderPayView<OrderModel>, WxStatusView, IntegraOrderPaysView {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private final int KEY_PAYTYPE_COUPON = 2;
    //展会申请
    private final int KEY_PAYTYPE_APPLY = 3;
    //个人积分
    private final int KEY_PAYTYPE_INTEGRAL = 4;

    private static final int WX_PRE_PAID_TYPE = 5;

    private IWXAPI api;

    private TextView mBackBtn;

    private OrderPayPresenter mPresenter, mPrePaidPresenter;

    private TextView successHint;
    private LinearLayout mContentLayout;
    private LinearLayout mLl;
    private LinearLayout mPayFailureLayout;
    private String orderId = "";
    private TextView mCancelBtn, mContinueBtn;
    private String mToken;
    private int mPayType;
    private TextView textDesc;
    private TextView phone;
    private boolean isSuccess = false;
    private TextView titleText;
    private TextView failedPayDescTv;
    private TextView mSuccessGotoBtn;
    private WxStatusPresenter wxStatusPresenter;
    private IntegralOrderPaysPresenter mIntegralOrderPaysPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        mToken = IOUtils.getToken(mContext);
        KPlayCarApp app = ((KPlayCarApp) getApplication());
        api = app.getMsgApi();
        orderId = app.getOrderId();
        Log.e("TAG", "orderId: " + orderId);
        api.handleIntent(getIntent(), this);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        titleText = text;
        titleText.setText("订单支付");
    }

    @Override
    protected void setUpViews() {
        textDesc = (TextView) findViewById(R.id.paydesc_tv);
        phone = (TextView) findViewById(R.id.phone);
        successHint = (TextView) findViewById(R.id.success_hint);
        mSuccessGotoBtn = (TextView) findViewById(R.id.goTicketBtn);
        mSuccessGotoBtn.setOnClickListener(this);
        mPresenter = new OrderPayPresenter();
        mPresenter.attachView(this);
        mPrePaidPresenter = new OrderPayPresenter();
        mPrePaidPresenter.attachView(prePaidOrderView);
        wxStatusPresenter = new WxStatusPresenter();
        wxStatusPresenter.attachView(this);
        mIntegralOrderPaysPresenter = new IntegralOrderPaysPresenter();
        mIntegralOrderPaysPresenter.attachView(this);
        mContentLayout = (LinearLayout) findViewById(R.id.contentLayout);
        mPayFailureLayout = (LinearLayout) findViewById(R.id.payFailureLayout);
        mLl = (LinearLayout) findViewById(R.id.ll);
        failedPayDescTv = (TextView) findViewById(R.id.failContentTv);
        mCancelBtn = (TextView) findViewById(R.id.cancelPayBtn);
        mContinueBtn = (TextView) findViewById(R.id.continuePayBtn);
        mCancelBtn.setOnClickListener(this);
        mContinueBtn.setOnClickListener(this);
    }

    private OrderPayView<ResponseMessage<Object>> prePaidOrderView = new OrderPayView<ResponseMessage<Object>>() {
        @Override
        public void showData(ResponseMessage<Object> msg) {

            if (msg.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                CommonUtils.showToast(mContext, "充值成功");
            } else {
                CommonUtils.showToast(mContext, msg.statusMessage);
            }

            List<Activity> allPages = KPlayCarApp.getActivityStack();
            for (Activity a : allPages) {
                if (a instanceof WXPrePaidActivity) {
                    a.finish();
                }
            }
            finish();
        }

        @Override
        public void showPayCouponResult(CouponPayModel data) {

        }

        @Override
        public void setLoadingIndicator(boolean active) {

        }

        @Override
        public void showLoadingTasksError() {

        }

        @Override
        public Context getContext() {
            return mContext;
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Logger.d("onPayFinish, errCode = " + resp.errCode + ",orderId:" + orderId);

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                paySuccess(resp);
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                CommonUtils.showToast(mContext, "授权失败!");
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                CommonUtils.showToast(mContext, "用户已取消支付");
                finish();
                break;


        }

        //        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
        //            AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //            builder.setTitle(R.string.pay_result_tip);
        //            //builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
        //            builder.show();
        //        }

    }

    private void paySuccess(BaseResp resp) {
        if (resp instanceof PayResp) {
            PayResp payResp = (PayResp) resp;

            String extData = payResp.extData;
            if (TextUtil.isEmpty(extData)) {
                if (!TextUtils.isEmpty(orderId)) {
                    mPresenter.queryPayResult(orderId, mToken);
                }
                return;
            }

            mPayType = Integer.parseInt(payResp.extData);
            if (mPayType == KEY_PAYTYPE_COUPON) {
                textDesc.setText(Html.fromHtml("请进入<font color=\"#4e81ba\">“会员中心-代金券-我的”</font>查看购买到的代金券"));
                mPresenter.queryCouponPayResult(orderId, mToken);
                return;
            }
            //参展申请
            if (mPayType == KEY_PAYTYPE_APPLY) {
                textDesc.setText(Html.fromHtml("请进入<font color=\"#36a95c\">我的->车主自售->参展申请</font>”查看进度"));
                textDesc.setTextSize(15);
                wxStatusPresenter.getWxStatus(IOUtils.getToken(this), orderId);
                return;
            }
            //个人积分
            if (mPayType == KEY_PAYTYPE_INTEGRAL) {
                //textDesc.setText(Html.fromHtml("请进入<font color=\"#36a95c\">我的->个人积分</font>”查看进度"));
                textDesc.setText("");
                textDesc.setTextSize(15);
                mIntegralOrderPaysPresenter.getIsPayStatus(IOUtils.getToken(this), orderId);
                return;
            }

            if (WX_PRE_PAID_TYPE == mPayType) {

                OrderPayParam payParam = KPlayCarApp.getValue(Constants.IntentParams.PRE_PAID_DATA);
                if (payParam != null) {
                    mPrePaidPresenter.findPrePaidResultByOrderId(payParam.orderSN, IOUtils.getToken(mContext));
                }
            }
        }
    }


    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.goTicketBtn:
                finish();
                if (mPayType == KEY_PAYTYPE_COUPON) {
                    Intent toMycouponIntent = new Intent(mContext, VoucherMainActivity.class);
                    toMycouponIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    toMycouponIntent.putExtra(Constants.IntentParams.INDEX, 3);
                    startActivity(toMycouponIntent);
                } else if (mPayType == KEY_PAYTYPE_APPLY) {
                    //查看申请详情
//                    CommonUtils.startNewActivity(mContext,SaleMgrListActivity.class);
                    CommonUtils.startNewActivity(mContext, com.tgf.kcwc.me.sale.SaleMgrListActivity.class);
                } else if (mPayType == KEY_PAYTYPE_INTEGRAL) {
                    //查看积分
                    finish();
                } else {
                    CommonUtils.startNewActivity(mContext, TicketActivity.class);
                }
                break;
            case R.id.cancelPayBtn:
                finish();
                break;
            case R.id.continuePayBtn:
                if (mPayType == KEY_PAYTYPE_COUPON) {
                    orderCouponPay();
                } else if (mPayType == KEY_PAYTYPE_APPLY) {
                    orderApplyPay();
                } else if (mPayType == KEY_PAYTYPE_INTEGRAL) {
                    orderIntegralPay();
                } else {
                    //门票
                    orderPay();
                }
                break;
        }
        //finish();

    }

    /**
     * 参展申请费用重新支付
     */
    private void orderApplyPay() {
        PayParamModel.Data data = KPlayCarApp.getValue(Constants.KeyParams.WX_PAY_DATA);
        PayUtil.weixinPay(api, data.appId,
                data.partnerId, data.prepayId,
                data.wxPackage, data.nonceStr,
                data.timeStamp, data.paySign,
                KEY_PAYTYPE_APPLY + "");
    }

    /**
     * 积分重新支付
     */
    private void orderIntegralPay() {
        OrderPayParam mWXPayModel = KPlayCarApp.getValue(Constants.KeyParams.WX_PAY_DATA);
        PayUtil.weixinPay(api, Constants.WEIXIN_APP_ID, mWXPayModel.partnerId, mWXPayModel.prepayId,
                Constants.PACKAGE_VALUE, mWXPayModel.nonceStr, mWXPayModel.timeStamp,
                mWXPayModel.paySign, KEY_PAYTYPE_INTEGRAL + "");
    }

    private void orderCouponPay() {
        TransactionModel transactionModel = KPlayCarApp.getValue(Constants.KeyParams.WX_PAY_DATA);
        PayUtil.weixinPay(api, transactionModel.transactionParam.appId,
                transactionModel.transactionParam.partnerId, transactionModel.transactionParam.prepayId,
                transactionModel.transactionParam.wxPackage, transactionModel.transactionParam.nonceStr,
                transactionModel.transactionParam.timeStamp, transactionModel.transactionParam.paySign,
                KEY_PAYTYPE_COUPON + "");
    }

    private void orderPay() {
        OrderPayParam mWXPayModel = KPlayCarApp.getValue(Constants.KeyParams.WX_PAY_DATA);
        PayUtil.weixinPay(api, Constants.WEIXIN_APP_ID, mWXPayModel.partnerId, mWXPayModel.prepayId,
                Constants.PACKAGE_VALUE, mWXPayModel.nonceStr, mWXPayModel.timeStamp,
                mWXPayModel.paySign);
    }

    @Override
    public void showData(OrderModel model) {

        if (model != null) {
            if (Constants.PayStatus.SUCCESS == model.details.status) {
                mContentLayout.setVisibility(View.VISIBLE);
                isSuccess = true;
                mPayFailureLayout.setVisibility(View.GONE);
            } else {
                isSuccess = false;
                mContentLayout.setVisibility(View.GONE);
                mPayFailureLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void showPayCouponResult(CouponPayModel couponPayModel) {
        if (couponPayModel != null) {
            Logger.d("PayStatus " + couponPayModel.isPay);
            if (Constants.PayStatus.SUCCESS == couponPayModel.isPay) {
                titleText.setText("支付成功");
                mSuccessGotoBtn.setText("查看代金券");
                mContentLayout.setVisibility(View.VISIBLE);
                mPayFailureLayout.setVisibility(View.GONE);
            } else {
                titleText.setText("支付失败");
                failedPayDescTv.setText("您可“继续支付”，完成代金券购买；\n也可“取消订单“，放弃购买。");
                mCancelBtn.setText("放弃订单");
                mCancelBtn.setBackgroundResource(R.drawable.button_bg_17);
                mContinueBtn.setBackgroundResource(R.drawable.button_bg_2);
                mContinueBtn.setTextColor(mRes.getColor(R.color.white));
                mContentLayout.setVisibility(View.GONE);
                mPayFailureLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        dismissLoadingDialog();
        CommonUtils.showToast(mContext, R.string.pay_failure);
        finish();

    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        wxStatusPresenter.detachView();
    }

    @Override
    public void showIntegralOrderDetail(BaseArryBean model) {
        if (model != null) {
            if (0 == model.code) {
                titleText.setText("支付成功");
                mSuccessGotoBtn.setText("确定");
                mContentLayout.setVisibility(View.VISIBLE);
                mPayFailureLayout.setVisibility(View.GONE);
            } else {
                titleText.setText("支付失败");
                failedPayDescTv.setText("您可“继续支付”，完成购买；\n也可“取消订单“，放弃购买。");
                mCancelBtn.setText("放弃订单");
                mCancelBtn.setBackgroundResource(R.drawable.button_bg_17);
                mContinueBtn.setBackgroundResource(R.drawable.button_bg_2);
                mContinueBtn.setTextColor(mRes.getColor(R.color.white));
                mContentLayout.setVisibility(View.GONE);
                mPayFailureLayout.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void wxStatusSuccess(String model) {
        if (Integer.parseInt(model) == Constants.NetworkStatusCode.SUCCESS) {
            mLl.setVisibility(View.VISIBLE);
            phone.setVisibility(View.VISIBLE);
            titleText.setText("二手车主参展申请");
            successHint.setText("参展申请提交成功，请耐心等待审核！\n我们将在3个工作日内反馈审核结果");
            mSuccessGotoBtn.setText("查看申请详情");
            mContentLayout.setVisibility(View.VISIBLE);
            mPayFailureLayout.setVisibility(View.GONE);
        } else {
            titleText.setText("支付失败");
            failedPayDescTv.setText("您可“继续支付”，完成参展申请购买；\n也可“取消订单“，放弃购买。");
            mCancelBtn.setText("取消订单");
            mContentLayout.setVisibility(View.GONE);
            mPayFailureLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void wxStatusFail(String model) {
        Log.e("TAG", "wxStatusFail: ");
        titleText.setText("支付失败");
        failedPayDescTv.setText("您可“继续支付”，完成参展申请购买；\n也可“取消订单“，放弃购买。");
        mCancelBtn.setText("取消订单");
        mContentLayout.setVisibility(View.GONE);
        mPayFailureLayout.setVisibility(View.VISIBLE);
    }
}