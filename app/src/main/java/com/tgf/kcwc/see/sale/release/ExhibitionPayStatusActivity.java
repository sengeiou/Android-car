package com.tgf.kcwc.see.sale.release;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.ThreadPoolProxy;
import com.tgf.kcwc.view.FunctionView;

import java.util.Map;

/**
 * 积分支付状态页面，主要用于支付宝付款成功后的提示页面
 */
public class ExhibitionPayStatusActivity extends BaseActivity {
    protected TextView goTicketBtn; //兑换成功按钮
    protected LinearLayout contentLayout; //兑换成功布局
    protected TextView successHint; //支付成功提示
    protected TextView paydescTv; //支付成功下面的话
    protected TextView continuePayBtn; //继续支付
    protected TextView phone; //联系电话
    protected LinearLayout payFailureLayout; //兑换失败布局
    protected TextView failContentTv;//兑换失败提示
    protected TextView cancelPayBtn; //取消订单

    private TextView titleText; //头部提示
    private int payStatus,eventId;
    private String orderInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        payStatus = intent.getIntExtra(Constants.IntentParams.PAY_STATUS, -1);
        orderInfo = intent.getStringExtra(Constants.IntentParams.DATA);
        eventId = intent.getIntExtra(Constants.IntentParams.ID,-1);
        setContentView(R.layout.exhibition_pay_result);
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
        if (Constants.PayStatus.APAY_SUCCESS == payStatus) {
            titleText.setText("二手车参展申请");
            function.setTextResource("参展须知",R.color.white,12);
            function.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: 2017/11/13
                    Intent intent = new Intent(ExhibitionPayStatusActivity.this, ApplyHintActivity.class);
                    intent.putExtra(Constants.IntentParams.ID, eventId);
                    intent.putExtra(Constants.IntentParams.INDEX, 1);
                    startActivity(intent);
                }
            });
        }else {
            titleText.setText("支付失败");
        }
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
                CommonUtils.startNewActivity(mContext, com.tgf.kcwc.me.sale.SaleMgrListActivity.class);
                finish();
                //CommonUtils.startNewActivity(mContext, TicketActivity.class);
                break;
        }
    }

    private void initView() {
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
                PayTask alipay = new PayTask(ExhibitionPayStatusActivity.this);
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