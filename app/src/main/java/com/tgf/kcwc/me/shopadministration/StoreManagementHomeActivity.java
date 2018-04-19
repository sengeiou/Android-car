package com.tgf.kcwc.me.shopadministration;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.view.FunctionView;

/**
 * Created by Administrator on 2017/11/8.
 * 店铺管理
 */

public class StoreManagementHomeActivity extends BaseActivity {

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storemanagement_home);
    }
}
