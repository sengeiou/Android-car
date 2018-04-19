package com.tgf.kcwc.coupon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.driv.SelectCityActivity;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.BasePageModel;
import com.tgf.kcwc.mvp.model.CouponCategory;
import com.tgf.kcwc.mvp.model.CouponDetailModel;
import com.tgf.kcwc.mvp.model.CouponDistanceFilterModel;
import com.tgf.kcwc.mvp.model.CouponNearModel;
import com.tgf.kcwc.mvp.model.ExclusiveCoupon;
import com.tgf.kcwc.mvp.model.MyCouponModel;
import com.tgf.kcwc.mvp.model.OnlineCoupon;
import com.tgf.kcwc.mvp.presenter.CouponNearPresenter;
import com.tgf.kcwc.mvp.presenter.CouponOnlinePresenter;
import com.tgf.kcwc.mvp.presenter.CouponPresenter;
import com.tgf.kcwc.mvp.view.CouponNearView;
import com.tgf.kcwc.mvp.view.CouponOnlineView;
import com.tgf.kcwc.mvp.view.CouponView;
import com.tgf.kcwc.seek.SeekActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.DistanceUtil;
import com.tgf.kcwc.util.FilterPopwinUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.SysUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.DropDownBrandSpinner;
import com.tgf.kcwc.view.DropDownCatSpinner;
import com.tgf.kcwc.view.DropDownSingleSpinner;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.countdown.CountdownView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Auther: Scott
 * Date: 2017/1/3 0003
 * E-mail:hekescott@qq.com
 */

public class CouponOlineActivity extends BaseActivity implements CouponOnlineView, CouponNearView {

    private ListView couponLv;
    private CouponOnlinePresenter mPresenter;
    private final int REQUEST_CODE_SERCITY = 100;
    //    private View layoutFilter;
    private TextView flterTitleTv;
    private int filterId = 0;
    private String token;
    private LinearLayout mExpireLayout;
    private DropDownSingleSpinner dropDownExpireSpinner;
    private View titleFilterLayout;
    private TextView mFilterTitle;
    private KPlayCarApp kPlayCarApp;
    private Integer categoryId=0, brandId, distanceId, orderId;
    private WrapAdapter<OnlineCoupon.OnlineCouponItem> onlineListAdapter;
    private VoucherMainActivity mainActivity;
    private View emptyBox;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        initRefreshLayout(mBGDelegate);
        kPlayCarApp = (KPlayCarApp) getApplication();
        findViewById(R.id.etSearch).setOnClickListener(this);
        findViewById(R.id.couonpOnline_laytoutTitle).setBackgroundResource(R.color.style_bg1);
        findViewById(R.id.voucher_back).setOnClickListener(this);
        mFilterTitle = (TextView) findViewById(R.id.filterTitle);
        mFilterTitle.setText(kPlayCarApp.locCityName);
        mFilterTitle.setTextColor(mRes.getColor(R.color.white));
        findViewById(R.id.filterLayout).setOnClickListener(this);
        couponLv = (ListView) findViewById(R.id.onlineCoupon_contentlv);
        couponLv.addFooterView(View.inflate(getContext(),R.layout.bottom_hint_layout,null));
        mPresenter = new CouponOnlinePresenter();
        couponNearPresenter = new CouponNearPresenter();
        couponNearPresenter.attachView(this);
        couponNearPresenter.getDistanceOrder();
        couponNearPresenter.getRankOrder();
        couponNearPresenter.getRecomendCategory();
        mPresenter.attachView(this);
        token = IOUtils.getToken(mContext);
        mPageSize = 10;
        mPresenter.loadOnlineCoupons(token, mPageIndex, mPageSize, categoryId, brandId, distanceId, orderId);
        initFilter();
    }
    private LinearLayout mRankLayout;
    private LinearLayout mKilometerLayout;
    private LinearLayout mCategoyLayout;
    private LinearLayout mBrandLayout;
    private LinearLayout filterLayout;
    private DropDownCatSpinner catDropDownSelector;
    private ArrayList<DataItem> mRankLists = new ArrayList<>();
    private DropDownSingleSpinner rankDropDownSelector;
    private DropDownSingleSpinner kiloDropDownSelector;
    private CouponNearPresenter couponNearPresenter;
    private DropDownBrandSpinner dropDownBrandSpinner;
    private ArrayList<DataItem> mCatLists = new ArrayList<>();
    private ArrayList<DataItem> kiloDataItems = new ArrayList<>();
    private void initFilter() {
        mCategoyLayout = (LinearLayout) findViewById(R.id.nearact_categroy);
        mBrandLayout = (LinearLayout) findViewById(R.id.nearact_brand);
        mKilometerLayout = (LinearLayout) findViewById(R.id.nearact_kilometer);
        filterLayout = (LinearLayout) findViewById(R.id.nearact_filterLayout);
        mRankLayout = (LinearLayout) findViewById(R.id.nearact_rank);
        emptyBox = findViewById(R.id.msgTv2);
        commonFilterTile(mCategoyLayout, "分类");
        commonFilterTile(mKilometerLayout, "附近");
        commonFilterTile(mRankLayout, "排序");
        commonFilterTile(mBrandLayout, "品牌");
        mCategoyLayout.setOnClickListener(this);
        mKilometerLayout.setOnClickListener(this);
        mRankLayout.setOnClickListener(this);
        mBrandLayout.setOnClickListener(this);
        dropDownBrandSpinner = new DropDownBrandSpinner(getContext(),1,"1",0);
        dropDownBrandSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                FilterPopwinUtil.commonFilterTitleColor(mRes, mBrandLayout,
//                        R.color.text_content_color);
                FilterPopwinUtil.commonFilterImage(mBrandLayout, R.drawable.fitler_drop_down_n);
                DataItem dataItem = dropDownBrandSpinner.getSelectDataItem();
                if (dataItem != null) {
                    FilterPopwinUtil.commonFilterTile(mBrandLayout, dataItem.name);
                    if ( dataItem.id != brandId) {
                        brandId = dataItem.id;
                        mPageIndex = 1;
                        isRefresh=true;
                        mPresenter.loadOnlineCoupons(token, mPageIndex, mPageSize, categoryId, brandId, distanceId, orderId);
//todo                        couponNearPresenter.getNearList(mPageIndex, lat, lon, distanceId, categoryId,
//                                brandId, order);
                    }
                }

            }
        });
    }

    private boolean isRefresh = false;
    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            mPageIndex = 1;
            isRefresh = true;
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            isRefresh = false;
            mPageIndex++;
            loadMore();
            return false;
        }
    };

    /**
     * 加载更多
     */
    private void loadMore() {
        mPresenter.loadOnlineCoupons(token, mPageIndex, mPageSize, categoryId, brandId, distanceId, orderId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_oline);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.exclusive_root_lv).setPadding(0,
                    SysUtils.getStatusHeight(this), 0, 0);
        }
        mainActivity = (VoucherMainActivity) getParent();
    }

    private void commonFilterTile(LinearLayout layout, String title) {
        TextView tv = (TextView) layout.findViewById(R.id.filterTitle);
        tv.setText(title);
    }
    private List<OnlineCoupon.OnlineCouponItem> mExclusiveCoupons = new ArrayList<>();

    @Override
    public void showSuccess(ArrayList<OnlineCoupon.OnlineCouponItem> onlineCouponList) {
        if (isRefresh) {
            mExclusiveCoupons.clear();
        }
        mExclusiveCoupons.addAll(onlineCouponList);
        if (onlineListAdapter == null) {
            initAdapter();
            couponLv.setAdapter(onlineListAdapter);
        } else {
            onlineListAdapter.notifyDataSetChanged();
        }
        if(mExclusiveCoupons.size()==0){
            emptyBox.setVisibility(View.VISIBLE);
            couponLv.setVisibility(View.GONE);
        }else {
            couponLv.setVisibility(View.VISIBLE);
            emptyBox.setVisibility(View.GONE);
        }

    }

    private void initAdapter() {
        onlineListAdapter = new WrapAdapter<OnlineCoupon.OnlineCouponItem>(mContext, R.layout.listitem_online_coupon, mExclusiveCoupons) {
            @Override
            public void convert(ViewHolder helper, OnlineCoupon.OnlineCouponItem couponModel) {
                TextView titelTv = helper.getView(R.id.listitem_recoment_coupontitle);
                titelTv.setText(couponModel.title);
                helper.setText(R.id.onlincoupon_distanceTv, DistanceUtil.getDistance(couponModel.distance) );
                helper.setText(R.id.dealer_name, couponModel.issueOrg);
                helper.getView(R.id.listviewitem_recomment_salenum);
                Double nowPrice = couponModel.price;
                TextView nowPriceTv = helper.getView(R.id.recyleitem_near_nowprice);
                if (nowPrice == 0) {
                    nowPriceTv.setText("免费");
                    nowPriceTv.setTextColor(mRes.getColor(R.color.style_bg1));
                    if (couponModel.total != 0) {
                        helper.setText(R.id.onlincoupon_getTv, "已领" + couponModel.total);
                    }
                } else {
                    nowPriceTv.setText("￥" + couponModel.price);
                    nowPriceTv.setTextColor(mRes.getColor(R.color.voucher_yellow));
                    if (couponModel.total != 0) {
                        helper.setText(R.id.onlincoupon_getTv, "已售" + couponModel.total);
                    }
                }
                SpannableString oldPrice = SpannableUtil.getDelLineString("￥" + couponModel.denomination);
                helper.setText(R.id.listviewitem_recomment_oldprice, oldPrice);
                String coverUrl = URLUtil.builderImgUrl(couponModel.cover, 270, 203);
                helper.setSimpleDraweeViewURL(R.id.exculsivecoupon_cover, coverUrl);
                if (couponModel.OnlinePersonlist != null && couponModel.OnlinePersonlist.size() > 0) {
                    OnlineCoupon.OnlinePerson dealer = couponModel.OnlinePersonlist.get(0);
                    helper.setText(R.id.couponOnline_personTv, dealer.nickname + "等" + couponModel.OnlinePersonlist.size() + "人在线");
                }
            }
        };
        couponLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OnlineCoupon.OnlineCouponItem couponModel = mExclusiveCoupons.get(position);
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, couponModel.id);
                CommonUtils.startNewActivity(mContext, args, CouponDetailActivity.class);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.filteritem_expired:
//                layoutFilter.setVisibility(View.GONE);
//                flterTitleTv.setText("失效");
//                filterId = 1;
//                mPresenter.getExCoupons(token, filterId + "");
//                break;
//            case R.id.filteritem_noexpire:
//                layoutFilter.setVisibility(View.GONE);
//                flterTitleTv.setText("有效");
//                filterId = 0;
//                mPresenter.getExCoupons(token, filterId + "");
//                break;
            case R.id.nearact_categroy:
//                FilterPopwinUtil.commonFilterTitleColor(mRes, mCategoyLayout, R.color.text_color1);
                FilterPopwinUtil.commonFilterImage(mCategoyLayout, R.drawable.filter_drop_down_r);
                if (catDropDownSelector != null)
                    catDropDownSelector.showAsDropDownBelwBtnView(filterLayout);
                break;
            case R.id.nearact_kilometer:
//                FilterPopwinUtil.commonFilterTitleColor(mRes, mKilometerLayout,
//                        R.color.text_color1);
                FilterPopwinUtil.commonFilterImage(mKilometerLayout, R.drawable.filter_drop_down_r);
                if (kiloDropDownSelector != null)
                    kiloDropDownSelector.showAsDropDownBelwBtnView(filterLayout);
                break;
            case R.id.nearact_rank:
//                FilterPopwinUtil.commonFilterTitleColor(mRes, mRankLayout, R.color.text_color1);
                FilterPopwinUtil.commonFilterImage(mRankLayout, R.drawable.filter_drop_down_r);
                if (rankDropDownSelector != null)
                    rankDropDownSelector.showAsDropDownBelwBtnView(filterLayout);
                break;
            case R.id.nearact_brand:
//                FilterPopwinUtil.commonFilterTitleColor(mRes, mBrandLayout, R.color.text_color1);
                FilterPopwinUtil.commonFilterImage(mBrandLayout, R.drawable.filter_drop_down_r);
                if (dropDownBrandSpinner != null)
                    dropDownBrandSpinner.showAsDropDownBelwBtnView(filterLayout);
                break;
            case R.id.exclusive_expirely:
                dropDownExpireSpinner.showAsDropDownBelwBtnView(titleFilterLayout);

                break;
            case R.id.voucher_back:
                finish();
                break;
//            case R.id.filterTitlelayout:
//                layoutFilter.setVisibility(View.VISIBLE);
//                break;
            case R.id.etSearch:
                CommonUtils.startNewActivity(getContext(), SeekActivity.class);
                break;
            case R.id.filterLayout:
                Intent intentCity = new Intent();
                intentCity.setClass(mContext, SelectCityActivity.class);
                startActivityForResult(intentCity, REQUEST_CODE_SERCITY);
                break;
            default:
                break;
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            showLoadingIndicator(active);
        } else {
            showLoadingIndicator(active);
            stopRefreshAll();
        }
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SERCITY:
                    mFilterTitle.setText(data.getStringExtra(Constants.IntentParams.DATA));
                    break;

            }
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }


    @Override
    public void showNearList(CouponNearModel couponNearModel) {

    }

    @Override
    public void showRankFilter(ArrayList<DataItem> rankFilterlist) {
        mRankLists = rankFilterlist;
        rankDropDownSelector = new DropDownSingleSpinner(this, mRankLists);
        rankDropDownSelector.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                FilterPopwinUtil.commonFilterTitleColor(mRes, mRankLayout,
//                        R.color.text_content_color);
                FilterPopwinUtil.commonFilterImage(mRankLayout, R.drawable.fitler_drop_down_n);
                DataItem dataItem = rankDropDownSelector.getSelectDataItem();
                if (dataItem != null) {

                    FilterPopwinUtil.commonFilterTile(mRankLayout, dataItem.name);
                    orderId = dataItem.id;
                    mPageIndex = 1;
                    isRefresh =true;
                    mPresenter.loadOnlineCoupons(token, mPageIndex, mPageSize, categoryId, brandId, distanceId, orderId);
//                    couponNearPresenter.getNearList(mPageIndex, lat, lon, distanceId, categoryId,
//                            brandId, order);
                }
            }
        });
    }

    @Override
    public void showDistanceOrderFilter(CouponDistanceFilterModel couponDistanceFilterModel) {
        kiloDataItems = couponDistanceFilterModel.nearby;
        kiloDropDownSelector = new DropDownSingleSpinner(this, kiloDataItems);
        kiloDropDownSelector.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                FilterPopwinUtil.commonFilterTitleColor(mRes, mKilometerLayout,
//                        R.color.text_content_color);
                FilterPopwinUtil.commonFilterImage(mKilometerLayout, R.drawable.fitler_drop_down_n);
                DataItem dataItem = kiloDropDownSelector.getSelectDataItem();
                if (dataItem != null) {
                    FilterPopwinUtil.commonFilterTile(mKilometerLayout, dataItem.name);
                    distanceId = dataItem.id;
                    mPageIndex = 1;
                    isRefresh =true;
                    mPresenter.loadOnlineCoupons(token, mPageIndex, mPageSize, categoryId, brandId, distanceId, orderId);
                }
            }
        });

    }

    @Override
    public void showCategorylist(ArrayList<CouponCategory> categoryList) {
        for (int i = 0; i < categoryList.size(); i++) {
            CouponCategory cat = categoryList.get(i);
            DataItem item = new DataItem();
            item.name = cat.name;
            item.id = cat.id;
            mCatLists.add(item);
        }
        DataItem item = new DataItem();
        item.name = "全部";
        item.id = 0;
        mCatLists.add(0,item);
        catDropDownSelector = new DropDownCatSpinner(this, mCatLists);
        catDropDownSelector.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                FilterPopwinUtil.commonFilterTitleColor(mRes, mCategoyLayout,
//                        R.color.text_content_color);
                FilterPopwinUtil.commonFilterImage(mCategoyLayout, R.drawable.fitler_drop_down_n);
                DataItem catSelectItem = catDropDownSelector.getSelectDataItem();
                if (catSelectItem != null) {

                    if(categoryId!=catSelectItem.id){
                        categoryId = catSelectItem.id;
                        FilterPopwinUtil.commonFilterTile(mCategoyLayout, catSelectItem.name);
                        mPageIndex =1;
                        isRefresh =true;
                        mPresenter.loadOnlineCoupons(token, mPageIndex, mPageSize, categoryId, brandId, distanceId, orderId);
                    }
                    if (catDropDownSelector.getIsBrand()) {
                        categoryId = 0;
                        mBrandLayout.setVisibility(View.VISIBLE);
                    } else {
                        brandId = 0;
                        mBrandLayout.setVisibility(View.GONE);
                    }

                }
            }
        });
    }

}
