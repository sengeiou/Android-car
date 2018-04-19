package com.tgf.kcwc.me.mybalance.resetpaypwd;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.presenter.MyWalletPresenter;
import com.tgf.kcwc.mvp.view.MyWalletView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.EncryptUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.PayPwdInputView;

/**
 * Author:Jenny
 * Date:2017/10/18
 * E-mail:fishloveqin@gmail.com
 */

public class ValidateOldPwdFragment extends BaseFragment implements MyWalletView {

    protected View rootView;
    protected TextView remarkTv;
    public PayPwdInputView password;
    protected Button confirmBtn;

    private MyWalletPresenter mWalletPresenter;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_validate_old_pwd;
    }

    @Override
    protected void initView() {
        remarkTv = findView(R.id.remarkTv);
        password = findView(R.id.password);
        confirmBtn = findView(R.id.confirmBtn);
        confirmBtn.setOnClickListener(this);
        mWalletPresenter = new MyWalletPresenter();
        mWalletPresenter.attachView(this);
    }

    @Override
    public void onClick(View v) {

        String pwd = password.getPasswordString();
        if (!TextUtil.isEmpty(pwd)) {

            KPlayCarApp.putValue(Constants.KeyParams.PAY_OLD_PWD, pwd);
            mWalletPresenter.validatePayPwd(IOUtils.getToken(mContext), EncryptUtil.encode(pwd));
        }

    }

    @Override
    public void showData(Object object) {

        if (mCallback != null) {
            mCallback.refresh(password.getPasswordString());
        }

    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        dismissLoadingDialog();
    }

    @Override
    public void backEvent() {

        if (password != null) {

            CommonUtils.closeSoftKeyword(password);
        }
    }
}
