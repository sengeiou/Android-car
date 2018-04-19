package com.tgf.kcwc.coupon;

import android.annotation.TargetApi;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tgf.kcwc.R;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.util.IOUtils;

/**
 * Auther: Scott
 * Date: 2017/1/3 0003
 * E-mail:hekescott@qq.com
 */
@SuppressWarnings("deprecation")
public class VoucherMainActivity extends TabActivity implements RadioGroup.OnCheckedChangeListener {


    private RadioGroup mTabRadioGroup;
    private RadioButton mainRecommend;
    private RadioButton mainNear;
    private RadioButton mainExclusive;
    private RadioButton mainExclusivemy;
    private TabHost mTabHost;
    private int currentIndex = 0;
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.main_recommend:
                mTabHost.setCurrentTab(0);
                currentIndex =0;
                break;
            case R.id.main_near:
                mTabHost.setCurrentTab(1);
                currentIndex =1;
                break;
            case R.id.main_exclusive:
                mTabHost.setCurrentTab(2);
                currentIndex =2;
                break;
            case R.id.main_exclusivemy:
                if(IOUtils.isLogin(getContext())){
                    mTabHost.setCurrentTab(3);
                }else {
                    onEventMainThread(currentIndex);
                    mTabHost.setCurrentTab(currentIndex);
                }
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintResource(R.color.app_layout_bg_color);
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        setStatusBar();


        setContentView(R.layout.activity_vouchermain);   // 获取按钮
        mTabRadioGroup = (RadioGroup) findViewById(R.id.tab_group);
        mainRecommend = (RadioButton) findViewById(R.id.main_recommend);
        mainNear = (RadioButton) findViewById(R.id.main_near);
        mainExclusive = (RadioButton) findViewById(R.id.main_exclusive);
        mainExclusivemy = (RadioButton) findViewById(R.id.main_exclusivemy);
        mTabHost = getTabHost();
        mTabHost.addTab(mTabHost.newTabSpec("tag1").setIndicator("0").setContent(new Intent(this, RecomentActivity.class)));
        // mTabhost.addTab(mTabhost.newTabSpec("tag2").setIndicator("0").setContent(new
        // Intent(this, TestActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("tag2").setIndicator("0").setContent(new Intent(this, NearActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("tag3").setIndicator("0").setContent(new Intent(this, CouponOlineActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("tag4").setIndicator("0").setContent(new Intent(this, MyCouponActivity.class)));


        mTabRadioGroup.setOnCheckedChangeListener(this);
       int tmpIndex = getIntent().getIntExtra(Constants.IntentParams.INDEX,-1);
        if(tmpIndex!=-1){
            onEventMainThread(tmpIndex);
        }else {
            mainRecommend.performClick();
        }
    }
    protected void setStatusBar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.style_bg7);
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void onEventMainThread(int event) {
        if (event < 0 || event > 3){
            return;
        }
        switch (event) {
            case 0:
                mainRecommend.performClick();
                break;
            case 1:
                mainNear.performClick();
                break;
            case 2:
                mainExclusive.performClick();
                break;
            case 3:
                mainExclusivemy.performClick();
                break;

        }
    }
    private Context getContext(){
       return  VoucherMainActivity.this;
    }
//    @TargetApi(19)
//    private void setTranslucentStatus(boolean on) {
//        Window win = getWindow();
//        WindowManager.LayoutParams winParams = win.getAttributes();
//        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        if (on) {
//            winParams.flags |= bits;
//        } else {
//            winParams.flags &= ~bits;
//        }
//        win.setAttributes(winParams);
//    }
}
