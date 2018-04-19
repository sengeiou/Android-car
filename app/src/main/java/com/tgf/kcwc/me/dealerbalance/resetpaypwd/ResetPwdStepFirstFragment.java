package com.tgf.kcwc.me.dealerbalance.resetpaypwd;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.util.IOUtils;

/**
 * Author:Jenny
 * Date:2017/10/18
 * E-mail:fishloveqin@gmail.com
 * 修改支付密码
 */

public class ResetPwdStepFirstFragment extends BaseFragment {
    protected TextView remarkTv;
    protected Button forgetBtn;
    protected Button rememberBtn;
    private Account.UserInfo mUser;
    private String tel;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_resetpwd_step_first;
    }

    @Override
    protected void initView() {
        mUser = IOUtils.getAccount(mContext).userInfo;
        tel = mUser.tel;
        remarkTv = findView(R.id.remarkTv);
        if (!TextUtils.isEmpty(tel)) {
            String localTel = tel.substring(0, 3) + "****" + tel.substring(8, 11);
            remarkTv.setText("您是否记得账号" + localTel+"\n当前使用的支付密码");
        }
        forgetBtn = findView(R.id.forgetBtn);
        forgetBtn.setOnClickListener(this);
        rememberBtn = findView(R.id.rememberBtn);
        rememberBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if(mCallback!=null){
            mCallback.refresh(v);
        }



    }


}
