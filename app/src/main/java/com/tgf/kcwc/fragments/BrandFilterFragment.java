package com.tgf.kcwc.fragments;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.see.BuilderBrands;
import com.tgf.kcwc.see.WrapBrandLists;

/**
 * Author：Jenny
 * Date:2017/4/12 09:43
 * E-mail：fishloveqin@gmail.com
 */

public class BrandFilterFragment extends BaseFragment {
    @Override
    protected void updateData() {

    }

    private WrapBrandLists mWrapBrandsLists;
    private RelativeLayout mSetBrandLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_filter_brand;
    }

    @Override
    protected void initView() {
        mSetBrandLayout = findView(R.id.setBrandLayout);
        BuilderBrands builderBrands = new BuilderBrands(mContext, mSetBrandLayout);
        builderBrands.setSingle(true);
        builderBrands.setDisplayChecked(false);
        builderBrands.setCallback(mCallback);
        builderBrands.loadData(1,"1",0);
    }

    private BuilderBrands.Callback mCallback = new BuilderBrands.Callback() {
        @Override
        public void refresh(ViewGroup parent, View view, Brand item, int position) {

            if (BrandFilterFragment.super.mCallback != null) {
                BrandFilterFragment.super.mCallback.refresh(item);
            }
        }
    };
}
