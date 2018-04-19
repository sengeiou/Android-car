package com.tgf.kcwc.me.dealerbalance.forgetpaypwd;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.callback.FragmentDataCallback;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:2017/10/18
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 忘记支付密码
 */

public class ForgetPayPwdActivity extends BaseActivity {

    private List<BaseFragment> mSubFragments = new ArrayList<BaseFragment>();

    @Override
    protected void setUpViews() {

        initView();
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("修改支付密码");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forget_pay_pwd);

    }


    public void initView() {


        addSubFragments();

        switchSubFragment(0);
    }

    private void addSubFragments() {

        mSubFragments.clear();

        CheckCodeFragment checkCodeFragment = new CheckCodeFragment();
        checkCodeFragment.setCallback(checkCodeCallback);
        ForgetStepSecondFragment forgetStepSecondFragment = new ForgetStepSecondFragment();
        forgetStepSecondFragment.setCallback(forgetFirstStepCallback);
        mSubFragments.add(checkCodeFragment);
        mSubFragments.add(forgetStepSecondFragment);
        ForgetStepThirdFragment forgetStepThirdFragment = new ForgetStepThirdFragment();
        mSubFragments.add(forgetStepThirdFragment);
        FragmentManager fragmentManager = getSupportFragmentManager();

        for (BaseFragment f : mSubFragments) {
            if (!f.isAdded()) {
                fragmentManager.beginTransaction().add(R.id.content, f).commit();
            }
        }
    }

    private FragmentDataCallback checkCodeCallback = new FragmentDataCallback() {
        @Override
        public void refresh(Object object) {

            ForgetStepSecondFragment forgetStepSecondFragment = (ForgetStepSecondFragment) switchSubFragment(1);
            forgetStepSecondFragment.openSoftKeyword(forgetStepSecondFragment.passwordInputView);
            KPlayCarApp.putValue(Constants.KeyParams.CHECK_CODE, object.toString());
        }


    };


    private FragmentDataCallback forgetFirstStepCallback = new FragmentDataCallback() {
        @Override
        public void refresh(Object object) {

            ForgetStepThirdFragment fragment = (ForgetStepThirdFragment) switchSubFragment(2);
            fragment.openSoftKeyword(fragment.passwordInputView);
            fragment.registerEvent(object);
        }


    };

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
}
