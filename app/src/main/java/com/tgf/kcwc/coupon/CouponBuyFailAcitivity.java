package com.tgf.kcwc.coupon;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Auther: Scott
 * Date: 2017/1/17 0017
 * E-mail:hekescott@qq.com
 */

public class CouponBuyFailAcitivity extends BaseActivity {
    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("支付失败");
        text.setTextColor(mRes.getColor(R.color.app_main_color2));
    }

    @Override
    protected void setUpViews() {
        findViewById(R.id.couponpay_cancel).setOnClickListener(this);
        findViewById(R.id.couponpay_continue).setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couponbuyf);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.couponpay_cancel:
                     finish();

                break;
            case R.id.couponpay_continue:
                CommonUtils.showToast(mContext, "继续支付");

                break;
            default:
                break;
        }
    }
}
