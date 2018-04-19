package com.tgf.kcwc.me.mybalance;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.me.mybalance.setpaypwd.SettingPayPwdActivity;
import com.tgf.kcwc.mvp.model.AccountBalanceModel;
import com.tgf.kcwc.mvp.presenter.MyWalletPresenter;
import com.tgf.kcwc.mvp.view.MyWalletView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/10/16
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 我的零钱
 */

public class MyBalanceActivity extends BaseActivity implements MyWalletView<AccountBalanceModel> {
    protected TextView balanceTv;
    protected ImageView img;
    protected RelativeLayout prePaidLayout;
    protected ImageView img2;
    protected RelativeLayout withdrawCashLayout;
    protected ImageView img3;
    protected RelativeLayout accountCheckingLayout;
    private MyWalletPresenter mWalletPresenter;
    AccountBalanceModel mModel;
    private int isPwd = -1;
    private static final int EXIST_PWD = 1;

    @Override
    protected void setUpViews() {
        initView();
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        text.setText("我的零钱");
        text.setTextSize(16);
        function.setTextResource("零钱明细", R.color.white, 14);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonUtils.startNewActivity(mContext, BalanceStatementActivity.class);

            }
        });
        backEvent(back);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_balance);

    }

    private void initView() {
        balanceTv = (TextView) findViewById(R.id.balanceTv);
        img = (ImageView) findViewById(R.id.img);
        prePaidLayout = (RelativeLayout) findViewById(R.id.prePaidLayout);
        prePaidLayout.setOnClickListener(this);
        img2 = (ImageView) findViewById(R.id.img2);
        withdrawCashLayout = (RelativeLayout) findViewById(R.id.withdrawCashLayout);
        withdrawCashLayout.setOnClickListener(this);
        img3 = (ImageView) findViewById(R.id.img3);
        accountCheckingLayout = (RelativeLayout) findViewById(R.id.accountCheckingLayout);
        accountCheckingLayout.setOnClickListener(this);
        findViewById(R.id.myBankCardLayout).setOnClickListener(this);
        mWalletPresenter = new MyWalletPresenter();
        mWalletPresenter.attachView(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        mWalletPresenter.getAccountBalances(IOUtils.getToken(mContext));
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (isPwd == -1) {
            CommonUtils.showToast(mContext, "正在加载数据...");
            return;
        }
        switch (id) {

            case R.id.prePaidLayout:

                if (isPwd == EXIST_PWD) {
                    CommonUtils.startNewActivity(mContext, WXPrePaidActivity.class);
                } else {
                    CommonUtils.startNewActivity(mContext, SettingPayPwdActivity.class);
                }

                break;
            case R.id.withdrawCashLayout:

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                if (isPwd == EXIST_PWD) {

                    args.put(Constants.IntentParams.DATA, "");
                    CommonUtils.startNewActivity(mContext, args, WXWithdrawCashActivity.class);
                } else {
                    CommonUtils.startNewActivity(mContext, SettingPayPwdActivity.class);
                }

                break;
            case R.id.accountCheckingLayout:
                CommonUtils.startNewActivity(mContext, CheckingAccountActivity.class);

                break;

            case R.id.myBankCardLayout:


                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.DATA, isPwd);
                CommonUtils.startNewActivity(mContext, args, MyBankCardActivity.class);
                break;
        }


    }

    private void validatePwd(String pwd) {


    }

    @Override
    public void showData(AccountBalanceModel model) {

        mModel = model;
        isPwd = model.isPwd;
        showBalances(model);

    }

    private void showBalances(AccountBalanceModel model) {

        balanceTv.setText(model.money + "");
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
}
