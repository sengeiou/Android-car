package com.tgf.kcwc.see;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.MyPagerAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.SeriesByBrandModel;
import com.tgf.kcwc.mvp.presenter.SeriesListsPresenter;
import com.tgf.kcwc.mvp.view.SeriesDataView;
import com.tgf.kcwc.view.ExBackPagerSlidingTabStrip;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:2017/8/4
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 当前品牌下的所有车系
 */

public class SeriesByBrandActivity extends BaseActivity implements SeriesDataView<SeriesByBrandModel> {
    private ExBackPagerSlidingTabStrip mTabs;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter = null;

    private int mSenseId = -1;
    private Intent fromIntent;

    private SeriesListsPresenter mPresenter;

    private String mBrandId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTitleBar = false;
        mPresenter = new SeriesListsPresenter();
        mPresenter.attachView(this);
        setContentView(R.layout.activity_series_by_brand);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText(R.string.tickets);
    }

    @Override
    protected void setUpViews() {
        fromIntent = getIntent();
        int index = fromIntent.getIntExtra(Constants.IntentParams.INDEX, 1);
        mBrandId = fromIntent.getStringExtra(Constants.IntentParams.ID);
        mTabs = (ExBackPagerSlidingTabStrip) findViewById(R.id.tabs);
        mTabs.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                finish();
            }
        });
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setBackgroundColor(mRes.getColor(R.color.white));


        mPresenter.getSeriesDatas(mBrandId + "");
    }

    private void createTab(SeriesByBrandModel model) {

        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        String oriArray[] = getResources().getStringArray(R.array.series_by_brand_value);
        ArrayList<String> titles = new ArrayList<String>();
        if (model.listIn != null && model.listIn.size() > 0) {
            fragments.add(new BaseBrandsFragment(model, Constants.SeriesStatus.ON));
            titles.add(oriArray[0]);
        }
        if (model.listPre != null && model.listPre.size() > 0) {
            fragments.add(new BaseBrandsFragment(model, Constants.SeriesStatus.PRE));
            titles.add(oriArray[1]);
        }

        if (model.listStop != null && model.listStop.size() > 0) {
            fragments.add(new BaseBrandsFragment(model, Constants.SeriesStatus.STOP));
            titles.add(oriArray[2]);
        }
        if (fragments.size() > 0) {
            mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, titles
            );
            mPager.setAdapter(mPagerAdapter);
            mTabs.setViewPager(mPager);


            mPager.setCurrentItem(0);
            setTabsValue();
        }

    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        mTabs.setShouldExpand(true);

        // 设置Tab的分割线的颜色
        // mTabs.setDividerColor(getResources().getColor(R.color.color_80cbc4));
        // 设置分割线的上下的间距,传入的是dp
        mTabs.setDividerPaddingTopBottom(12);

        // 设置Tab底部线的高度,传入的是dp
        mTabs.setUnderlineHeight(0);
        //设置Tab底部线的颜色
        mTabs.setUnderlineColor(getResources().getColor(R.color.split_line_color));

        // 设置Tab 指示器Indicator的高度,传入的是dp
        mTabs.setIndicatorHeight(2);
        // 设置Tab Indicator的颜色
        mTabs.setIndicatorColorResource(R.color.white);

        // 设置Tab标题文字的大小,传入的是dp
        mTabs.setTextSize(14);
        // 设置选中Tab文字的颜色
        mTabs.setSelectedTextColorResource(R.color.white);
        //设置正常Tab文字的颜色
        mTabs.setTextColorResource(R.color.white);
    }

    @Override
    public void loadDatas(SeriesByBrandModel seriesByBrandModel) {

        createTab(seriesByBrandModel);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {
        dismissLoadingDialog();
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
