package com.tgf.kcwc.play.topic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;

/**
 * Created by Administrator on 2017/5/16.
 */

public class CompieTopicLeadActivity extends BaseActivity{

    protected String intro;
    protected EditText mEdtext;
    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("编辑导语");
        function.setText("完成");
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEdtext.getText().toString().trim()!=null&&!mEdtext.getText().toString().trim().equals("")){
                    back();
                }else {
                    CommonUtils.showToast(mContext, "导语不能为空");
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compielead);
        mEdtext= (EditText) findViewById(R.id.edtext);
        intro = (String) getIntent().getSerializableExtra("intro");
        mEdtext.setText(intro);
    }

    public void back(){
        Intent intent = new Intent();
        intent.putExtra(Constants.IntentParams.DATA, mEdtext.getText().toString().trim());
        setResult(RESULT_OK, intent);
        finish();
    }

}
