package com.tgf.kcwc.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.view.FunctionView;

/**
 * Auther: Scott
 * Date: 2016/12/15 0015
 * E-mail:hekescott@qq.com
 */

public class FindBackPWDActivity extends BaseActivity {
    private EditText findbackPhonenumEd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findbackpwd);
    }

    @Override
    protected void setUpViews() {


        findbackPhonenumEd = (EditText) findViewById(R.id.editmyinfo_nickname_ed);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.login_findback_pwd);
        text.setTextColor(mRes.getColor(R.color.app_title_color1));
        findViewById(R.id.submit_btn).setOnClickListener(this);
        //        function.setTextResource(R.string.reset, R.color.tab_text_s_color, 16);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_btn:
                Intent intent =new Intent(mContext,FindBackResetPWDActivity.class);
                intent.putExtra("phonenum",findbackPhonenumEd.getText()+"");
                startActivity(intent);
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
