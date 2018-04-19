package com.tgf.kcwc.login;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.view.FunctionView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Auther: Scott
 * Date: 2016/12/15 0015
 * E-mail:hekescott@qq.com
 */

public class VertifyPhoneActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertifyphone);
    }

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("验证手机号");
        text.setTextColor(mRes.getColor(R.color.app_main_color1));
        findViewById(R.id.submit_btn).setOnClickListener(this);
        //        function.setTextResource(R.string.reset, R.color.tab_text_s_color, 16);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_btn:

                break;
            default:
                break;
        }
    }

    /**
     * 返回事件
     *
     * @param button
     */
    protected void backEvent(View button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
