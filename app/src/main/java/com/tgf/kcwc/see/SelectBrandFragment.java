package com.tgf.kcwc.see;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.model.HotTag;
import com.tgf.kcwc.mvp.presenter.SearchResultPresenter;
import com.tgf.kcwc.mvp.view.SearchResultView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Author:Jenny
 * Date:2017/8/2
 * E-mail:fishloveqin@gmail.com
 * 品牌选车
 */

public class SelectBrandFragment extends BaseFragment {

    protected GridView hotGridView;
    protected RelativeLayout hotSearchLayout;
    private SearchResultPresenter mSearchResultPresenter;
    private RelativeLayout mBrandRootLayout;
    private WrapAdapter<HotTag> mHotTagAdapter = null;

    private List<HotTag> mHotsDatas = new ArrayList<HotTag>();
    HotTag moreHotTag = null;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.select_brand_fragment;
    }

    @Override
    protected void initView() {

        mSearchResultPresenter = new SearchResultPresenter();
        mSearchResultPresenter.attachView(mSearchResultView);
        String token = IOUtils.getToken(mContext);
        mSearchResultPresenter.getHotsDatas(token);
        hotGridView = findView(R.id.hotGridView);
        hotSearchLayout = findView(R.id.hotSearchLayout);
        mBrandRootLayout = findView(R.id.brandRootLayout);
        moreHotTag = new HotTag();
        moreHotTag.text = "更多条件";
        moreHotTag.type = "more";
        mHotTagAdapter = new WrapAdapter<HotTag>(mContext, mHotsDatas, R.layout.hot_tag_item) {
            @Override
            public void convert(ViewHolder helper, HotTag item) {

                String type = item.type;

                TextView titleText = helper.getView(R.id.title);
                titleText.setText(item.text);
                if ("more".equals(type)) {
                    titleText.setTextColor(mRes.getColor(R.color.text_color6));
                } else {
                    titleText.setTextColor(mRes.getColor(R.color.text_state_color));
                }
            }
        };
        hotGridView.setAdapter(mHotTagAdapter);
        hotGridView.setOnItemClickListener(mHotTagItemClickListener);

        BuilderBrands builderBrands = new BuilderBrands(mContext, mBrandRootLayout);
        builderBrands.setSingle(true);
        builderBrands.setDisplayChecked(false);
        builderBrands.setCallback(mCallback);
        builderBrands.loadData(1, "0", 0);
    }


    private BuilderBrands.Callback mCallback = new BuilderBrands.Callback() {
        @Override
        public void refresh(ViewGroup parent, View view, Brand item, int position) {

            HashMap<String, Serializable> args = new HashMap<String, Serializable>();
            args.put(Constants.IntentParams.ID, item.brandId + "");
            CommonUtils.startNewActivity(mContext, args, SeriesByBrandActivity.class);

        }
    };

    private AdapterView.OnItemClickListener mHotTagItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            HotTag hotTag = (HotTag) parent.getAdapter().getItem(position);

            Intent intent = new Intent();
            intent.setClass(mContext, CustomizationCarActivity.class);
            intent.putExtra(Constants.IntentParams.DATA, hotTag);
            startActivity(intent);

        }
    };

    private SearchResultView<List<HotTag>> mSearchResultView = new SearchResultView<List<HotTag>>() {
        @Override
        public void showDatas(List<HotTag> hotTag) {

            mHotsDatas.clear();
            mHotsDatas.addAll(hotTag);
            mHotsDatas.add(moreHotTag);
            mHotTagAdapter.notifyDataSetChanged();
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
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser) {

            isLoading = true;
        } else {
            isLoading = false;
        }

    }
}
