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
import com.tgf.kcwc.util.RxBus;
import com.tgf.kcwc.view.FunctionView;

/**
 * Auther: Scott
 * Date: 2017/1/17 0017
 * E-mail:hekescott@qq.com
 */

public class CouponGetSuccesAcitivity extends BaseActivity {
    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("领取成功");
        backEvent(back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.$().post("CouponOrderActivity",RESULT_OK);
                finish();
            }
        });
    }

    @Override
    protected void setUpViews() {
        findViewById(R.id.couponbuy_see).setOnClickListener(this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couponbuys);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.couponbuy_see:
                Intent toMycouponIntent = new Intent(mContext,VoucherMainActivity.class);
                toMycouponIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                toMycouponIntent.putExtra(Constants.IntentParams.INDEX,3);
                startActivity(toMycouponIntent);
                break;
            default:
                break;
        }
    }

    public Context getContext() {
        return CouponGetSuccesAcitivity.this;
    }
}
