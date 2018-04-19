package com.tgf.kcwc.coupon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.driv.SelectCityActivity;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.Coupon;
import com.tgf.kcwc.mvp.model.CouponCategory;
import com.tgf.kcwc.mvp.model.CouponDistanceFilterModel;
import com.tgf.kcwc.mvp.model.CouponNearModel;
import com.tgf.kcwc.mvp.presenter.CouponNearPresenter;
import com.tgf.kcwc.mvp.view.CouponNearView;
import com.tgf.kcwc.see.dealer.DealerHomeActivity;
import com.tgf.kcwc.seek.SeekActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DistanceUtil;
import com.tgf.kcwc.util.FilterPopwinUtil;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.SysUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.DropDownBrandSpinner;
import com.tgf.kcwc.view.DropDownCatSpinner;
import com.tgf.kcwc.view.DropDownSingleSpinner;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Auther: Scott
 * Date: 2017/1/3 0003
 * E-mail:hekescott@qq.com
 */

public class NearActivity extends BaseActivity implements CouponNearView {

    private ListView nearVoucherRV;
    private LinearLayout mRankLayout;
    private LinearLayout mKilometerLayout;
    private LinearLayout mCategoyLayout;
    private LinearLayout filterLayout;
    private DropDownCatSpinner catDropDownSelector;
    private ArrayList<DataItem> mRankLists = new ArrayList<>();
    private DropDownSingleSpinner rankDropDownSelector;
    private DropDownSingleSpinner kiloDropDownSelector;
    private CouponNearPresenter couponNearPresenter;
    private DropDownBrandSpinner dropDownBrandSpinner;
    private ArrayList<DataItem> mCatLists = new ArrayList<>();
    private ArrayList<DataItem> kiloDataItems = new ArrayList<>();
    private ArrayList<CouponNearModel.Component> componentStoreList = new ArrayList<>();

    private View headView;
    private OutAdapter mOuteAdapter;
    private int distanceId, categoryId, brandId, order;

    private KPlayCarApp kPlayCarApp;
    private String lat;
    private String lon;
    private TextView headCityTv;
    private LinearLayout mBrandLayout;
    private TextView mFilterTitle;
    private final int REQUEST_CODE_SERCITY = 100;
    private VoucherMainActivity mainActivity;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        initRefreshLayout(mBGDelegate);
        kPlayCarApp = (KPlayCarApp) getApplication();
        lat = kPlayCarApp.getLattitude();
        lon = kPlayCarApp.getLongitude();
        nearVoucherRV = (ListView) findViewById(R.id.near_vouchers);
        headView = View.inflate(mContext, R.layout.coupon_near_head, null);
        nearVoucherRV.addHeaderView(headView);
        TextView addressTv = (TextView) headView.findViewById(R.id.head_addresstv);
        addressTv.setText(kPlayCarApp.getAddressInfo());
        headCityTv = (TextView) findViewById(R.id.filterTitle);
        headCityTv.setText(kPlayCarApp.locCityName);
        initFilter();
        findViewById(R.id.etSearch).setOnClickListener(this);
        mFilterTitle = (TextView) findViewById(R.id.filterTitle);
        mFilterTitle.setText(kPlayCarApp.locCityName);
        mFilterTitle.setTextColor(mRes.getColor(R.color.white));
        findViewById(R.id.filterLayout).setOnClickListener(this);
        couponNearPresenter = new CouponNearPresenter();
        couponNearPresenter.attachView(this);
        couponNearPresenter.getNearList(mPageIndex, lat, lon, distanceId, categoryId, brandId,
                order);
        couponNearPresenter.getDistanceOrder();
        couponNearPresenter.getRankOrder();
        couponNearPresenter.getRecomendCategory();
        //        couponNearPresenter.getAreaDatas("22");
    }

    private void initFilter() {
        mCategoyLayout = (LinearLayout) findViewById(R.id.nearact_categroy);
        mBrandLayout = (LinearLayout) findViewById(R.id.nearact_brand);
        mKilometerLayout = (LinearLayout) findViewById(R.id.nearact_kilometer);
        filterLayout = (LinearLayout) findViewById(R.id.nearact_filterLayout);
        mRankLayout = (LinearLayout) findViewById(R.id.nearact_rank);
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
                        isFilterRefresh =true;
                        couponNearPresenter.getNearList(mPageIndex, lat, lon, distanceId, categoryId,
                                brandId, order);
                    }
                }

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            (findViewById(R.id.near_root_lv)).setPadding(0, SysUtils.getStatusHeight(this), 0, 0);
        }
        findViewById(R.id.root_search_title).setBackgroundResource(R.color.style_bg1);
        findViewById(R.id.voucher_back).setOnClickListener(this);
        mainActivity = (VoucherMainActivity) getParent();
    }

    private void commonFilterTile(LinearLayout layout, String title) {
        TextView tv = (TextView) layout.findViewById(R.id.filterTitle);
        tv.setText(title);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
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
            case R.id.voucher_back:
                finish();
                break;
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
                    order = dataItem.id;
                    mPageIndex = 1;
                    isFilterRefresh =true;
                    couponNearPresenter.getNearList(mPageIndex, lat, lon, distanceId, categoryId,
                            brandId, order);
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
                    isFilterRefresh =true;
                    couponNearPresenter.getNearList(mPageIndex, lat, lon, distanceId, categoryId,
                            brandId, order);
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
                        isFilterRefresh = true;
                        couponNearPresenter.getNearList(mPageIndex, lat, lon, distanceId, categoryId,
                                brandId, order);
                    }
                    if (catDropDownSelector.getIsBrand()) {
//                        categoryId = 0;
                        mBrandLayout.setVisibility(View.VISIBLE);
                    } else {
                        brandId = 0;
                        mBrandLayout.setVisibility(View.GONE);
                    }

                }
            }
        });
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
        return mContext;
    }

    @Override
    protected void onDestroy() {
        couponNearPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showNearList(CouponNearModel couponNearModel) {
        if (couponNearModel.componentList == null || couponNearModel.componentList.size() == 0) {
            if(isLoadMore){
                CommonUtils.showToast(mContext, "没有数据了");
                isLoadMore = false;
            }else{
                if(mOuteAdapter!=null){
                    if( isFilterRefresh ){
                        componentStoreList.clear();
                        mOuteAdapter.notifyDataSetChanged();
                        isFilterRefresh =false;
                    }
                }
            }
            return;
        }
        isFilterRefresh =false;
        if (mOuteAdapter == null) {
            componentStoreList = couponNearModel.componentList;
            mOuteAdapter = new OutAdapter(mContext);
            nearVoucherRV.setAdapter(mOuteAdapter);
        } else {
            if (isLoadMore) {
                componentStoreList.addAll(couponNearModel.componentList);
                isLoadMore = false;
            } else {
                componentStoreList.clear();
                componentStoreList = couponNearModel.componentList;
            }
            mOuteAdapter.notifyDataSetChanged();
        }

    }

    private boolean isLoadMore = false;
    private boolean isFilterRefresh = false;
    private BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {
        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            mPageIndex = 0;
            componentStoreList.clear();
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
        couponNearPresenter.getNearList(mPageIndex, lat, lon, distanceId, categoryId, brandId,
                order);
    }

    private class OutAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public OutAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return componentStoreList.size();
        }

        @Override
        public Object getItem(int position) {
            return componentStoreList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final CouponNearModel.Component item = componentStoreList.get(position);
            OuterViewHolder holder;
            if (convertView == null) {
                holder = new OuterViewHolder();
                convertView = mInflater.inflate(R.layout.recyleitem_near_store, null);
                holder.footerTv = (TextView) convertView.findViewById(R.id.couponnear_left);
                holder.titleRl = convertView.findViewById(R.id.neartopLayout);
                holder.footerRl = convertView.findViewById(R.id.couponnear_leftrl);
                holder.nameTv = (TextView) convertView.findViewById(R.id.name);
                holder.nearAreaTv = (TextView) convertView.findViewById(R.id.near_area_tv);
                holder.nearDistanceTv = (TextView) convertView.findViewById(R.id.near_distance_tv);
                holder.onlinetv = (TextView) convertView.findViewById(R.id.nearitem_onlinetv);
                holder.orgLogo = (SimpleDraweeView) convertView.findViewById(R.id.img);
                convertView.setTag(holder);
            } else {
                holder = (OuterViewHolder) convertView.getTag();
            }
            holder.nameTv.setText(item.store.full_name);
            holder.nearAreaTv.setText(item.store.address);
            holder.nearDistanceTv.setText(DistanceUtil.getDistance(item.store.distance ));
            String url = URLUtil.builderImgUrl(item.store.logo, 270, 203);
            holder.orgLogo.setImageURI(Uri.parse(url));
            holder.footerRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.isExpand = true;
                    mOuteAdapter.notifyDataSetChanged();
                }
            });
            holder.titleRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toStorhome = new Intent(getContext(), DealerHomeActivity.class);
                    toStorhome.putExtra(Constants.IntentParams.ID,item.store.id+"");
                    toStorhome.putExtra(Constants.IntentParams.TITLE,item.store.full_name);
                    startActivity(toStorhome);
                }
            });
            holder.onlinetv.setText("附近("+item.store.onlineNum+"人)");
            int limmit = 2;
            holder.footerTv.setText("其他" + (item.couponList.size() - limmit) + "个代金券");
            if (item.isExpand) {
                holder.footerRl.setVisibility(View.GONE);
            } else {
                if (item.couponList.size() > limmit) {
                    holder.footerRl.setVisibility(View.VISIBLE);
                } else {
                    holder.footerRl.setVisibility(View.GONE);
                }
            }
            ListView innerlist = (ListView) convertView.findViewById(R.id.nearinnerlist);
            InnerAdapter myExpandAdapter = new InnerAdapter(mContext, item, innerlist, limmit);
            innerlist.setAdapter(myExpandAdapter);
            innerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent toIntent = new Intent(mContext, CouponDetailActivity.class);
                    toIntent.putExtra(Constants.IntentParams.ID, item.couponList.get(position).id);
                    startActivity(toIntent);
                }
            });
            ViewUtil.setListViewHeightBasedOnChildren(innerlist);
            return convertView;
        }

    }

    public static class OuterViewHolder {
        public TextView footerTv;
        public View titleRl;
        public TextView nameTv;
        public TextView nearAreaTv;
        public TextView nearDistanceTv;
        public View footerRl;
        public SimpleDraweeView orgLogo;
        public TextView onlinetv;
    }

    private class InnerAdapter extends BaseAdapter {
        protected List<Coupon> mInDatas;
        protected Context mInContext;
        protected LayoutInflater mInflater;
        protected ListView mListView;
        private CouponNearModel.Component mComponent;
        private int mlimmit = 1;
        int count;

        public InnerAdapter(Context context, CouponNearModel.Component component, ListView listView,
                            int limmit) {
            this.mInContext = context;
            this.mInflater = LayoutInflater.from(mInContext);
            this.mInDatas = component.couponList;
            this.mComponent = component;
            this.mListView = listView;
            this.mlimmit = limmit;
            count = mInDatas.size();
            //            footer = mInflater.inflate(R.layout.coupon_near_footer, null);
            //            mListView.addFooterView(footer);
            //            if (!isExpand) {
            //                count = ((count > 2) ? 2 : count);
            //            }
            //            if (count > mlimmit) {
            //                count = mlimmit;
            //                footer.setVisibility(View.VISIBLE);
            //            } else {
            //                footer.setVisibility(View.GONE);
            //            }
            //            footer.setOnClickListener(new View.OnClickListener() {
            //                @Override
            //                public void onClick(View v) {
            //                    mComponent.isExpand = true;
            //                    mOuteAdapter.notifyDataSetChanged();
            ////                    InnerAdapter.this.notifyDataSetChanged();
            //                }
            //            });
        }

        @Override
        public int getCount() {
            if (mComponent.isExpand) {
                count = mInDatas.size();
            } else {
                count = count > mlimmit ? mlimmit : count;
            }
            return count;
        }

        @Override
        public Coupon getItem(int position) {
            return mInDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            InnerViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.recyleitem_near_couponitem, null);
                holder = new InnerViewHolder();
                holder.nowPriceTv = (TextView) convertView
                        .findViewById(R.id.recyleitem_near_nowprice);
                holder.coupondescTv = (TextView) convertView.findViewById(R.id.coupon_desc);
                holder.oldPrice = (TextView) convertView
                        .findViewById(R.id.listviewitem_recomment_oldprice);
                holder.getnumTv = (TextView) convertView.findViewById(R.id.coupont_getnum);
                convertView.setTag(holder);
            } else {
                holder = (InnerViewHolder) convertView.getTag();
            }
            Coupon item = mInDatas.get(position);
            holder.coupondescTv.setText(item.title);
            boolean isFree = Double.parseDouble(item.price) == 0;
            if (isFree) {
                holder.nowPriceTv.setText("免费");
                holder.nowPriceTv.setTextColor(mRes.getColor(R.color.style_bg1));
            } else {
                holder.nowPriceTv.setText("￥" + item.price);
                holder.nowPriceTv.setTextColor(mRes.getColor(R.color.voucher_yellow));
            }
            SpannableString denominationtext = SpannableUtil
                    .getDelLineString("￥" + item.denomination);
            holder.oldPrice.setText(denominationtext);
            holder.getnumTv.setText("已领取" + item.total);
            return convertView;
        }
    }

    public static class InnerViewHolder {
        public TextView nowPriceTv;
        public TextView coupondescTv;
        public TextView oldPrice;
        public TextView getnumTv;
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
