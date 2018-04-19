package com.tgf.kcwc.redpack;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.view.FunctionView;

public class RedpackCreateSuccessActivity extends BaseActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createsuccess);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("创建成功");
        backEvent(back);
        function.setText("完成");
    }

    @Override
    protected void setUpViews() {

    }




    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.continuePayBtn:

                break;
        }

    }


}