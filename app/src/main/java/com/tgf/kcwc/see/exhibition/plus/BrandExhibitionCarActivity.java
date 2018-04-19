package com.tgf.kcwc.see.exhibition.plus;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.MyPagerAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * @anthor noti
 * @time 2017/10/11
 * @describle 品牌展会展车
 */
public class BrandExhibitionCarActivity extends BaseActivity {
    ImageView back;
    TextView secondBrandNameTv;
    //品牌相关
    SimpleDraweeView brandImgSdv;
    TextView brandNameTv;
    TextView hallNameTv;
    TextView brandNumTv;

    PagerSlidingTabStrip pagerTab;
    ViewPager pager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_exhibition_car);
    }

    @Override
    protected void setUpViews() {
        back = (ImageView) findViewById(R.id.back);
        secondBrandNameTv = (TextView) findViewById(R.id.secondBrandNameTv);
        brandImgSdv = (SimpleDraweeView) findViewById(R.id.brandImgSdv);
        brandNameTv = (TextView) findViewById(R.id.brandNameTv);
        hallNameTv = (TextView) findViewById(R.id.hallNameTv);
        brandNumTv = (TextView) findViewById(R.id.brandNumTv);
        pagerTab = (PagerSlidingTabStrip) findViewById(R.id.pagerTab);
        pager = (ViewPager) findViewById(R.id.pager);

        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        fragments.add(new NewCarFragment());
        fragments.add(new SeeModelFragment());
        fragments.add(new ExhibitionCarFragment());
        fragments.add(new OwnerSaleFragment());
        MyPagerAdapter mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments,
                getResources().getStringArray(R.array.brand_car_tabs));
        pager.setAdapter(mPagerAdapter);
        pagerTab.setViewPager(pager);
        pager.setCurrentItem(0);
        setTabsValue();
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        pagerTab.setShouldExpand(false);

        // 设置Tab的分割线的颜色
        // mTabs.setDividerColor(getResources().getColor(R.color.color_80cbc4));
        // 设置分割线的上下的间距,传入的是dp
        pagerTab.setDividerPaddingTopBottom(12);
        //左右间距
        pagerTab.setTabPaddingLeftRight(16);
        // 设置Tab底部线的高度,传入的是dp
        pagerTab.setUnderlineHeight(1);
        //设置Tab底部线的颜色
        pagerTab.setUnderlineColor(getResources().getColor(R.color.split_line_color));

        // 设置Tab 指示器Indicator的高度,传入的是dp
        pagerTab.setIndicatorHeight(2);
        // 设置Tab Indicator的颜色
        pagerTab.setIndicatorColorResource(R.color.customer_detail_bottom);
        // 设置Tab标题文字的大小,传入的是dp
        pagerTab.setTextSize(12);
        // 设置选中Tab文字的颜色
        pagerTab.setSelectedTextColorResource(R.color.customer_detail_bottom);
        //设置正常Tab文字的颜色
        pagerTab.setTextColorResource(R.color.text_color15);
    }
}
