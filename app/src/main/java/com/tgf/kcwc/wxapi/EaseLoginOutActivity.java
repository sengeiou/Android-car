package com.tgf.kcwc.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.login.LoginActivity;
import com.tgf.kcwc.login.LoginOutSevice;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.NotitleContentDialog;

/**
 * Auther: Scott
 * Date: 2017/11/16 0016
 * E-mail:hekescott@qq.com
 */

public class EaseLoginOutActivity extends Activity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_easeloginout);
        findViewById(R.id.loginout_cancletv).setOnClickListener(this);
        findViewById(R.id.loginout_oktv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
             switch (v.getId()){
                         case R.id.loginout_oktv:
                            startActivity(new Intent(this, LoginActivity.class));
                            finish();
                             break;
                         case R.id.loginout_cancletv:
                             finish();
                             break;
                         default:
                             break;
                     }
    }
}
