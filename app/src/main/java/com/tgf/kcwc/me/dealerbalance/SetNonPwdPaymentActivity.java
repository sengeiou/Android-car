package com.tgf.kcwc.me.dealerbalance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.presenter.DealerWalletPresenter;
import com.tgf.kcwc.mvp.presenter.SMSPresenter;
import com.tgf.kcwc.mvp.view.DealerWalletView;
import com.tgf.kcwc.mvp.view.SMSDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PayPwdInputView;

import java.util.HashMap;
import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/10/30
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 免密码支付设置
 */

public class SetNonPwdPaymentActivity extends BaseActivity {
    protected TextView remarkTv;
    protected PayPwdInputView password;
    protected TextView resendBtn;
    protected Button confirmBtn;
    private DealerWalletPresenter mSetNonPwdPaymentPresenter;
    private SMSPresenter mSendMsgPresenter;
    private Account.UserInfo mUser;
    private String tel;
    private int isFreePay = -1;

    @Override
    protected void setUpViews() {

        initView();
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("小额免密支付");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set_non_pwd_payment);

        Intent intent = getIntent();
        isFreePay = intent.getIntExtra(Constants.IntentParams.DATA, -1);
    }


    protected void initView() {
        mUser = IOUtils.getAccount(mContext).userInfo;
        tel = mUser.tel;
        mSetNonPwdPaymentPresenter = new DealerWalletPresenter();
        mSetNonPwdPaymentPresenter.attachView(dealerWalletView);
        mSendMsgPresenter = new SMSPresenter();
        mSendMsgPresenter.attachView(smsDataView);

        remarkTv = findViewById(R.id.remarkTv);
        if (!TextUtils.isEmpty(tel)) {
            mSendMsgPresenter.sendSMS(tel);
            countDownTimer.start();
            String localTel = tel.substring(0, 3) + "****" + tel.substring(8, 11);
            remarkTv.setText("我们已发送验证码到店铺管理人的手机\n" + localTel);
        }

        resendBtn = findViewById(R.id.resendBtn);
        resendBtn.setOnClickListener(this);
        confirmBtn = findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(this);
        remarkTv = (TextView) findViewById(R.id.remarkTv);
        password = (PayPwdInputView) findViewById(R.id.password);
        resendBtn = (TextView) findViewById(R.id.resendBtn);
        confirmBtn = (Button) findViewById(R.id.confirmBtn);
    }

    DealerWalletView<ResponseMessage<Object>> dealerWalletView = new DealerWalletView<ResponseMessage<Object>>() {
        @Override
        public void showData(ResponseMessage<Object> responseMessage) {


            int statusCode = responseMessage.statusCode;
            if (Constants.NetworkStatusCode.SUCCESS == statusCode) {
                finish();

                CommonUtils.showToast(mContext, "设置成功！");
            } else {
                CommonUtils.showToast(mContext, responseMessage.statusMessage);
            }

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

    private SMSDataView smsDataView = new SMSDataView() {
        @Override
        public void loadData(Object object) {

            resendBtn.setEnabled(false);
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
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.confirmBtn:

                if (isFreePay == -1) {

                    return;
                }
                setNonPwdPayment();
                break;
            case R.id.resendBtn:
                mSendMsgPresenter.sendSMS(tel);
                countDownTimer.start();
                break;
        }


    }

    private CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            resendBtn.setTextColor(mRes.getColor(R.color.text_color17));
            resendBtn.setText((millisUntilFinished / 1000) + "秒" + "后重发");
        }

        @Override
        public void onFinish() {
            resendBtn.setText("重发短信");
            resendBtn.setTextColor(mRes.getColor(R.color.text_color6));
            resendBtn.setEnabled(true);
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        countDownTimer.cancel();
    }

    private void setNonPwdPayment() {


        mSetNonPwdPaymentPresenter.setNonPwdPayment(builderSetNonPwdPaymentReqParams());

    }

    private Map<String, String> builderSetNonPwdPaymentReqParams() {

        Map<String, String> params = new HashMap<String, String>();
        /* token
     quota:免密额度
    ver_code：验证码
    status:1 开启 0 关闭*/

        params.put("token", IOUtils.getToken(mContext));

        String status = "";
        if (isFreePay == 1) {
            status = "0";
        } else if (isFreePay == 0) {
            status = "1";
        }
        params.put("status", status);
        params.put("ver_code", password.getPasswordString());
        return params;

    }
}
