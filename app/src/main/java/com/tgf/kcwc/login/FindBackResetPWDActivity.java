package com.tgf.kcwc.login;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.mvp.presenter.EditUserInfoPresenter;
import com.tgf.kcwc.mvp.view.EditUserInfoView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;

import android.content.Context;
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

public class FindBackResetPWDActivity extends BaseActivity implements EditUserInfoView {

    private TextView findbackTitleTv;
    private EditUserInfoPresenter editUserInfoPresenter;
    private String phonenum;
    private EditText verCodeED;
    private EditText pwdED;
    private boolean cansSeePwd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findback_resetpwd);
        findbackTitleTv = (TextView) findViewById(R.id.findback_titletv);
        verCodeED = (EditText) findViewById(R.id.verification_code);
        pwdED = (EditText) findViewById(R.id.pwd_code_ed);
        findViewById(R.id.seepwd_bt).setOnClickListener(this);
        phonenum = getIntent().getStringExtra("phonenum");
        String str = String.format(mRes.getString(R.string.sms_arrive), phonenum);
        findbackTitleTv.setText(str);
        findbackTitleTv.setTextColor(mRes.getColor(R.color.app_title_color1));
    }

    @Override
    protected void setUpViews() {
        editUserInfoPresenter = new EditUserInfoPresenter();
        editUserInfoPresenter.attachView(this);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.login_findback_pwd);
        text.setTextColor(mRes.getColor(R.color.app_main_color1));
        findViewById(R.id.submit_btn).setOnClickListener(this);
        //        function.setTextResource(R.string.reset, R.color.tab_text_s_color, 16);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_btn:
                editUserInfoPresenter.resetPWD(phonenum,verCodeED.getText()+"",pwdED.getText()+"");
                break;
            case R.id.seepwd_bt:
                cansSeePwd = !cansSeePwd;
                if (cansSeePwd) {
                    pwdED.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    pwdED.setInputType(
                            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
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

    @Override
    public void showOnSuccess() {
        CommonUtils.showToast(this,"设置密码成功");
    }

    @Override
    public void showOnFailure(String errormsg) {
        CommonUtils.showToast(this,errormsg);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return null;
    }
}
