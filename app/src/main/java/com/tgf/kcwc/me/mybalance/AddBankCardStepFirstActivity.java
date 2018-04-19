package com.tgf.kcwc.me.mybalance;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/10/16
 * E-mail:fishloveqin@gmail.com
 */

public class AddBankCardStepFirstActivity extends BaseActivity {

    protected Button nextStepBtn;
    protected TextView nameTv;
    protected EditText nameET;
    protected TextView cardTv;
    protected EditText cardET;

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("添加银行卡");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_add_bank_card_step_first);
        initView();
    }

    private void initView() {
        nextStepBtn = (Button) findViewById(R.id.nextStepBtn);
        nextStepBtn.setOnClickListener(this);
        nameTv = (TextView) findViewById(R.id.nameTv);
        nameET = (EditText) findViewById(R.id.nameET);
        cardTv = (TextView) findViewById(R.id.cardTv);
        cardET = (EditText) findViewById(R.id.cardET);
        nameET.addTextChangedListener(watcher1);
        cardET.addTextChangedListener(watcher2);
    }


    private TextWatcher watcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            checkNextStep(s.toString(), cardET.getText().toString());
        }
    };


    private TextWatcher watcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {


            checkNextStep(s.toString(), nameET.getText().toString());
        }
    };

    private void checkNextStep(String s, String s2) {

        if (TextUtil.isEmpty(s) || TextUtil.isEmpty(s2)) {

            nextStepBtn.setEnabled(false);
        } else {
            nextStepBtn.setEnabled(true);
        }

    }


    @Override
    public void onClick(View view) {

        String name = nameET.getText().toString();
        String card = cardET.getText().toString();

        Map<String, Serializable> args = new HashMap<String, Serializable>();

        args.put(Constants.IntentParams.DATA, name);
        args.put(Constants.IntentParams.DATA2, card);
        CommonUtils.startNewActivity(mContext, args,AddBankCardStepSecondActivity.class);

    }
}
