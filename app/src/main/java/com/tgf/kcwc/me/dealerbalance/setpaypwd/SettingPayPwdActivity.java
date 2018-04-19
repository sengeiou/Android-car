package com.tgf.kcwc.me.dealerbalance.setpaypwd;

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
 */

public class SettingPayPwdActivity extends BaseActivity {

    private List<BaseFragment> mSubFragments = new ArrayList<BaseFragment>();

    @Override
    protected void setUpViews() {

        initView();
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("设置支付密码");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting_pay_pwd);

    }


    public void initView() {


        addSubFragments();

        switchSubFragment(0);
    }

    private void addSubFragments() {

        mSubFragments.clear();

        CheckCodeFragment checkCodeFragment = new CheckCodeFragment();
        checkCodeFragment.setCallback(checkCodeCallback);
        SettingPwdStepFirstFragment settingPwdStepFirstFragment = new SettingPwdStepFirstFragment();
        settingPwdStepFirstFragment.setCallback(firstStepCallback);
        mSubFragments.add(checkCodeFragment);
        mSubFragments.add(settingPwdStepFirstFragment);
        SettingPwdStepSecondFragment settingPwdStepSecondFragment = new SettingPwdStepSecondFragment();
        mSubFragments.add(settingPwdStepSecondFragment);
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

            SettingPwdStepFirstFragment settingPwdStepFirstFragment = (SettingPwdStepFirstFragment) switchSubFragment(1);
            settingPwdStepFirstFragment.openSoftKeyword(settingPwdStepFirstFragment.passwordInputView);
            KPlayCarApp.putValue(Constants.KeyParams.CHECK_CODE, object.toString());
        }


    };


    private FragmentDataCallback firstStepCallback = new FragmentDataCallback() {
        @Override
        public void refresh(Object object) {

            SettingPwdStepSecondFragment fragment = (SettingPwdStepSecondFragment) switchSubFragment(2);
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
