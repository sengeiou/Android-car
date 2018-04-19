package com.tgf.kcwc.me.mybalance.resetpaypwd;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.callback.FragmentDataCallback;
import com.tgf.kcwc.me.mybalance.forgetpaypwd.ForgetPayPwdActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:2017/10/20
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 修改密码
 */

public class ResetPwdActivity extends BaseActivity {

    protected RelativeLayout content;
    private List<BaseFragment> mSubFragments = new ArrayList<BaseFragment>();

    @Override
    protected void setUpViews() {
        initView();
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                finish();
            }
        });
        text.setText("修改支付密码");
    }


    @Override
    public void finish() {
        for (BaseFragment fragment : mSubFragments) {
            fragment.backEvent();
        }
        super.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_reset_pwd);

    }


    /**
     * 根据index切换Fragment布局(已缓存Fragment， 调用show、hide方法显示、隐藏)
     *
     * @param index
     */
    private BaseFragment switchSubFragment(int index) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        BaseFragment fragment = mSubFragments.get(index);
        for (BaseFragment f : mSubFragments) {

            if (f == fragment) {

                fragmentManager.beginTransaction().show(fragment).commit();

            } else {
                fragmentManager.beginTransaction().hide(f).commit();
            }
        }
        return fragment;
    }

    private void addSubFragments() {

        mSubFragments.clear();

        ResetPwdStepFirstFragment resetPwdStepFirstFragment = new ResetPwdStepFirstFragment();
        resetPwdStepFirstFragment.setCallback(firstStepCallback);
        ValidateOldPwdFragment validateOldPwdFragment = new ValidateOldPwdFragment();
        mSubFragments.add(resetPwdStepFirstFragment);
        validateOldPwdFragment.setCallback(validatePwdCallback);
        mSubFragments.add(validateOldPwdFragment);
        ResetPwdStepSecondFragment resetPwdStepSecondFragment = new ResetPwdStepSecondFragment();
        resetPwdStepSecondFragment.setCallback(secondStepCallback);
        mSubFragments.add(resetPwdStepSecondFragment);
        ResetPwdStepThirdFragment resetPwdStepThirdFragment = new ResetPwdStepThirdFragment();
        mSubFragments.add(resetPwdStepThirdFragment);
        FragmentManager fragmentManager = getSupportFragmentManager();

        for (BaseFragment f : mSubFragments) {
            if (!f.isAdded()) {
                fragmentManager.beginTransaction().add(R.id.content, f).commit();
            }
        }
    }

    private FragmentDataCallback firstStepCallback = new FragmentDataCallback() {
        @Override
        public void refresh(Object object) {

            if (object instanceof View) {
                View view = (View) object;

                int id = view.getId();

                switch (id) {
                    case R.id.forgetBtn:
                        CommonUtils.startNewActivity(mContext, ForgetPayPwdActivity.class);
                        finish();
                        break;

                    case R.id.rememberBtn:
                        BaseFragment fragment = switchSubFragment(1);
                        ValidateOldPwdFragment validateOldPwdFragment = (ValidateOldPwdFragment) fragment;
                        fragment.openSoftKeyword(validateOldPwdFragment.password);

                }
            }
        }
    };

    private FragmentDataCallback validatePwdCallback = new FragmentDataCallback() {
        @Override
        public void refresh(Object object) {


            ResetPwdStepSecondFragment resetPwdStepSecondFragment = (ResetPwdStepSecondFragment) switchSubFragment(2);
            resetPwdStepSecondFragment.openSoftKeyword(resetPwdStepSecondFragment.passwordInputView);


        }
    };

    private FragmentDataCallback secondStepCallback = new FragmentDataCallback() {
        @Override
        public void refresh(Object object) {

            String pwd = object.toString();
            ResetPwdStepThirdFragment baseFragment = (ResetPwdStepThirdFragment) switchSubFragment(3);
            baseFragment.registerEvent(pwd);
            baseFragment.openSoftKeyword(baseFragment.passwordInputView);
        }
    };

    private void initView() {
        content = (RelativeLayout) findViewById(R.id.content);

        addSubFragments();
        switchSubFragment(0);
    }
}
