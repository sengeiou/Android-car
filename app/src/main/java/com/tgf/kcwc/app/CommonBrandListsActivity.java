package com.tgf.kcwc.app;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.see.WrapBrandLists;
import com.tgf.kcwc.view.FunctionView;

public class CommonBrandListsActivity extends BaseActivity {
    protected RelativeLayout mSetBrandLayout;
    TranslateAnimation       mShowAction, mHiddenAction;
    private WrapBrandLists   mWrapBrandsLists;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTitleBar=false;
        setContentView(R.layout.activity_common_brands);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        mSetBrandLayout = (RelativeLayout) findViewById(R.id.brandsLayout);
        mWrapBrandsLists = new WrapBrandLists();
        mWrapBrandsLists.setUpViews(mContext, mSetBrandLayout);
        mWrapBrandsLists.loadData();
        // mWrapBrandsLists.setCallback(mCallback);
    }

    private void showAnimation() {
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(500);
    }

}
