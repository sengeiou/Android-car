package com.tgf.kcwc.me.dealerbalance.forgetpaypwd;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.PwdPatternCheckUtil;
import com.tgf.kcwc.view.PayPwdInputView;

/**
 * Author:Jenny
 * Date:2017/10/18
 * E-mail:fishloveqin@gmail.com
 */

public class ForgetStepSecondFragment extends BaseFragment {
    public PayPwdInputView passwordInputView;
    protected TextView remarkTv;
    protected TextView cipherSpecTv;
    protected Button nextStepBtn;
    private String tel;
    private Account.UserInfo mUser;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_resetpwd_step_second;
    }

    @Override
    protected void initView() {

        passwordInputView = findView(R.id.password);

        remarkTv = findView(R.id.remarkTv);
        mUser = IOUtils.getAccount(mContext).userInfo;
        tel = mUser.tel;
        if (!TextUtils.isEmpty(tel)) {
            String localTel = tel.substring(0, 3) + "****" + tel.substring(8, 11);
            remarkTv.setText("请为账号" + localTel + "\n设置六位数字支付密码");
        }
        cipherSpecTv = findView(R.id.cipherSpecTv);
        nextStepBtn = findView(R.id.nextStepBtn);
        nextStepBtn.setOnClickListener(this);
        passwordInputView.setPwdLengthListener(mListener);
    }

    private PayPwdInputView.PwdLengthListener mListener = new PayPwdInputView.PwdLengthListener() {
        @Override
        public void onLength(String pwd) {


            if (pwd.trim().length() == Constants.PWD_MAX_LENGTH) {
                boolean isInvalidate = PwdPatternCheckUtil.simpleNumCheck(pwd, Constants.PWD_MAX_LENGTH);
                if (isInvalidate) {
                    nextStepBtn.setEnabled(false);
                } else {
                    nextStepBtn.setEnabled(true);
                }
            } else {
                nextStepBtn.setEnabled(false);
            }

        }
    };

    @Override
    public void onClick(View v) {

        if (mCallback != null) {
            mCallback.refresh(passwordInputView.getPasswordString());
        }
    }
}
