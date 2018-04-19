package com.tgf.kcwc.redpack;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.view.FunctionView;

/**
 * Auther: Scott
 * Date: 2017/11/9 0009
 * E-mail:hekescott@qq.com
 */

public class RedpackEventDetailActivity extends BaseActivity {
    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redpack_eventdetail);
    }
}
