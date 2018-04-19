package com.tgf.kcwc.me.setting;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.mvp.presenter.FankuiPresenter;
import com.tgf.kcwc.mvp.view.FanKuiView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.FunctionView;

/**
 * Auther: Scott
 * Date: 2017/5/22 0022
 * E-mail:hekescott@qq.com
 */

public class FanKuiActivity extends BaseActivity implements FanKuiView {

    private FankuiPresenter mFankuiPresenter;
    private String token;
    private EditText mFankuiMsged;
    private EditText mFankuiPhoneed;
    @Override
    protected void setUpViews() {
        findViewById(R.id.fankui_submittv).setOnClickListener(this);
        mFankuiMsged = (EditText) findViewById(R.id.fankui_msged);
        mFankuiPhoneed = (EditText) findViewById(R.id.fankui_phoneed);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("用户反馈");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fankui);
        token = IOUtils.getToken(getContext());
        mFankuiPresenter = new FankuiPresenter();
        mFankuiPresenter.attachView(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fankui_submittv:
                if(TextUtil.isEmpty(mFankuiMsged.getText().toString())){
                    CommonUtils.showToast(getContext(),"请输入反馈内容");
                    return ;
                }
                if(TextUtil.isEmpty(mFankuiPhoneed.getText().toString())){
                    CommonUtils.showToast(getContext(),"请输入手机号码");
                    return ;
                }
                mFankuiPresenter.postFanKuiMsg(token,mFankuiPhoneed.getText().toString(),mFankuiMsged.getText().toString());

                break;
            default:
                break;
        }
    }

    public Context getContext() {
        return FanKuiActivity.this;
    }

    @Override
    public void showPostSuccess() {
        CommonUtils.showToast(getContext(), "谢谢反馈");
        finish();
    }

    @Override
    public void showPostFailed(String statusMessage) {
        CommonUtils.showToast(getContext(), "提交失败："+statusMessage);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
            showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }
}
