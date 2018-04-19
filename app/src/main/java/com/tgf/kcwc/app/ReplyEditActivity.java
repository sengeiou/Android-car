package com.tgf.kcwc.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;

/**
 * Auther: Scott
 * Date: 2016/12/26 0026
 * E-mail:hekescott@qq.com
 */

public class ReplyEditActivity extends BaseActivity {

    private  String name;
    private  Intent fromIntent;
    private EditText editText;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        function.setTextResource("发送",R.color.btn_color1,14);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editText.getText()+"";
                if(TextUtils.isEmpty(content)){
                    CommonUtils.showToast(mContext,"内容不能为空");
                }else {
                    fromIntent.putExtra("data",content);
                    setResult(RESULT_OK, fromIntent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void setUpViews() {
        findViewById(R.id.title_bar_backtv).setOnClickListener(this);
        editText = (EditText) findViewById(R.id.reply_content);
        findViewById(R.id.reply_emotioniv).setOnClickListener(this);
        fromIntent = getIntent();
        name = fromIntent.getStringExtra("carName");
        editText.setHint("@"+name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replyedit);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_backtv:
                finish();
                break;
            case R.id.reply_emotioniv:
                CommonUtils.showToast(mContext,"表情");
                break;
            default:
                break;
        }
    }
}
