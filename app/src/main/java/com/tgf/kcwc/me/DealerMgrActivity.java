package com.tgf.kcwc.me;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.me.dealerbalance.DealerBalanceActivity;
import com.tgf.kcwc.me.dealerbalance.SetNonPwdPaymentActivity;
import com.tgf.kcwc.me.dealerbalance.resetpaypwd.ResetPwdActivity;
import com.tgf.kcwc.me.dealerbalance.setpaypwd.SettingPayPwdActivity;
import com.tgf.kcwc.mvp.model.AccountBalanceModel;
import com.tgf.kcwc.mvp.presenter.DealerWalletPresenter;
import com.tgf.kcwc.mvp.view.DealerWalletView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.SettingSelectedLayoutView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/11/2
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 店铺管理
 */

public class DealerMgrActivity extends BaseActivity {
    protected TextView orgBalanceTv;
    protected TextView payTitleTv;
    protected RelativeLayout payMgrLayout;
    protected SettingSelectedLayoutView setNonPaymentView;
    private DealerWalletPresenter mWalletPresenter;

    private int isFreePay = -1;
    private int isPwd = -1;

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("店铺管理");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_my_dealer_mgr_home);
        initView();
    }

    private void initView() {
        orgBalanceTv = (TextView) findViewById(R.id.orgBalanceTv);
        orgBalanceTv.setOnClickListener(this);
        payTitleTv = (TextView) findViewById(R.id.payTitleTv);
        payMgrLayout = (RelativeLayout) findViewById(R.id.payMgrLayout);
        payMgrLayout.setOnClickListener(this);
        setNonPaymentView = (SettingSelectedLayoutView) findViewById(R.id.setNonConfidentialPaymentView);
        setNonPaymentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isFreePay == -1) {
                    CommonUtils.showToast(mContext, "正在加载数据，请稍候!");
                    return;
                }
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.DATA, isFreePay);
                CommonUtils.startNewActivity(mContext,args, SetNonPwdPaymentActivity.class);

            }
        });
        mWalletPresenter = new DealerWalletPresenter();
        mWalletPresenter.attachView(dealerWalletView);
    }



    private DealerWalletView<AccountBalanceModel> dealerWalletView = new DealerWalletView<AccountBalanceModel>() {
        @Override
        public void showData(AccountBalanceModel accountBalanceModel) {


            showPayMgrInfo(accountBalanceModel);
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


    private void showPayMgrInfo(AccountBalanceModel accountBalanceModel) {

        TextView payTitleTv = findViewById(R.id.payTitleTv);

        String payTitle = "";
        isPwd = accountBalanceModel.isPwd;
        if (isPwd == 1) {
            payTitle = "修改支付密码";
        } else if (isPwd == 0) {
            payTitle = "设置支付密码";
        }
        payTitleTv.setText(payTitle);

        isFreePay = accountBalanceModel.isFreePay;
        if (isFreePay == 1) {

            setNonPaymentView.setStatus(true);
        } else if (isFreePay == 0) {
            setNonPaymentView.setStatus(false);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        mWalletPresenter.getAccountBalances(IOUtils.getToken(mContext));
    }

    @Override
    public void onClick(View view) {


        int id = view.getId();

        switch (id) {

            case R.id.orgBalanceTv:
                CommonUtils.startNewActivity(mContext, DealerBalanceActivity.class);
                break;

            case R.id.payMgrLayout:

                if (isPwd == -1) {
                    CommonUtils.showToast(mContext, "数据加载中，请稍候...");
                    return;
                }
                if (isPwd == 1) {
                    CommonUtils.startNewActivity(mContext, ResetPwdActivity.class);
                } else {
                    CommonUtils.startNewActivity(mContext, SettingPayPwdActivity.class);
                }
                break;
        }

    }
}
