package com.tgf.kcwc.me.myline;

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
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:2017/5/3
 * E-mail:fishloveqin@gmail.com
 * 我的路线主页
 */

public class MyLineMainActivity extends BaseActivity {

    private PagerSlidingTabStrip mTabs;
    private ViewPager            mPager;
    private PagerAdapter         mPagerAdapter = null;
    private String               type          = "";

    public String getType() {
        return type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_line);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        text.setText("我的路线");
        backEvent(back);
        function.setImageResource(R.drawable.create_edit);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonUtils.startNewActivity(mContext, CreateRideLineActivity.class);
            }
        });
    }

    @Override
    protected void setUpViews() {
        type = getIntent().getStringExtra(Constants.IntentParams.INTENT_TYPE);
        if (type == null) {
            type = "";
        }
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mTabs.setBackgroundColor(mRes.getColor(R.color.white));
        mPager = (ViewPager) findViewById(R.id.pager);
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        fragments.add(new RideFragment());
        fragments.add(new PlanFragment());
        fragments.add(new FavoriteFragment());
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments,
            getResources().getStringArray(R.array.my_lines_tabs));
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
        //设置Tab底部线的颜色
        mTabs.setUnderlineColor(getResources().getColor(R.color.btn_color1));

        // 设置Tab 指示器Indicator的高度,传入的是dp
        mTabs.setIndicatorHeight(2);
        // 设置Tab Indicator的颜色
        mTabs.setIndicatorColorResource(R.color.btn_color1);

        // 设置Tab标题文字的大小,传入的是dp
        mTabs.setTextSize(16);
        // 设置选中Tab文字的颜色
        mTabs.setSelectedTextColorResource(R.color.text_color10);
        //设置正常Tab文字的颜色
        mTabs.setTextColorResource(R.color.text_color15);

    }

}
