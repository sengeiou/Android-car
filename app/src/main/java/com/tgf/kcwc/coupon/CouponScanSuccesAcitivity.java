package com.tgf.kcwc.coupon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.presenter.CouponScanPresenter;
import com.tgf.kcwc.mvp.view.CouponScanView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;

/**
 * Auther: Scott
 * Date: 2017/1/17 0017
 * E-mail:hekescott@qq.com
 */

public class CouponScanSuccesAcitivity extends BaseActivity implements CouponScanView {
    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("分发成功");
        backEvent(back);
    }

    @Override
    protected void setUpViews() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couponscans);
        Intent formIntent = getIntent();
        CouponScanPresenter couponScanPresenter = new CouponScanPresenter();
        couponScanPresenter.attachView(this);
        couponScanPresenter.postGetSendCoupon(IOUtils.getToken(getContext()) ,
                formIntent.getStringExtra(Constants.IntentParams.ID),
                formIntent.getStringExtra(Constants.IntentParams.ID2),
                formIntent.getStringExtra(Constants.IntentParams.DATA));
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            default:
                break;
        }
    }

    public Context getContext() {
        return CouponScanSuccesAcitivity.this;
    }

    @Override
    public void showScanSuccess() {

    }

    @Override
    public void showScanFailed() {

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }
}
