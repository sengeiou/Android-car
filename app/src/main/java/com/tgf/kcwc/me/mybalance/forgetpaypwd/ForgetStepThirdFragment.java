package com.tgf.kcwc.me.mybalance.forgetpaypwd;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.presenter.MyWalletPresenter;
import com.tgf.kcwc.mvp.view.MyWalletView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.EncryptUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.PayPwdInputView;

import java.util.HashMap;
import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/10/18
 * E-mail:fishloveqin@gmail.com
 */

public class ForgetStepThirdFragment extends BaseFragment implements MyWalletView {
    public PayPwdInputView passwordInputView;
    protected TextView remarkTv;
    protected TextView cipherSpecTv;
    protected Button confirmBtn;
    private String tel;
    private Account.UserInfo mUser;
    private MyWalletPresenter mWalletPresenter;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_resetpwd_step_third;
    }

    @Override
    protected void initView() {

        passwordInputView = findView(R.id.password);

        remarkTv = findView(R.id.remarkTv);
        mUser = IOUtils.getAccount(mContext).userInfo;
        tel = mUser.tel;
        cipherSpecTv = findView(R.id.cipherSpecTv);
        confirmBtn = findView(R.id.confirmBtn);
        confirmBtn.setOnClickListener(this);
        mWalletPresenter = new MyWalletPresenter();
        mWalletPresenter.attachView(this);
    }

    @Override
    public void onClick(View v) {

        if (mWalletPresenter != null) {
            mWalletPresenter.setPayPwd(builderReqParams());
        }


    }

    @Override
    public void registerEvent(Object obj) {

        passwordInputView.setComparePassword(obj.toString(), new PayPwdInputView.onPasswordListener() {
            @Override
            public void onDifference() {

                if(passwordInputView.getPasswordString().length()==Constants.PWD_MAX_LENGTH){
                    CommonUtils.showToast(mContext,"两次输入的密码不一致，请重新输入！");
                    passwordInputView.setText("");
                }
                confirmBtn.setEnabled(false);
            }

            @Override
            public void onEqual(String psd) {

                confirmBtn.setEnabled(true);
            }
        });
    }

    @Override
    public void showData(Object object) {

        CommonUtils.showToast(mContext, "修改支付密码成功");
        KPlayCarApp.removeValue(Constants.KeyParams.CHECK_CODE);
        getActivity().finish();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        dismissLoadingDialog();
    }

    private Map<String, String> builderReqParams() {

        Map<String, String> params = new HashMap<String, String>();

        /** token
         old_password:老支付密码  和验证码传其中一个
         ver_code:验证码  和老支付密码传其中一个
         new_password:新密码
         **/

        String checkCode = KPlayCarApp.getValue(Constants.KeyParams.CHECK_CODE);
        params.put("old_password", "");
        params.put("ver_code", checkCode);
        params.put("new_password", EncryptUtil.encode(passwordInputView.getPasswordString()));
        params.put("token", IOUtils.getToken(mContext));
        return params;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWalletPresenter != null) {
            mWalletPresenter.detachView();
        }
    }
}
