package com.tgf.kcwc.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.view.EmojiTextView;
import com.tgf.kcwc.view.FunctionView;

/**
 * Auther: Scott
 * Date: 2017/7/17 0017
 * E-mail:hekescott@qq.com
 */

public class TestActivity extends Activity implements View.OnClickListener {

    private EditText testEd;
    private EmojiTextView testTv;
    private Button testBtn;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_testact);
        testEd = (EditText) findViewById(R.id.tested);
        testTv = (EmojiTextView) findViewById(R.id.test_tv);
        testBtn = (Button) findViewById(R.id.btn_sumbimt);
        testBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
             switch (view.getId()){
                         case R.id.btn_sumbimt:
                             testTv.setEmojiToTextView(testEd.getText()+"");
                             break;
                         default:
                             break;
                     }
    }
}
