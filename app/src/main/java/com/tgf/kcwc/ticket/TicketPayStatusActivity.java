package com.tgf.kcwc.ticket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.presenter.OrderPayPresenter;
import com.tgf.kcwc.mvp.presenter.OrderPaysPresenter;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.ThreadPoolProxy;
import com.tgf.kcwc.view.FunctionView;

import java.util.Map;

/**
 * 门票支付状态页面，主要用于支付宝付款成功后的提示页面
 */
public class TicketPayStatusActivity extends BaseActivity {
    protected RelativeLayout titleLayout;
    protected TextView paydescTv;
    protected TextView goTicketBtn;
    protected LinearLayout contentLayout;
    protected TextView failContentTv;
    protected TextView cancelPayBtn;
    protected TextView continuePayBtn;
    protected LinearLayout payFailureLayout;


    private TextView mBackBtn;

    private OrderPayPresenter mPresenter;

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
    private OrderPaysPresenter orderPaysPresenter;
    private int payStatus;
    private String orderInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        payStatus = intent.getIntExtra(Constants.IntentParams.PAY_STATUS, -1);
        orderInfo = intent.getStringExtra(Constants.IntentParams.DATA);
        setContentView(R.layout.ticket_pay_result);

        if (Constants.PayStatus.APAY_SUCCESS == payStatus) {
            contentLayout.setVisibility(View.VISIBLE);
            payFailureLayout.setVisibility(View.GONE);
        } else {
            contentLayout.setVisibility(View.GONE);
            payFailureLayout.setVisibility(View.VISIBLE);
        }

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

        initView();
    }


    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.cancelPayBtn:
                finish();
                break;

            case R.id.continuePayBtn:

                if (!TextUtil.isEmpty(orderInfo)) {

                    execAlipay(orderInfo);
                }

                break;

            case R.id.goTicketBtn:

                finish();
                CommonUtils.startNewActivity(mContext, TicketActivity.class);
                break;
        }

    }


    private void initView() {
        titleLayout = (RelativeLayout) findViewById(R.id.title_layout);
        successHint = (TextView) findViewById(R.id.success_hint);
        paydescTv = (TextView) findViewById(R.id.paydesc_tv);
        goTicketBtn = (TextView) findViewById(R.id.goTicketBtn);
        goTicketBtn.setOnClickListener(this);
        phone = (TextView) findViewById(R.id.phone);
        contentLayout = (LinearLayout) findViewById(R.id.contentLayout);
        failContentTv = (TextView) findViewById(R.id.failContentTv);
        cancelPayBtn = (TextView) findViewById(R.id.cancelPayBtn);
        continuePayBtn = (TextView) findViewById(R.id.continuePayBtn);
        payFailureLayout = (LinearLayout) findViewById(R.id.payFailureLayout);
        cancelPayBtn.setOnClickListener(this);
        continuePayBtn.setOnClickListener(this);
    }


    /**
     * 支付宝支付
     */
    private void execAlipay(final String orderInfo) {


        ThreadPoolProxy.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {

                PayTask alipay = new PayTask(TicketPayStatusActivity.this);
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
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            Intent intent = new Intent();
            if (WHAT_VALUE == msg.what) {

                String result = ((Map) msg.obj).get("resultStatus").toString();


                if (Constants.PayStatus.APAY_SUCCESS == Integer.parseInt(result)) {
                    contentLayout.setVisibility(View.VISIBLE);
                    payFailureLayout.setVisibility(View.GONE);
                } else {
                    contentLayout.setVisibility(View.GONE);
                    payFailureLayout.setVisibility(View.VISIBLE);
                }

            }


        }
    };
}