package com.tgf.kcwc.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.view.FunctionView;

/**
 * Author:Jenny
 * Date:2017/11/29
 * E-mail:fishloveqin@gmail.com
 */

public class ScannerCodeResultActivity extends BaseActivity {
    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("扫码结果");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String content = intent.getStringExtra(Constants.IntentParams.DATA);
        setContentView(R.layout.activity_scanner_code_result);

        TextView contentTv = findViewById(R.id.contentTv);
        contentTv.setText(content);
    }
}
