package com.tgf.kcwc.tripbook;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.view.FunctionView;

/**
 * Auther: Scott
 * Date: 2017/5/10 0010
 * E-mail:hekescott@qq.com
 */

public class CheckEndLineActivity extends BaseActivity {
    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("制作路书");
        backEvent(back);
    }

    @Override
    protected void setUpViews() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
