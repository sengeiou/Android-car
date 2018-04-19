package com.tgf.kcwc.me.dealerbalance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.BankCardModel;
import com.tgf.kcwc.mvp.presenter.DealerBindBankCardPresenter;
import com.tgf.kcwc.mvp.view.BindBankCardView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.EncryptUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.DealerPayPwdValidatePopupWindow;
import com.tgf.kcwc.view.FunctionView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/10/16
 * E-mail:fishloveqin@gmail.com
 */

public class AddBankCardStepSecondActivity extends BaseActivity implements BindBankCardView {


    protected TextView cardTypeTitleTv;
    protected TextView cardTypeTv;
    protected TextView phoneNumberTitleTv;
    protected TextView phoneNumberTv;
    protected Button nextStepBtn;

    private DealerBindBankCardPresenter mPresenter;
    private String mName, mCardCode;

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("添加银行卡");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mName = intent.getStringExtra(Constants.IntentParams.DATA);
        mCardCode = intent.getStringExtra(Constants.IntentParams.DATA2);
        super.setContentView(R.layout.activity_add_bank_card_step_second);
        initView();
    }

    private void initView() {
        cardTypeTitleTv = (TextView) findViewById(R.id.cardTypeTitleTv);
        cardTypeTv = (TextView) findViewById(R.id.cardTypeTv);
        phoneNumberTitleTv = (TextView) findViewById(R.id.phoneNumberTitleTv);
        phoneNumberTv = (TextView) findViewById(R.id.phoneNumberTv);
        nextStepBtn = (Button) findViewById(R.id.nextStepBtn);
        nextStepBtn.setOnClickListener(this);
        TextView msgTv=findViewById(R.id.msgTv);
        msgTv.setText("为保证资金安全，请绑定本机构银行卡");
        mPresenter = new DealerBindBankCardPresenter();
        mPresenter.attachView(this);
        mPresenter.getBankCardInfo(IOUtils.getToken(mContext), mCardCode);
    }


    @Override
    public void onClick(View view) {


        validatePwd();


    }

    private void validatePwd() {

        final DealerPayPwdValidatePopupWindow payPwdValidatePopupWindow = new DealerPayPwdValidatePopupWindow(this, null);
        payPwdValidatePopupWindow.setValidateListener(new DealerPayPwdValidatePopupWindow.PayPwdValidateListener() {

            @Override
            public void onSuccess(String pwd) {
                mPresenter.bindBankCard(builderBankCardRequestParams(pwd));
                payPwdValidatePopupWindow.dismiss();
            }

            @Override
            public void onFailure(String pwd) {

                CommonUtils.showToast(mContext, "输入密码不对，请重新输入！");

            }
        });
        payPwdValidatePopupWindow.show(this);
    }

    private Map<String, String> builderBankCardRequestParams(String pwd) {

        Map<String, String> params = new HashMap<String, String>();

        /**  token：
         * card_code: 卡号
         * name: 持卡人姓名
         * pay_password: 支付密码 */
        params.put("token", IOUtils.getToken(mContext));
        params.put("card_code", mCardCode);
        params.put("pay_password", EncryptUtil.encode(pwd));
        params.put("name", mName);

        return params;
    }

    @Override
    public void loadCardInfo(BankCardModel model) {

        cardTypeTv.setText(model.bankInfo.name);
        nextStepBtn.setEnabled(true);
    }

    @Override
    public void bindCard(boolean isSuccess) {

        if (isSuccess) {
            CommonUtils.showToast(mContext, "添加银行卡成功!");

            List<Activity> allPages = KPlayCarApp.getActivityStack();
            for (Activity a : allPages) {

                if (a instanceof AddBankCardStepSecondActivity || a instanceof AddBankCardStepFirstActivity) {
                    a.finish();
                }
            }
        } else {
            CommonUtils.showToast(mContext, "添加失败，请重试!");
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
}
