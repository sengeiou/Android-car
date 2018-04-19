package com.tgf.kcwc.see.sale.release;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.me.integral.IntegralPayStatusActivity;
import com.tgf.kcwc.mvp.model.OrderPayModel;
import com.tgf.kcwc.mvp.model.OrderPayParam;
import com.tgf.kcwc.mvp.model.PayParamModel;
import com.tgf.kcwc.mvp.presenter.AlipayPresenter;
import com.tgf.kcwc.mvp.presenter.OrderPaysPresenter;
import com.tgf.kcwc.mvp.presenter.PayParamPresenter;
import com.tgf.kcwc.mvp.view.AlipayView;
import com.tgf.kcwc.mvp.view.OrderPaysView;
import com.tgf.kcwc.mvp.view.PayParamView;
import com.tgf.kcwc.ticket.OrderListActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.PayUtil;
import com.tgf.kcwc.util.ThreadPoolProxy;
import com.tgf.kcwc.view.FunctionView;

import java.util.Map;

/**
 * @anthor noti
 * @time 2017/9/13
 * @describle 订单支付
 */
public class OrderPayActivity extends BaseActivity implements OrderPaysView, PayParamView, AlipayView {
    //订单号，订单名，展位，时间段，服务费，押金，总价
    TextView orderNum, orderName, exhibitionPos, interval, serviceCharge, deposit, orderTotalPrice;
    //微信
    RelativeLayout wechatRl;
    ImageView wechatSelect;
    //支付宝
    RelativeLayout alipayRl;
    ImageView alipaySelect;
    //看车玩车-零钱
    RelativeLayout changeMoneyRl;
    TextView changeMoneyTv;
    ImageView changeMoneySelect;
    //订单编号
    private String orderSn;
    //支付方式
    private String payType;
    //支付宝参数
    private String callType;
    //微信参数
//    JSAPI/NATIVE/APP
    private String tradeType = "APP";
    private String openId;
    private PayParamModel.Data mWXPayMOdel;
    private KPlayCarApp mApp;
    private IWXAPI mWXAPI;
    //订单id
    private int orderId;
    //展会id
    private int eventId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);
    }

    @Override
    protected void setUpViews() {
        mApp = (KPlayCarApp) getApplication();
        mWXAPI = mApp.getMsgApi();
        orderSn = getIntent().getStringExtra(Constants.IntentParams.ID);

        orderNum = (TextView) findViewById(R.id.order_num);
        orderName = (TextView) findViewById(R.id.order_name);
        exhibitionPos = (TextView) findViewById(R.id.exhibition_pos);
        interval = (TextView) findViewById(R.id.interval);
        serviceCharge = (TextView) findViewById(R.id.service_charge);
        deposit = (TextView) findViewById(R.id.deposit);
        orderTotalPrice = (TextView) findViewById(R.id.order_total_price);

        wechatRl = (RelativeLayout) findViewById(R.id.wechat_rl);
        wechatSelect = (ImageView) findViewById(R.id.wechat_select);

        alipayRl = (RelativeLayout) findViewById(R.id.alipay_rl);
        alipaySelect = (ImageView) findViewById(R.id.alipay_select);

        changeMoneyRl = (RelativeLayout) findViewById(R.id.change_money_rl);
        changeMoneyTv = (TextView) findViewById(R.id.change_money_tv);
        changeMoneySelect = (ImageView) findViewById(R.id.change_money_select);

        findViewById(R.id.sure_pay).setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View view) {
                // TODO: 2017/9/13 确认 跳转等待审核
                PayParamPresenter payParamPresenter = new PayParamPresenter();
                payParamPresenter.attachView(OrderPayActivity.this);
                if (payType.equals("wechat")) {
                    payParamPresenter.postPayParam(IOUtils.getToken(OrderPayActivity.this), orderId, "APP");
                } else if (payType.equals("alipay")) {
                    AlipayPresenter alipayPresenter = new AlipayPresenter();
                    alipayPresenter.attachView(OrderPayActivity.this);
                    alipayPresenter.alipay(IOUtils.getToken(getContext()), orderId+"");
                } else {

                }
            }
        });
        wechatRl.setOnClickListener(this);
        alipayRl.setOnClickListener(this);
        changeMoneyRl.setOnClickListener(this);
        //默认选中微信支付
        payType = "wechat";
        wechatSelect.setVisibility(View.VISIBLE);
        OrderPaysPresenter orderPaysPresenter = new OrderPaysPresenter();
        orderPaysPresenter.attachView(this);
        orderPaysPresenter.getOrderDetail(IOUtils.getToken(this), orderSn);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("订单支付");
//        function.setImageResource(R.drawable.cover_default);
//        function.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            protected void onSingleClick(View view) {
//                // TODO: 2017/9/13 参展申请须知
////                CommonUtils.startNewActivity(OrderPayActivity.this, ApplyHintActivity.class);
//                Intent intent = new Intent(getContext(), ApplyHintActivity.class);
////                intent.putExtra(Constants.IntentParams.ID, eventId);
//                intent.putExtra(Constants.IntentParams.INDEX, 2);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            //微信
            case R.id.wechat_rl:
                payType = "wechat";
                wechatSelect.setVisibility(View.VISIBLE);
                alipaySelect.setVisibility(View.GONE);
                changeMoneySelect.setVisibility(View.GONE);
                break;
            //支付宝
            case R.id.alipay_rl:
//                callType =
//                CommonUtils.showToast(this,"暂不支持");
                payType = "alipay";
                wechatSelect.setVisibility(View.GONE);
                alipaySelect.setVisibility(View.VISIBLE);
                changeMoneySelect.setVisibility(View.GONE);
                break;
            //看车玩车-零钱
            case R.id.change_money_rl:
                CommonUtils.showToast(this, "暂不支持");
//                payType = "burse";
//                wechatSelect.setVisibility(View.GONE);
//                alipaySelect.setVisibility(View.GONE);
//                changeMoneySelect.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void showOrderDetail(OrderPayModel model) {
        if (model != null) {
            eventId = model.slaList.eventId;
            orderId = model.maList.id;
            mApp.setOrderId(orderId+"");
            orderNum.setText(model.maList.orderSn);
            orderName.setText(model.maList.title);
            //展位
            exhibitionPos.setText(model.slaList.parkName);
            //时间段
            interval.setText(model.slaList.addtime);
            serviceCharge.setText("￥" + model.slaList.serviceCharge + "元");
            deposit.setText("￥" + model.slaList.deposit + "元");
            orderTotalPrice.setText("￥" + model.maList.total);
        }
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
        return this;
    }

    @Override
    public void payParamSuccess(PayParamModel.Data data) {
        mWXPayMOdel = data;
        KPlayCarApp.putValue(Constants.KeyParams.WX_PAY_DATA, mWXPayMOdel);
        Logger.d(data + "");
        execWXPay();
//        finish();
    }

    @Override
    public void payParamFail(PayParamModel model) {
        CommonUtils.showToast(this, model.statusMessage);
    }

    /**
     * 微信支付
     */
    private void execWXPay() {
        if (mWXPayMOdel != null) {
            PayUtil.weixinPay(mWXAPI, Constants.WEIXIN_APP_ID, mWXPayMOdel.partnerId,
                    mWXPayMOdel.prepayId, Constants.PACKAGE_VALUE, mWXPayMOdel.nonceStr,
                    mWXPayMOdel.timeStamp, mWXPayMOdel.paySign, "3");
        } else {
            CommonUtils.showToast(mContext, "订单异常，请稍候重试！");
        }
    }

    /**
     * 支付宝支付
     */
    private void execAlipay(final String orderInfo) {
        ThreadPoolProxy.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderPayActivity.this);
                Map map = alipay.payV2(orderInfo, true);
                Message msg = mHandler.obtainMessage();
                msg.obj = map;
                msg.what = WHAT_VALUE;
                mHandler.sendMessage(msg);
                Logger.d("支付宝:" + map);
            }
        });
    }

    private int WHAT_VALUE = 1;
    private String orderInfo = "";
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            if (WHAT_VALUE == msg.what) {
                String result = ((Map) msg.obj).get("resultStatus").toString();
                if (Constants.PayStatus.APAY_SUCCESS == Integer.parseInt(result)) {
                    intent.putExtra(Constants.IntentParams.PAY_STATUS, Constants.PayStatus.APAY_SUCCESS);
                } else {
                    intent.putExtra(Constants.IntentParams.PAY_STATUS, -1);
                }
                intent.putExtra(Constants.IntentParams.DATA, orderInfo);
                intent.putExtra(Constants.IntentParams.ID, eventId);
                intent.setClass(mContext, ExhibitionPayStatusActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    public void alipaySuccess(String model) {
        orderInfo = model;
        execAlipay(model);
    }

    @Override
    public void alipayFail(String model) {
        CommonUtils.showToast(getContext(), model);
    }
}
