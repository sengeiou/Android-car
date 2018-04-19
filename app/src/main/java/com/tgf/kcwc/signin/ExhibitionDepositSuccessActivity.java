package com.tgf.kcwc.signin;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.view.FunctionView;

/**
 * @anthor noti
 * @time 2017/9/25
 * @describle 占位投诉成功
 */
public class ExhibitionDepositSuccessActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition_deposit_success);
    }

    @Override
    protected void setUpViews() {
        findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("占位投诉");
//        function.setImageResource(R.drawable.icon_common_right);
//        function.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            protected void onSingleClick(View view) {
//
//            }
//        });
    }
}
