package com.tgf.kcwc.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.Tencent;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.MyPagerAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.driv.SelectCityActivity;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.finddiscount.LimitDiscountNewFragment;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.see.OwnerSubActivity;
import com.tgf.kcwc.see.SaleFragment;
import com.tgf.kcwc.see.SelectBrandFragment;
import com.tgf.kcwc.see.ShowFragment;
import com.tgf.kcwc.see.StoreFragment;
import com.tgf.kcwc.seek.SeekActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.share.SinaWBCallback;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FragmentPageSelectListener;
import com.tgf.kcwc.view.OpenShareWindow;
import com.tgf.kcwc.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:16/4/23 22:16
 * E-mail:fishloveqin@gmail.com
 */
public class TabSeeFragment extends BaseFragment {

    private PagerSlidingTabStrip mTabs;
    private KPlayCarApp mApp;

    public ViewPager getPager() {
        return mPager;
    }

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter = null;
    private ImageView mLinkPKListsImg;
    private TextView mFilterTitle;
    private TextView mPKNums;

    private ArrayList<CarBean> mDatas = new ArrayList<CarBean>();
    //    private ImageView            linkAllListsImg;
    private ImageView linkShareImg;
    private KPlayCarApp kPlayCarApp;
    private TextView mSeek;
    private TextView editText = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void updateData() {

        if (mTabs != null) {
            mTabs.setPageSelectListener(mListener);
            //            if (TextUtils.isEmpty(IOUtils.getToken(mContext))) {
            //                mTabs.setPageSelectListener(mListener);
            //            } else {
            //                mTabs.setPageSelectListener(null);
            //            }
        }
        if (kPlayCarApp.locCityName != null) {
            mFilterTitle.setText(kPlayCarApp.locCityName);
        }
    }

    private WbShareHandler mWbHandler;

    @Override
    public void onShare(int requestCode, int resultCode, Intent data, int type) {

        if (type == 1) {
            //加上这一句才能回调
            Tencent.onActivityResultData(requestCode, resultCode, data, mBaseUIListener);
        }

        if (type == 2 && mWbHandler != null) {
            mWbHandler.doResultIntent(data, new SinaWBCallback(mContext));
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_see;
    }

    private AMapLocationClient mLocationClient;
    private List<BaseFragment> fragments = new ArrayList<BaseFragment>();
    private OpenShareWindow openShareWindow;                          //分享弹框

    @Override
    protected void initView() {
        mApp = (KPlayCarApp) getActivity().getApplication();
        fragments.clear();
        kPlayCarApp = (KPlayCarApp) getContext().getApplicationContext();
        mTabs = findView(R.id.tabs);
        mPager = findView(R.id.pager);
        mLinkPKListsImg = findView(R.id.linkPKListsImg);
        linkShareImg = findView(R.id.linkShareListsImg);
        editText = findView(R.id.etSearch);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.startNewActivity(mContext, SeekActivity.class);
            }
        });
        openShareWindow = new OpenShareWindow(getActivity());
        final Activity activity = getActivity();
        linkShareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShareWindow.show(activity);
                if (fragments.size() != 0) {
                    ShowFragment showFragment = (ShowFragment) fragments.get(1);
                    DataItem dataItem = showFragment.mShowDataItem;
                    if (dataItem == null) {
                        CommonUtils.showToast(mContext, "数据正在加载中...");
                        return;
                    }

                    final String title = dataItem.title;
                    final String description = dataItem.content;
                    final String cover = dataItem.url;

                    openShareWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {
                            switch (position) {
                                case 0:
                                    OpenShareUtil.sendWXMoments(mContext, mApp.getMsgApi(), title,
                                            description);
                                    break;
                                case 1:
                                    OpenShareUtil.sendWX(mContext, mApp.getMsgApi(), title,
                                            description);
                                    break;

                                case 2:
                                    mWbHandler = OpenShareUtil.shareSina(activity, title,
                                            description);
                                    break;
                                case 3:
                                    ArrayList<String> urls = new ArrayList<String>();
                                    urls.add(cover);
                                    OpenShareUtil.shareToQzone(mApp.getTencent(), activity,
                                            mBaseUIListener, urls, title, description);
                                    break;
                                case 4:
                                    OpenShareUtil.shareToQQ(mApp.getTencent(), activity,
                                            mBaseUIListener, title, cover, description);
                                    break;

                            }
                        }
                    });
                }
            }
        });
        mPKNums = findView(R.id.pkNums);
        //mPKNums.setOnClickListener(this);
        mFilterTitle = findView(R.id.filterTitle);
        //mSeek = findView(R.id.seek);
        findView(R.id.filterLayout).setOnClickListener(this);
        mLinkPKListsImg.setOnClickListener(this);
        //mSeek.setOnClickListener(this);
        //mLinkPKListsImg.setOnClickListener(this);

//        CustomizationCarActivity customizationFragment = new CustomizationCarActivity();
//        fragments.add(customizationFragment);

        SelectBrandFragment brandFragment = new SelectBrandFragment();
        fragments.add(brandFragment);
        ShowFragment showFragment = new ShowFragment();
        fragments.add(showFragment);
        fragments.add(new StoreFragment());
        //        fragments.add(new FindDiscountFragment());
        fragments.add(new SaleFragment());
        fragments.add(new LimitDiscountNewFragment());
        mPagerAdapter = new MyPagerAdapter(getChildFragmentManager(), fragments,
                getResources().getStringArray(R.array.see_tabs));
        mPager.setAdapter(mPagerAdapter);
        mTabs.setViewPager(mPager);
        Integer index = KPlayCarApp.getValue(Constants.IntentParams.INDEX);
        Logger.d("KPlayCarApp  index =="+index);
        if (index != null) {
            mPager.setCurrentItem(index);
        }else {
            mPager.setCurrentItem(1);
        }

        setTabsValue();

        //        mLocationClient = LocationUtil.getGaodeLocationClient(getActivity());
        //        mLocationClient.setLocationListener(new AMapLocationListener() {
        //            @Override
        //            public void onLocationChanged(AMapLocation aMapLocation) {
        //                if (aMapLocation != null) {
        //
        //
        //                }
        //            }
        //        });

        //        mLocationClient.startLocation();
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
        mTabs.setIndicatorColorResource(R.color.tab_indicator_s_line);

        // 设置Tab标题文字的大小,传入的是dp
        mTabs.setTextSize(16);
        // 设置选中Tab文字的颜色
        mTabs.setSelectedTextColorResource(R.color.text_color11);
        //设置正常Tab文字的颜色
        mTabs.setTextColorResource(R.color.text_color11);

    }

    private FragmentPageSelectListener mListener = new FragmentPageSelectListener() {
        @Override
        public void onSelected(int position, float positionOffset, int positionOffsetPixels) {
            //            if (position == 2 && positionOffset == 0) {
            //                mLinkPKListsImg.setVisibility(View.VISIBLE);
            //                if (mDatas.size() > 0) {
            //                    mPKNums.setVisibility(View.VISIBLE);
            //                }
            //
            //                linkShareImg.setVisibility(View.GONE);
            //
            //            } else if (position == 1) {
            //                linkShareImg.setVisibility(View.VISIBLE);
            //                mLinkPKListsImg.setVisibility(View.GONE);
            //                mPKNums.setVisibility(View.INVISIBLE);
            //            }

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) editText
                    .getLayoutParams();
            if ((position == 1) && (int) positionOffset == 0) {
                linkShareImg.setVisibility(View.VISIBLE);
                mLinkPKListsImg.setVisibility(View.GONE);
                mPKNums.setVisibility(View.GONE);
                params.setMargins(BitmapUtils.dp2px(mContext, 10), 0,
                        BitmapUtils.dp2px(mContext, 50), 0);

            } else {
                params.setMargins(BitmapUtils.dp2px(mContext, 10), 0,
                        BitmapUtils.dp2px(mContext, 10), 0);
                linkShareImg.setVisibility(View.GONE);
                mLinkPKListsImg.setVisibility(View.GONE);
                mPKNums.setVisibility(View.GONE);
            }
            editText.setLayoutParams(params);
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.linkPKListsImg:
            case R.id.pkNums:

                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(Constants.IntentParams.DATA, mDatas);
                intent.setClass(mContext, OwnerSubActivity.class);
                startActivity(intent);
                break;
            case R.id.filterLayout:
                Intent intentCity = new Intent();
                intentCity.setClass(mContext, SelectCityActivity.class);
                startActivityForResult(intentCity, REQUEST_CODE_SERCITY);
                break;
            case R.id.seek:
                CommonUtils.startNewActivity(mContext, SeekActivity.class);
                break;

        }

    }

    private final int REQUEST_CODE_SERCITY = 100;

    @Override
    public void onDestroy() {
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
        if (openShareWindow != null) {
            openShareWindow.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SERCITY:
                    String cityName = data.getStringExtra(Constants.IntentParams.DATA);
                    mFilterTitle.setText(cityName + "");
                    String cityId = data.getStringExtra(Constants.IntentParams.DATA3);
                    if (mPager.getCurrentItem() == 2) {

                        fragments.get(2).updateInfoByCity(cityId);
                    } else if (mPager.getCurrentItem() == 4) {
                        fragments.get(4).updateInfoByCity(cityId);
                    } else if (mPager.getCurrentItem() == 3) {
                        fragments.get(3).updateInfoByCity(cityId);
                    }

                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Integer index = KPlayCarApp.getValue(Constants.IntentParams.INDEX);

        if (index != null) {
           if( index != mPager.getCurrentItem()){
               mPager.setCurrentItem(index);
               Logger.d("KPlayCarApp  index tabsee =="+index);
           }

           }



    }

    public void setCurrentItem(int index) {
        mPager.setCurrentItem(index);
    }

    @Override
    public void onPause() {
        KPlayCarApp.removeValue(Constants.IntentParams.INDEX);
        Logger.d("KPlayCarApp  index tabsee");
        super.onPause();
    }


}
