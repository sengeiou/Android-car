package com.tgf.kcwc.fragments;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.PriceViewBuilder;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.view.rangeseekbar.RangeBar;

/**
 * Author：Jenny
 * Date:2017/4/12 09:43
 * E-mail：fishloveqin@gmail.com
 */

public class PriceFilterFragment extends BaseFragment {
    protected View rootView;
    protected RelativeLayout mPriceLayout;

    @Override
    protected void updateData() {

    }

    private RangeBar mRangeBar;
    private Button mConfirmBtn;
    private PriceViewBuilder priceViewBuilder;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_filter_price;
    }

    @Override
    protected void initView() {
        mPriceLayout = findView(R.id.setPriceLayout);
        priceViewBuilder = new PriceViewBuilder(mContext, mPriceLayout, priceCallback);

    }


    private PriceViewBuilder.FilterPriceCallback priceCallback = new PriceViewBuilder.FilterPriceCallback() {
        @Override
        public void result(String customMin, String customMax) {

            loadFilterByPriceReuslt(customMin + "-" + customMax + "万", customMin, customMax, "");


        }

        @Override
        public void result(DataItem item) {

            loadFilterByPriceReuslt(item.title, "", "", item.key);
        }
    };


    private void loadFilterByPriceReuslt(String title, String min, String max, String key) {


        if (mCallback != null) {
            DataItem dataItem = new DataItem();
            dataItem.priceMinStr = min;
            dataItem.priceMaxStr = max;
            dataItem.key = key;
            dataItem.name=title;
            mCallback.refresh(dataItem);
        }
    }
}
