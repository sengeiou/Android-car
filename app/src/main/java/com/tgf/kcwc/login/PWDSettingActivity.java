package com.tgf.kcwc.login;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.view.FunctionView;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Auther: Scott
 * Date: 2016/12/15 0015
 * E-mail:hekescott@qq.com
 */

public class PWDSettingActivity extends BaseActivity {
    private boolean      cansSeePwd;
    private EditText pwdCodeEd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingpwd);
    }

    @Override
    protected void setUpViews() {
        pwdCodeEd = (EditText) findViewById(R.id.setpwd_code_ed);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("登录");
        text.setTextColor(mRes.getColor(R.color.app_main_color1));
        findViewById(R.id.submit_btn).setOnClickListener(this);
        //        function.setTextResource(R.string.reset, R.color.tab_text_s_color, 16);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_btn:

                break;
            case R.id.seepwd_bt:
                cansSeePwd = !cansSeePwd;

                if (cansSeePwd) {
                    pwdCodeEd.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    pwdCodeEd.setInputType(
                            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

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
