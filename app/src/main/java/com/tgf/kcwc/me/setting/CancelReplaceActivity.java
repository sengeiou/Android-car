package com.tgf.kcwc.me.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.view.FunctionView;

/**
 * @anthor noti
 * @time 2017/8/23
 * @describle 撤销及更换绑定
 */
public class CancelReplaceActivity extends BaseActivity{
    private RelativeLayout cancelRl;
    private LinearLayout cancelll;
    private TextView phoneTv;
    private Button bindBtn;
    private String title;
    @Override
    protected void setUpViews() {
        title = getIntent().getStringExtra(Constants.IntentParams.TITLE);
        bindBtn = (Button) findViewById(R.id.cancel_bind_btn);
        cancelRl = (RelativeLayout) findViewById(R.id.cancel_rl);
        cancelll = (LinearLayout) findViewById(R.id.cancel_ll);
        phoneTv = (TextView) findViewById(R.id.cancel_phone);

        bindBtn.setOnClickListener(this);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_replace);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.cancel_code_tv:break;
            case R.id.cancel_bind_btn:break;
        }
    }
}
