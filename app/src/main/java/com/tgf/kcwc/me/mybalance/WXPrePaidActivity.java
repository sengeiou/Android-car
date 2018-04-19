package com.tgf.kcwc.me.mybalance;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.AccountBalanceModel;
import com.tgf.kcwc.mvp.model.BankCardModel;
import com.tgf.kcwc.mvp.model.OrderPayParam;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.presenter.MyWalletPresenter;
import com.tgf.kcwc.mvp.view.MyWalletView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.PayUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.HashMap;
import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/10/24
 * E-mail:fishloveqin@gmail.com
 * 微信充值
 */

public class WXPrePaidActivity extends BaseActivity implements MyWalletView<BankCardModel> {
    protected ImageView bankCardImg;
    protected TextView bankName;
    protected RelativeLayout addBankLayout1;
    protected ImageView bankCardImg2;
    protected TextView bankName2;
    protected TextView cardInfoTv;
    protected RelativeLayout addBankLayout2;
    protected RelativeLayout addBankCardLayout;
    protected TextView moneyTagTv;
    protected EditText cashET;
    protected TextView balanceTv;
    protected Button nextStepBtn;

    private MyWalletPresenter mPresenter, mPrePaidPresenter, mBalancePresenter;
    private BankCardModel mModel;

    @Override
    protected void setUpViews() {
        initView();
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("充值");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_wx_pre_paid);

    }

    private void initView() {
        bankCardImg = (ImageView) findViewById(R.id.bankCardImg);
        bankName = (TextView) findViewById(R.id.bankName);
        addBankLayout1 = (RelativeLayout) findViewById(R.id.addBankLayout1);
        bankCardImg2 = (ImageView) findViewById(R.id.bankCardImg2);
        bankName2 = (TextView) findViewById(R.id.bankName2);
        cardInfoTv = (TextView) findViewById(R.id.cardInfoTv);
        addBankLayout2 = (RelativeLayout) findViewById(R.id.addBankLayout2);
        addBankCardLayout = (RelativeLayout) findViewById(R.id.addBankCardLayout);
        moneyTagTv = (TextView) findViewById(R.id.moneyTagTv);
        cashET = (EditText) findViewById(R.id.cashET);
        balanceTv = (TextView) findViewById(R.id.balanceTv);
        nextStepBtn = (Button) findViewById(R.id.nextStepBtn);

        addBankLayout1.setOnClickListener(this);
        addBankLayout2.setOnClickListener(this);
        nextStepBtn.setOnClickListener(this);

        //设置Input的类型两种都要

        cashET.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

//设置字符过滤
        cashET.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(".") && dest.toString().length() == 0) {
                    return "0.";
                }
                if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    int length = dest.toString().substring(index).length();
                    if (length == 3) {
                        return "";
                    }
                }
                if (dest.toString().length() > 16) {

                    return "";
                }
                return null;
            }
        }});

        cashET.addTextChangedListener(mWatcher);

        mPresenter = new MyWalletPresenter();
        mPresenter.attachView(this);
        mPrePaidPresenter = new MyWalletPresenter();
        mPrePaidPresenter.attachView(mPrePaidView);
        mBalancePresenter = new MyWalletPresenter();
        mBalancePresenter.attachView(mBalanceView);
        mBalancePresenter.getAccountBalances(IOUtils.getToken(mContext));
    }


    private TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (TextUtil.isEmpty(s.toString().trim()) || (mModel != null && mModel.bankInfo == null)) {
                nextStepBtn.setEnabled(false);
            } else {

                nextStepBtn.setEnabled(true);
            }
        }
    };


    private AccountBalanceModel mAccountBalanceModel;
    private MyWalletView<AccountBalanceModel> mBalanceView = new MyWalletView<AccountBalanceModel>() {
        @Override
        public void showData(AccountBalanceModel accountBalanceModel) {

            mAccountBalanceModel = accountBalanceModel;
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

    private final String mMode = "01";
    private MyWalletView<ResponseMessage<OrderPayParam>> mPrePaidView = new MyWalletView<ResponseMessage<OrderPayParam>>() {
        @Override
        public void showData(ResponseMessage<OrderPayParam> resp) {

            if (resp.statusCode == Constants.NetworkStatusCode.SUCCESS) {

                execWXPay(resp.data);
            } else {

                CommonUtils.showToast(mContext, resp.statusMessage);
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
        public Context getContext() {
            return mContext;
        }
    };

    private void execWXPay(OrderPayParam data) {

        PayUtil.weixinPay(mGlobalContext.getMsgApi(), data.appId, data.partnerId,
                data.prepayId, data.wxPackage, data.nonceStr,
                data.timeStamp, data.paySign, "5");

        KPlayCarApp.putValue(Constants.IntentParams.PRE_PAID_DATA, data);
    }

    @Override
    public void showData(BankCardModel model) {

        balanceTv.setText("可用余额：" + model.money + "元");
        mModel = model;
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

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getSelectedBankCard(IOUtils.getToken(mContext));
    }


    @Override
    public void onClick(View view) {


        int id = view.getId();

        switch (id) {

            case R.id.nextStepBtn:

                if (mModel == null) {

                    CommonUtils.showToast(mContext, "请选择要提现的银行卡");
                    return;
                }

                String cash = cashET.getText().toString();
                if (Double.parseDouble(cash) <= 0.0d) {

                    CommonUtils.showToast(mContext, "充值金额必须大于0!");
                    return;
                }

                if (mAccountBalanceModel == null) {

                    CommonUtils.showToast(mContext, "正在加载数据...");
                    return;
                }


                mPrePaidPresenter.wxPrePaidToCard(prePaidReqParams());
                break;

        }


    }


    private Map<String, String> prePaidReqParams() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("token", IOUtils.getToken(mContext));
        params.put("money", cashET.getText().toString());
        params.put("trade_type", "APP");
        return params;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }

        if (mPrePaidPresenter != null) {

            mPrePaidPresenter.detachView();
        }
    }


}
