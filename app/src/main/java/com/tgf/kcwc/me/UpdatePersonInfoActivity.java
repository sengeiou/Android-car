package com.tgf.kcwc.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.presenter.UserDataPresenter;
import com.tgf.kcwc.mvp.view.UserDataView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;

/**
 * Author:Jenny
 * Date:16/5/11 10:23
 * E-mail:fishloveqin@gmail.com
 * 个人信息修改页面
 **/
public class UpdatePersonInfoActivity extends BaseActivity implements View.OnClickListener, UserDataView {

    protected TextView warningContent;
    protected RelativeLayout warningFrame;
    protected EditText nickET, signET;
    private String mTitle;

    private int mType;

    private String mData;

    private RelativeLayout nickLayout, signLayout;
    private TextView noteTv, errorMsgTv;

    private UserDataPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mTitle = intent.getStringExtra(Constants.IntentParams.TITLE);
        mType = intent.getIntExtra(Constants.IntentParams.INTENT_TYPE, -1);
        mData = intent.getStringExtra(Constants.IntentParams.DATA);
        setContentView(R.layout.update_person_info_layout);

    }

    @Override
    protected void setUpViews() {

        initView();
        mPresenter = new UserDataPresenter();
        mPresenter.attachView(this);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);

        text.setText(mTitle);

        function.setTextResource("保存", R.color.white, 14);

        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "";
                if (mType == 1) {
                    content = nickET.getText().toString();

                    if (content.length() > 8) {
                        errorMsgTv.setVisibility(View.VISIBLE);
                        errorMsgTv.setText("昵称输入最多8字！");
                        return;
                    } else if (content.length() == 0) {
                        errorMsgTv.setVisibility(View.VISIBLE);
                        errorMsgTv.setText("昵称不能为空哦！");
                        return;
                    } else {
                        errorMsgTv.setVisibility(View.GONE);
                        mPresenter.checkNicknameIsExist(content, IOUtils.getToken(mContext));
                    }
                } else {
                    content = signET.getText().toString();
                    setResult(content);
                }


            }
        });
    }

    private void setResult(String content) {

        Intent intent = getIntent();
        intent.putExtra(Constants.IntentParams.DATA, content);
        setResult(RESULT_OK, intent);
        finish();
    }


    private void initView() {
        nickLayout = (RelativeLayout) findViewById(R.id.nickLayout);
        signLayout = (RelativeLayout) findViewById(R.id.signLayout);
        warningContent = (TextView) findViewById(R.id.warning_content);
        warningFrame = (RelativeLayout) findViewById(R.id.warning_frame);
        nickET = (EditText) findViewById(R.id.editContent);
        nickET.setHintTextColor(mRes.getColor(R.color.text_color2));
        signET = (EditText) findViewById(R.id.signET);
        noteTv = (TextView) findViewById(R.id.noteTv);
        errorMsgTv = (TextView) findViewById(R.id.errorMsgTv);
        signET.addTextChangedListener(signWatcher);
        if (mType == 1) {

            nickLayout.setVisibility(View.VISIBLE);
            signLayout.setVisibility(View.GONE);

            nickET.setText(mData);
        } else if (mType == 2) {

            nickLayout.setVisibility(View.GONE);
            signLayout.setVisibility(View.VISIBLE);
            signET.setText(mData);
        }
    }


    private TextWatcher signWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            String str = s.toString();
            noteTv.setText((str.length() + "/30"));
        }
    };

    @Override
    public void showDatas(Object o) {


        setResult(nickET.getText().toString().trim());
    }

    @Override
    public void setLoadingIndicator(boolean active) {


    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
