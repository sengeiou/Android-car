package com.tgf.kcwc.coupon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.driv.SelectCityActivity;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.mvp.model.CouponCategory;
import com.tgf.kcwc.mvp.model.CouponDistanceFilterModel;
import com.tgf.kcwc.mvp.presenter.CouponCategoryListPresenter;
import com.tgf.kcwc.mvp.view.CouponCategoryListView;
import com.tgf.kcwc.seek.SeekActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.FilterPopwinUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.DropDownBrandSpinner;
import com.tgf.kcwc.view.DropDownCatSpinner;
import com.tgf.kcwc.view.DropDownKiloSpinner;
import com.tgf.kcwc.view.DropDownSingleSpinner;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Auther: Scott
 * Date: 2017/1/9 0009
 * E-mail:hekescott@qq.com
 */

public class CouponListActivity extends BaseActivity implements CouponCategoryListView {
    //    private List<Coupon>            voucherList      = new ArrayList<>();
    private final int REQUEST_CODE_SERCITY = 100;
    private RecyclerView couponListRV;
    private MultiTypeAdapter couponTitleAdapter;
    private Items itemsCouponTitle = new Items();
    private ArrayList<DataItem> mRankLists = new ArrayList<>();
    private ArrayList<DataItem> mCatLists = new ArrayList<>();
    private LinearLayout mKilometerLayout;
    private LinearLayout mCategoyLayout;
    private LinearLayout filterLayout;
    private LinearLayout mRankLayout;
    private DropDownSingleSpinner rankDropDownSelector;
    private DropDownBrandSpinner dropDownBrandSpinner;
    private DropDownCatSpinner catDropDownSelector;
    private DropDownKiloSpinner kiloDropDownSelector;
    private CouponCategoryListPresenter couponCategoryListPresenter;
    private ArrayList<DataItem> kiloDataItems = new ArrayList<DataItem>();
    private List<DataItem> areaData = new ArrayList<>();
    private int categoryId, brandId, cityId, distanceId, orderId;
    private String lat, lon;
    private KPlayCarApp kPlayCarApp;
    private TextView mFilterTitle;
    private LinearLayout mBrandLayout;
    private Intent fromIntent;
    private String categoyName="分类";
    private int categoyPos=1;
    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        initRefreshLayout(mBGDelegate);
        lat = kPlayCarApp.getLattitude();
        lon = kPlayCarApp.getLongitude();
        fromIntent = getIntent();
        categoryId = fromIntent.getIntExtra(Constants.IntentParams.ID,1);
        categoyName =  fromIntent.getStringExtra(Constants.IntentParams.NAME);
        categoyPos =  fromIntent.getIntExtra(Constants.IntentParams.INDEX,0);

        initFilter();
        findViewById(R.id.root_search_title).setBackgroundResource(R.color.style_bg1);
        couponCategoryListPresenter = new CouponCategoryListPresenter();
        couponCategoryListPresenter.attachView(this);

        couponCategoryListPresenter.getCouponlist(categoryId, brandId, cityId, distanceId, orderId,
                lat, lon, mPageIndex);
        couponCategoryListPresenter.getRankOrder();
        couponCategoryListPresenter.getDistanceOrder();
        couponCategoryListPresenter.getRecomendCategory();
//        couponCategoryListPresenter.getAreaDatas(kPlayCarApp.cityId+"");
        couponCategoryListPresenter.getAreaDatas("22");
    }

    private void initFilter() {
        couponListRV = (RecyclerView) findViewById(R.id.listact_coupons);
        filterLayout = (LinearLayout) findViewById(R.id.listact_filterLayout);
        mCategoyLayout = (LinearLayout) findViewById(R.id.listact_categroy);
        mBrandLayout = (LinearLayout) findViewById(R.id.listact_brand);
        mKilometerLayout = (LinearLayout) findViewById(R.id.listact_kilometer);
        mRankLayout = (LinearLayout) findViewById(R.id.listact_rank);
        mCategoyLayout.setOnClickListener(this);
        mKilometerLayout.setOnClickListener(this);
        mRankLayout.setOnClickListener(this);
        mBrandLayout.setOnClickListener(this);

        FilterPopwinUtil.commonFilterTile(mCategoyLayout, categoyName);
        FilterPopwinUtil.commonFilterTile(mBrandLayout, "品牌");
        FilterPopwinUtil.commonFilterTile(mKilometerLayout, "附近");
        FilterPopwinUtil.commonFilterTile(mRankLayout, "排序");
        dropDownBrandSpinner = new DropDownBrandSpinner(getContext(),1,"1",0);
        dropDownBrandSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                FilterPopwinUtil.commonFilterTitleColor(mRes, mBrandLayout,
                        R.color.text_color3);
                FilterPopwinUtil.commonFilterImage(mBrandLayout, R.drawable.fitler_drop_down_n);
                DataItem dataItem = dropDownBrandSpinner.getSelectDataItem();
                if (dataItem != null) {
                    FilterPopwinUtil.commonFilterTile(mBrandLayout, dataItem.name);
                    if ( dataItem.id != brandId) {
                        brandId = dataItem.id;
                        mPageIndex =1;
                        isFileterRefresh =true;
                        couponCategoryListPresenter.getCouponlist(categoryId, brandId, cityId,
                                distanceId, orderId, lat, lon, mPageIndex);
                    }
                }

            }
        });
        if(!TextUtil.isEmpty(categoyName)){
            if(categoyName.equals("买车")){
                mBrandLayout.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        kPlayCarApp = (KPlayCarApp) getApplication();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couponlist);
        findViewById(R.id.voucher_back).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        findViewById(R.id.etSearch).setOnClickListener(this);
        mFilterTitle = (TextView) findViewById(R.id.filterTitle);
        mFilterTitle.setText(kPlayCarApp.locCityName);
        mFilterTitle.setTextColor(mRes.getColor(R.color.white));
        findViewById(R.id.filterLayout).setOnClickListener(this);
        couponTitleAdapter = new MultiTypeAdapter(itemsCouponTitle);
        couponTitleAdapter.register(Coupon.class, new CouponTitleItemProvider(getContext()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        couponListRV.setLayoutManager(linearLayoutManager);
        couponListRV.setAdapter(couponTitleAdapter);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.listact_kilometer:
                FilterPopwinUtil.commonFilterTitleColor(mRes, mKilometerLayout,
                        R.color.text_color3);
                FilterPopwinUtil.commonFilterImage(mKilometerLayout, R.drawable.filter_drop_down_r);
                if (kiloDropDownSelector == null) {
                    if (areaData.size() != 0)
                        kiloDataItems.addAll(areaData);
                    kiloDropDownSelector = new DropDownKiloSpinner(this, kiloDataItems);
                    kiloDropDownSelector.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            FilterPopwinUtil.commonFilterTitleColor(mRes, mKilometerLayout,
                                    R.color.text_color3);
                            FilterPopwinUtil.commonFilterImage(mKilometerLayout,
                                    R.drawable.fitler_drop_down_n);
                            DataItem dataItem = kiloDropDownSelector.getSelectDataItem();
                            if (dataItem != null) {
                                FilterPopwinUtil.commonFilterTile(mKilometerLayout, dataItem.name);
                                if (kiloDropDownSelector.getIsdistance()) {
                                    cityId = 0;
                                    distanceId = dataItem.id;
                                } else {
                                    distanceId = 0;
                                    cityId = dataItem.id;
                                }
                                mPageIndex =1;
                                isFileterRefresh =true;
                                couponCategoryListPresenter.getCouponlist(categoryId, brandId,
                                        cityId, distanceId, orderId, lat, lon, mPageIndex);
                            }
                        }
                    });
                }
                kiloDropDownSelector.showAsDropDownBelwBtnView(filterLayout);
                break;
            case R.id.listact_rank:
                FilterPopwinUtil.commonFilterTitleColor(mRes, mRankLayout, R.color.text_color3);
                FilterPopwinUtil.commonFilterImage(mRankLayout, R.drawable.filter_drop_down_r);
                if(rankDropDownSelector!=null)
                rankDropDownSelector.showAsDropDownBelwBtnView(filterLayout);
                break;
            case R.id.listact_brand:
                FilterPopwinUtil.commonFilterTitleColor(mRes, mBrandLayout, R.color.text_color3);
                FilterPopwinUtil.commonFilterImage(mBrandLayout, R.drawable.filter_drop_down_r);
                if(dropDownBrandSpinner!=null)
                dropDownBrandSpinner.showAsDropDownBelwBtnView(filterLayout);
                break;
            case R.id.listact_categroy:
                FilterPopwinUtil.commonFilterTitleColor(mRes, mCategoyLayout, R.color.text_color3);
                FilterPopwinUtil.commonFilterImage(mCategoyLayout, R.drawable.filter_drop_down_r);
                if(catDropDownSelector!=null)
                catDropDownSelector.showAsDropDownBelwBtnView(filterLayout);
                break;
            case R.id.filterLayout:
                Intent intentCity = new Intent();
                intentCity.setClass(mContext, SelectCityActivity.class);
                startActivityForResult(intentCity, REQUEST_CODE_SERCITY);
                break;
            case R.id.etSearch:
                CommonUtils.startNewActivity(getContext(), SeekActivity.class);
                break;

            default:
                break;
        }

    }

    @Override
    public void showCouponList(List<Coupon> couponlist) {
        Logger.d(couponlist);


            if (couponlist == null||couponlist.size()==0) {
                CommonUtils.showToast(mContext, "没有数据了");
                if(isFileterRefresh){
                    itemsCouponTitle.clear();
                    couponTitleAdapter.notifyDataSetChanged();
                    Logger.d("isFileterRefresh=="+isFileterRefresh);
                    isFileterRefresh = false;
                    Logger.d("isFileterRefresh2=="+isFileterRefresh);
                }
                isLoadMore = false;
                return;
            }
        isFileterRefresh =false;
        if (isLoadMore) {
            isLoadMore = false;
        } else {
            itemsCouponTitle.clear();
        }
        for (Coupon coupon : couponlist) {
            itemsCouponTitle.add(coupon);
        }
        couponTitleAdapter.notifyDataSetChanged();
    }

    @Override
    public void showRankFilter(ArrayList<DataItem> rankFilterlist) {
        if (rankFilterlist != null && rankFilterlist.size() != 0) {
            mRankLists = rankFilterlist;
            rankDropDownSelector = new DropDownSingleSpinner(this, mRankLists);
            rankDropDownSelector.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    FilterPopwinUtil.commonFilterTitleColor(mRes, mRankLayout,
                            R.color.text_color3);
                    FilterPopwinUtil.commonFilterImage(mRankLayout, R.drawable.fitler_drop_down_n);
                    DataItem dataItem = rankDropDownSelector.getSelectDataItem();
                    if (dataItem != null) {
                        FilterPopwinUtil.commonFilterTile(mRankLayout, dataItem.name);
                        orderId = dataItem.id;
                        mPageIndex =1;
                        isFileterRefresh =true;
                        couponCategoryListPresenter.getCouponlist(categoryId, brandId, cityId,
                                distanceId, orderId, lat, lon, mPageIndex);
                    }
                }
            });
        }

    }

    @Override
    public void showDistanceOrderFilter(CouponDistanceFilterModel couponDistanceFilterModel) {
        if (couponDistanceFilterModel != null) {
            DataItem dataNearby = new DataItem();
            dataNearby.id = 0;
            dataNearby.name = "附近";
            dataNearby.subDataItem = couponDistanceFilterModel.nearby;
            DataItem dataditance = new DataItem();
            dataditance.id = 1;
            dataditance.name = "距离";
            dataditance.subDataItem = couponDistanceFilterModel.journey;
            kiloDataItems.add(dataNearby);
            kiloDataItems.add(dataditance);
        }

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

//        if(mCatLists!=null&&mCatLists.size()>0){}
//        if(categoryId== mCatLists.get(0).id){
//            categoryId = 0;
//            mBrandLayout.setVisibility(View.VISIBLE);
//        }else{
//            mBrandLayout.setVisibility(View.GONE);
//        }
        catDropDownSelector = new DropDownCatSpinner(this, mCatLists);
        catDropDownSelector.setDeafultSelectId(categoyPos);
        catDropDownSelector.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                FilterPopwinUtil.commonFilterTitleColor(mRes, mCategoyLayout, R.color.text_color3);
                FilterPopwinUtil.commonFilterImage(mCategoyLayout, R.drawable.fitler_drop_down_n);
                DataItem catSelectItem = catDropDownSelector.getSelectDataItem();
                if (catSelectItem != null) {

                    if(categoryId!=catSelectItem.id){
                        categoryId = catSelectItem.id;
                        mPageIndex =1;
                        FilterPopwinUtil.commonFilterTile(mCategoyLayout, catSelectItem.name);
                        isFileterRefresh =true;
                        couponCategoryListPresenter.getCouponlist(categoryId, brandId, cityId,
                                distanceId, orderId, lat, lon, mPageIndex);
                    }
                    if (catDropDownSelector.getIsBrand()) {
//                        categoryId = 0;
                        mBrandLayout.setVisibility(View.VISIBLE);
                    } else {
                        brandId = 0;
                        mBrandLayout.setVisibility(View.GONE);
                    }
                }
                //                int selectPOS = catDropDownSelector.getSelectPos();

            }
        });
    }

    @Override
    public void showAreaList(List<DataItem> areaList) {
        if (areaList != null) {
            areaData = areaList;

        }
    }

    private boolean isLoadMore = false;
    private boolean isFileterRefresh = false;
    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            mPageIndex = 0;
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            isLoadMore = true;
            loadMore();
            return false;
        }
    };

    /**
     * 加载更多
     */
    private void loadMore() {
        mPageIndex++;
        couponCategoryListPresenter.getCouponlist(categoryId, brandId, cityId, distanceId, orderId,
                lat, lon, mPageIndex);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            showLoadingDialog();
        } else {
            stopRefreshAll();
            dismissLoadingDialog();
        }
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        couponCategoryListPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SERCITY:
                    String cityName = data.getStringExtra(Constants.IntentParams.DATA);
                    kPlayCarApp.locCityName = cityName;
                    mFilterTitle.setText(data.getStringExtra(Constants.IntentParams.DATA));
                    break;
            }
        }
    }
}
