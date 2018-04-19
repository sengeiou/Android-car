package com.tgf.kcwc.businessconcerns;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.view.FunctionView;

/**
 * @anthor noti
 * @time 2017/8/3 9:51
 * 商务记录-看车记录
 */

public class CarRecordActivity extends BaseActivity {
    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_record);
    }
}
