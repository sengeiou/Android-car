package com.tgf.kcwc.play.havefun;

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
import com.tgf.kcwc.seek.SeekActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.PopupMenuUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */

public class HaveFunActivity extends BaseActivity {
    private PagerSlidingTabStrip mTabs;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter = null;
    private int mSenseId = -1;
    private int number = 0;

    private View bottomLayout;

    @Override
    protected void setUpViews() {
        ImageButton viewById = (ImageButton) findViewById(R.id.title_bar_back);
        ImageButton function = (ImageButton) findViewById(R.id.title_function_btn);
        TextView text = (TextView) findViewById(R.id.title_bar_text);
        text.setText(R.string.havefun);
        backEvent(viewById);
        function.setImageResource(R.drawable.btn_search_white);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CommonUtils.startResultNewActivity(TopicActivity.this, null, TopicListActivity.class,Constants.InteractionCode.REQUEST_CODE);
                CommonUtils.startNewActivity(mContext, SeekActivity.class);
            }
        });
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
 /*       backEvent(back);
        text.setText(R.string.havefun);
        function.setImageResource(R.drawable.btn_search_white);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.startNewActivity(mContext, SeekActivity.class);
            }
        });*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isTitleBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_havefun);

        bottomLayout = findViewById(R.id.triplist_bottll);
        findViewById(R.id.home_indicatorBg).setOnClickListener(this);
        findViewById(R.id.see_indicatorBg).setOnClickListener(this);
        findViewById(R.id.posted_indicatorBg).setOnClickListener(this);
        findViewById(R.id.me_indicatorBg).setOnClickListener(this);

        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mPager = (ViewPager) findViewById(R.id.pager);
        mTabs.setBackgroundColor(mRes.getColor(R.color.white));
        mPager.setBackgroundColor(mRes.getColor(R.color.white));
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        NewestListFragment newestListFragment = new NewestListFragment();
        newestListFragment.setSenseId(mSenseId);
        fragments.add(newestListFragment);
        HotListFragment hotListFragment = new HotListFragment();
        hotListFragment.setSenseId(mSenseId);
        fragments.add(hotListFragment);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, getResources().getStringArray(R.array.play_havefun_tabs));
        mPager.setAdapter(mPagerAdapter);
        mTabs.setViewPager(mPager);
        mPager.setCurrentItem(0);
        setTabsValue();
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
        //设置Tab底部线的颜色split_line_color
        mTabs.setUnderlineColor(getResources().getColor(R.color.split_line_color));

        // 设置Tab 指示器Indicator的高度,传入的是dp
        mTabs.setIndicatorHeight(2);
        // 设置Tab Indicator的颜色
        mTabs.setIndicatorColorResource(R.color.tab_text_s_color);

        // 设置Tab标题文字的大小,传入的是dp
        mTabs.setTextSize(14);
        // 设置选中Tab文字的颜色
        mTabs.setSelectedTextColorResource(R.color.tab_text_s_color);
        //设置正常Tab文字的颜色
        mTabs.setTextColorResource(R.color.text_color);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.play_backiv:
                finish();
                break;
            case R.id.home_indicatorBg:
//                Intent intent = new Intent();
//                intent.putExtra(Constants.IntentParams.INDEX, 0);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.setClass(mContext, MainActivity.class);
//                startActivity(intent);
                CommonUtils.gotoMainPage(mContext,Constants.Navigation.HOME_INDEX);
                break;
            case R.id.see_indicatorBg:
//                Intent intentSee = new Intent();
//                intentSee.putExtra(Constants.IntentParams.INDEX, 1);
//                intentSee.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intentSee.setClass(mContext, MainActivity.class);
//                startActivity(intentSee);
                CommonUtils.gotoMainPage(mContext,Constants.Navigation.SEE_INDEX);
                break;
            case R.id.posted_indicatorBg:
                PopupMenuUtil.getInstance()._show(mContext, bottomLayout);
                break;
            case R.id.me_indicatorBg:
                if (IOUtils.isLogin(mContext)) {
//                    Intent intentMe = new Intent();
//                    intentMe.putExtra(Constants.IntentParams.INDEX, 4);
//                    intentMe.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intentMe.setClass(mContext, MainActivity.class);
//                    startActivity(intentMe);
                    CommonUtils.gotoMainPage(mContext,Constants.Navigation.ME_INDEX);
                }
                break;
        }

    }

}
