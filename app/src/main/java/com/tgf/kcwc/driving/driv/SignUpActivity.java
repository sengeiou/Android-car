package com.tgf.kcwc.driving.driv;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.AgreementModel;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.presenter.SignUpPresenter;
import com.tgf.kcwc.mvp.view.SingUpView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.link.Link;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/3.
 */

public class SignUpActivity extends BaseActivity implements SingUpView {

    private SignUpPresenter mSignUpPresenter;

    private TextView mAgreement;
    private TextView mConfirm;        //报名
    private CheckBox mCheckBox;       //是否同意协议

    private TextView name;            //昵称
    private EditText phone;           //电话
    private EditText number;          //人数
    private EditText mComment;        //理由

    private String ID;
    private String nickName = "";
    private String phoneString = "";

    private String type="";

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.myapply);
    }

    @Override
    protected void setUpViews() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ID = (String) getIntent().getSerializableExtra(Constants.IntentParams.ID);
        type = (String) getIntent().getSerializableExtra(Constants.IntentParams.ID2);
        Account account = IOUtils.getAccount(mContext);
        nickName = account.userInfo.nickName;
        phoneString = account.userInfo.tel;
        mAgreement = (TextView) findViewById(R.id.agreement_text);
        mConfirm = (TextView) findViewById(R.id.confirm);
        mComment = (EditText) findViewById(R.id.edit_comment);
        name = (TextView) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        number = (EditText) findViewById(R.id.number);
        mCheckBox = (CheckBox) findViewById(R.id.checkBox_license);

        mSignUpPresenter = new SignUpPresenter();
        mSignUpPresenter.attachView(this);
        ViewUtil.link("报名协议", mAgreement, new Link.OnClickListener() {
            @Override
            public void onClick(Object o, String clickedText) {
                Map<String, Serializable> args=new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID,type);
                CommonUtils.startNewActivity(mContext, args, SignUpAgreementActivity.class);
            }
        }, mRes.getColor(R.color.text_color6), true);

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Confirmt();
            }
        });
        name.setText(nickName);
        phone.setText(phoneString);
    }

    public void Confirmt() {
        if (judgeString(name)) {

        } else {
            CommonUtils.showToast(mContext, "请输入昵称");
            return;
        }

        if (judgeString(phone)) {

        } else {
            CommonUtils.showToast(mContext, "请输入电话");
            return;
        }

        if (judgeString(number)) {

        } else {
            CommonUtils.showToast(mContext, "请输入报名人数");
            return;
        }

        if (number.equals("0")) {
            CommonUtils.showToast(mContext, "请输入报名人数大于0");
            return;
        } else {

        }

        if (judgeString(mComment)) {

        } else {
            CommonUtils.showToast(mContext, "请输入报名理由");
            return;
        }

        if (mCheckBox.isChecked()) {

        } else {
            CommonUtils.showToast(mContext, "请查看活动协议并同意");
            return;
        }
        mSignUpPresenter.gainAppLsis(IOUtils.getToken(mContext), nickName, getEdtext(number), getEdtext(mComment), phoneString, ID);
        setLoadingIndicator(true);
    }

    public boolean judgeString(EditText editText) {
        String text = editText.getText().toString().trim();
        if (text != null && !text.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean judgeString(TextView textView) {
        String text = textView.getText().toString().trim();
        if (text != null && !text.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public String getEdtext(EditText editText) {
        String text = editText.getText().toString().trim();
        return text;
    }

    @Override
    public void dataListSucceed(BaseBean attentionBean) {
        setLoadingIndicator(false);
        CommonUtils.showToast(mContext, "报名成功");
        finish();
    }

    @Override
    public void dataSucceed(List<AgreementModel> attentionBean) {

    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSignUpPresenter != null) {
            mSignUpPresenter.detachView();
        }
    }
}
