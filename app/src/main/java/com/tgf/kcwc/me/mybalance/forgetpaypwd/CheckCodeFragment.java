package com.tgf.kcwc.me.mybalance.forgetpaypwd;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.presenter.MyWalletPresenter;
import com.tgf.kcwc.mvp.presenter.SMSPresenter;
import com.tgf.kcwc.mvp.view.MyWalletView;
import com.tgf.kcwc.mvp.view.SMSDataView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.PayPwdInputView;

/**
 * Author:Jenny
 * Date:2017/10/18
 * E-mail:fishloveqin@gmail.com
 */

public class CheckCodeFragment extends BaseFragment {
    protected View rootView;
    protected TextView remarkTv;
    protected PayPwdInputView passwordInputView;
    protected TextView resendBtn;
    protected Button confirmBtn;
    private MyWalletPresenter mWalletPresenter;
    private SMSPresenter mSendMsgPresenter;
    private Account.UserInfo mUser;
    private String tel;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setpaypwd_check_code;
    }

    @Override
    protected void initView() {
        mUser = IOUtils.getAccount(mContext).userInfo;
        tel = mUser.tel;
        mWalletPresenter = new MyWalletPresenter();
        mWalletPresenter.attachView(myWalletView);
        mSendMsgPresenter = new SMSPresenter();
        mSendMsgPresenter.attachView(smsDataView);

        passwordInputView = findView(R.id.password);
        remarkTv = findView(R.id.remarkTv);
        if (!TextUtils.isEmpty(tel)) {
            mSendMsgPresenter.sendSMS(tel);
            countDownTimer.start();
            String localTel = tel.substring(0, 3) + "****" + tel.substring(8, 11);
            remarkTv.setText("我们已发送验证码到你手机\n" + localTel);
        }

        resendBtn = findView(R.id.resendBtn);
        resendBtn.setOnClickListener(this);
        confirmBtn = findView(R.id.confirmBtn);
        confirmBtn.setOnClickListener(this);
    }

    MyWalletView myWalletView = new MyWalletView() {
        @Override
        public void showData(Object object) {

            if (mCallback != null) {
                mCallback.refresh(passwordInputView.getPasswordString());
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
                mWalletPresenter.validateCheckCode(IOUtils.getToken(mContext), IOUtils.getAccount(mContext).userInfo.tel, passwordInputView.getPasswordString());
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
}
