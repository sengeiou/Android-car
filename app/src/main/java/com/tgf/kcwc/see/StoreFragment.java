package com.tgf.kcwc.see;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.callback.FragmentDataCallback;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.fragments.BrandFilterFragment;
import com.tgf.kcwc.fragments.PriceFilterFragment;
import com.tgf.kcwc.fragments.SeatFilterFragment;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.presenter.DealerDataPresenter;
import com.tgf.kcwc.mvp.presenter.StoreCarListModel;
import com.tgf.kcwc.mvp.view.DealerDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.NumFormatUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tgf.kcwc.R.id.brand;

/**
 * Author：Jenny
 * Date:2017/3/15 15:53
 * E-mail：fishloveqin@gmail.com
 * 店内看
 */

public class StoreFragment extends BaseFragment implements DealerDataView<StoreCarListModel> {
    protected TextView isCouponTv;
    protected ListView mList;

    private LinearLayout mBrandLayout, mPriceLayout, mSeatLayout;
    private RelativeLayout mFilterContent;
    private List<BaseFragment> mSubFragments = new ArrayList<BaseFragment>();
    private String mBrandId = "", mCityId = "", mPage = "",
            mSeatKey = "";
    private String mPriceMin = "", mPriceMax = "", mPriceKey = "";
    private String mLongitude = "", mLatitude = "";
    private List<StoreCarListModel.ListBean> mDatas = new ArrayList<StoreCarListModel.ListBean>();

    private List<StoreCarListModel.ListBean> mCouponDatas = new ArrayList<StoreCarListModel.ListBean>();
    private DealerDataPresenter mPresenter;

    private String mCarId = "";

    @Override
    protected void updateData() {

        KPlayCarApp app = ((KPlayCarApp) getActivity().getApplication());
        mPresenter.getStoreCarLists(mBrandId, app.cityId+"", mCarId, mPage, mPriceKey, mPriceMin,
                mPriceMax, mSeatKey, mLongitude, mLatitude);
    }


    @Override
    public void updateInfoByCity(String... args) {

//        mCityId = args[0];
//        mPresenter.getStoreCarLists(mBrandId, mCityId, mCarId, mPage, mPriceKey, mPriceMin,
//                mPriceMax, mSeatKey, mLongitude, mLatitude);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_inner_store;
    }

    private List<View> mFilterViews = new ArrayList<View>();

    @Override
    protected void initView() {
        initEmptyView();
        isCouponTv = findView(R.id.isCouponTv);
        mBrandLayout = findView(brand);
        mPriceLayout = findView(R.id.price);
        mSeatLayout = findView(R.id.seat);
        mFilterContent = findView(R.id.filterContent);

        mFilterViews.clear();
        mFilterViews.add(mBrandLayout);
        mFilterViews.add(mPriceLayout);
        mFilterViews.add(mSeatLayout);
        mBrandLayout.setOnClickListener(this);
        mPriceLayout.setOnClickListener(this);


        mSeatLayout.setOnClickListener(this);
        isCouponTv.setOnClickListener(this);
        mList = findView(R.id.list);
        isFirst = true;
        initFilterData();
        addSubFragments();
        mPresenter = new DealerDataPresenter();
        mPresenter.attachView(this);

        mCarId = KPlayCarApp.getValue(Constants.IntentParams.CAR_ID);
        if (TextUtils.isEmpty(mCarId)) {
            mCarId = "";
        }

        KPlayCarApp app = (KPlayCarApp) getActivity().getApplication();
        mBrandId = "";
        mCityId = "";
        mPage = "";
        mPriceKey = "";
        mPriceMin = "";
        mPriceMax = "";
        mSeatKey = "";
        mLongitude = app.longitude + "";
        mLatitude = app.latitude + "";

        if (app.cityId != 0) {
            mCityId = app.cityId + "";
        }

        mPresenter.getStoreCarLists(mBrandId, mCityId, mCarId, mPage, mPriceKey, mPriceMin,
                mPriceMax, mSeatKey, mLongitude, mLatitude);
    }


    /**
     * 创建子tab项
     */
    private void initFilterData() {

        String[] arrays = mRes.getStringArray(R.array.filter_type2);

        int size = arrays.length;
        for (int i = 0; i < size; i++) {

            TextView titleTv = (TextView) mFilterViews.get(i).findViewById(R.id.filterTitle);
            titleTv.setText(arrays[i]);
        }
    }

    private void setFilterTitle(int index, String title) {

        TextView titleTv = (TextView) mFilterViews.get(index).findViewById(R.id.filterTitle);
        titleTv.setText(title);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        boolean flag = false;
        Object obj = v.getTag();
        if (obj != null) {
            flag = Boolean.parseBoolean(obj.toString());
        }
        switch (id) {

            case R.id.isCouponTv:

                if (flag) {
                    v.setSelected(false);
                    v.setTag(false);

                    bindAdapterData(mDatas);
                } else {
                    v.setSelected(true);
                    v.setTag(true);
                    //开启线程异步本地过滤数据
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            filterCouponsDatas();
                            mHandler.sendEmptyMessage(0);
                        }
                    }).start();

                }

                break;

            case R.id.price:
                setBtnSelectState(v);
                switchSubFragment(1);
                break;
            case brand:
                setBtnSelectState(v);
                switchSubFragment(0);
                break;
            case R.id.seat:
                setBtnSelectState(v);
                switchSubFragment(2);
                break;
        }

    }

    private void setBtnSelectState(View v) {

        for (View view : mFilterViews) {

            boolean flag = false;
            Object obj = view.getTag();
            if (obj != null) {
                flag = Boolean.parseBoolean(obj.toString());
            }
            if (view == v) {
                if (flag) {

                    setSelected(view, false);
                    mFilterContent.setVisibility(View.GONE);

                } else {

                    setSelected(view, true);
                    mFilterContent.setVisibility(View.VISIBLE);

                }
            } else {

                setSelected(view, false);
            }
        }
    }

    private void setSelected(View v, boolean selected) {
        v.findViewById(R.id.filterTitle).setSelected(selected);
        v.findViewById(R.id.filterImg).setSelected(selected);
        v.setTag(selected);
    }

    private void addSubFragments() {

        mSubFragments.clear();
        BrandFilterFragment brandFilterFragment = new BrandFilterFragment();
        brandFilterFragment.setCallback(mBrandCallback);
        PriceFilterFragment priceFilterFragment = new PriceFilterFragment();
        priceFilterFragment.setCallback(mPriceCallback);
        SeatFilterFragment seatFilterFragment = new SeatFilterFragment();
        seatFilterFragment.setCallback(mSeatCallback);
        mSubFragments.add(brandFilterFragment);
        mSubFragments.add(priceFilterFragment);
        mSubFragments.add(seatFilterFragment);

        FragmentManager fragmentManager = getChildFragmentManager();

        for (BaseFragment f : mSubFragments) {
            if (!f.isAdded()) {
                fragmentManager.beginTransaction().add(R.id.filterContent, f).commit();
            }
        }
    }

    private FragmentDataCallback<DataItem> mPriceCallback = new FragmentDataCallback<DataItem>() {
        @Override
        public void refresh(DataItem dataItem) {

            setBtnSelectState(mPriceLayout);
            mFilterContent
                    .setVisibility(View.GONE);
            mPriceMin = dataItem.priceMinStr;

            mPriceMax = dataItem.priceMaxStr;
            setFilterTitle(1,
                    dataItem.name);
            mSeatKey = dataItem.key;
            mPresenter.getStoreCarLists(
                    mBrandId, mCityId, mCarId,
                    mPage, mPriceKey, mPriceMin,
                    mPriceMax, mSeatKey,
                    mLongitude, mLatitude);
        }
    };
    private FragmentDataCallback<DataItem> mSeatCallback = new FragmentDataCallback<DataItem>() {
        @Override
        public void refresh(DataItem dataItem) {
            setBtnSelectState(mSeatLayout);
            setFilterTitle(2, dataItem.name);
            mFilterContent
                    .setVisibility(View.GONE);
            mSeatKey = dataItem.key + "";
            mPresenter.getStoreCarLists(
                    mBrandId, mCityId, mCarId,
                    mPage, mPriceKey, mPriceMin,
                    mPriceMax, mSeatKey,
                    mLongitude, mLatitude);
        }
    };

    private FragmentDataCallback<Brand> mBrandCallback = new FragmentDataCallback<Brand>() {
        @Override
        public void refresh(Brand brand) {
            setBtnSelectState(mBrandLayout);
            setFilterTitle(0,
                    brand.brandName);
            mFilterContent
                    .setVisibility(View.GONE);
            mBrandId = brand.brandId + "";
            mPresenter.getStoreCarLists(
                    mBrandId, mCityId, mCarId,
                    mPage, mPriceKey, mPriceMin,
                    mPriceMax, mSeatKey,
                    mLongitude, mLatitude);
        }
    };

    /**
     * 根据index切换Fragment布局(已缓存Fragment， 调用show、hide方法显示、隐藏)
     *
     * @param index
     */
    private void switchSubFragment(int index) {

        FragmentManager fragmentManager = getChildFragmentManager();

        BaseFragment fragment = mSubFragments.get(index);
        for (BaseFragment f : mSubFragments) {

            if (f == fragment) {

                fragmentManager.beginTransaction().show(fragment).commit();
            } else {
                fragmentManager.beginTransaction().hide(f).commit();
            }
        }

    }

    private boolean isFirst = true;

    private void bindAdapterData(List<StoreCarListModel.ListBean> datas) {
        WrapAdapter<StoreCarListModel.ListBean> adapter = new WrapAdapter<StoreCarListModel.ListBean>(
                mContext, mDatas, R.layout.store_inner_car_list_item) {

            protected TextView interiorTv;
            protected TextView appearanceTv;
            protected LinearLayout coverLayout;
            protected SimpleDraweeView cover2;
            protected SimpleDraweeView cover1;
            protected TextView name;
            protected SimpleDraweeView logo;
            protected TextView mBrandNameTv;
            private int isSeries = -1;

            @Override
            public void convert(ViewHolder helper, final StoreCarListModel.ListBean item) {

                isSeries = item.isSeries;
                logo = helper.getView(R.id.logo);
                mBrandNameTv = helper.getView(R.id.brandName);
                mBrandNameTv.setText(item.factoryName);
                name = helper.getView(R.id.name);
                cover1 = helper.getView(R.id.cover1);
                cover2 = helper.getView(R.id.cover2);
                coverLayout = helper.getView(R.id.coverLayout);
                appearanceTv = helper.getView(R.id.appearanceTv);
                interiorTv = helper.getView(R.id.interiorTv);

                String tag1 = String.format(mRes.getString(R.string.color_tag1),
                        item.appearanceColorName);
                String tag2 = String.format(mRes.getString(R.string.color_tag2),
                        item.interiorColorName);

                if ("- -".equals(item.appearanceColorName.trim())) {
                    appearanceTv.setVisibility(View.GONE);
                } else {
                    appearanceTv.setVisibility(View.VISIBLE);
                }
                if ("- -".equals(item.interiorColorName.trim())) {
                    interiorTv.setVisibility(View.GONE);
                } else {
                    interiorTv.setVisibility(View.VISIBLE);
                }
                CommonUtils.customDisplayText(mRes, appearanceTv, tag1, R.color.text_color12);
                CommonUtils.customDisplayText(mRes, interiorTv, tag2, R.color.text_color12);
                name.setText(item.seriesName + " " + item.carName);
                logo.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.logo, 144, 144)));
                cover1.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.appearanceImg, 270, 203)));
                cover2.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.interiorImg, 270, 203)));

                ListView list = helper.getView(R.id.list);
                list.setAdapter(new WrapAdapter<StoreCarListModel.ListBean.OrganizationBean>(
                        mContext, item.organization, R.layout.store_dealer_list_item) {

                    protected RelativeLayout titleLayout;
                    protected TextView xcImg;
                    protected TextView zcImg;
                    protected TextView orgAddress;
                    protected ImageView moreIcon;
                    protected TextView orgTitle;
                    protected TextView distanceTv;

                    @Override
                    public void convert(ViewHolder helper,
                                        StoreCarListModel.ListBean.OrganizationBean item) {

                        orgTitle = helper.getView(R.id.orgTitle);
                        orgTitle.setText(item.orgName);
                        distanceTv = helper.getView(R.id.distanceTv);

                        int distance = item.distance;
                        if (distance == 0) {
                            distanceTv.setText("- -米");
                        } else {

                            distanceTv.setText(NumFormatUtil.getFmtOneNum(item.distance / 1000.0) + "km");
                        }

                        moreIcon = helper.getView(R.id.moreIcon);
                        orgAddress = helper.getView(R.id.orgAddress);
                        orgAddress.setText(item.address);
                        zcImg = helper.getView(R.id.zcImg);
                        xcImg = helper.getView(R.id.xcImg);
                        titleLayout = helper.getView(R.id.titleLayout);

                        if (item.typeExist == 1) {
                            xcImg.setVisibility(View.VISIBLE);
                        } else {
                            xcImg.setVisibility(View.GONE);
                        }
                        zcImg = helper.getView(R.id.zcImg);
                        if (item.typeShow == 1) {
                            zcImg.setVisibility(View.VISIBLE);
                        } else {
                            zcImg.setVisibility(View.GONE);
                        }
                    }
                });

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

                        StoreCarListModel.ListBean.OrganizationBean organizationBean = (StoreCarListModel.ListBean.OrganizationBean) parent
                                .getAdapter().getItem(position);
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, organizationBean.id);
                        args.put(Constants.IntentParams.DATA, isSeries);
                        args.put(Constants.IntentParams.DATA2, organizationBean.orgId);
                        args.put(Constants.IntentParams.DATA3,
                                item.factoryName + item.seriesName + "-" + organizationBean.orgName);
                        CommonUtils.startNewActivity(mContext, args,
                                StoreShowCarDetailActivity.class);

                    }
                });
                ViewUtil.setListViewHeightBasedOnChildren(list);
            }
        };
        mList.setAdapter(adapter);

        if (isFirst) {
            isFirst = false;

            View view = LayoutInflater.from(mContext).inflate(R.layout.bottom_hint_layout, null,
                    false);
            mList.addFooterView(view);
        }
        ViewUtil.setListViewHeightBasedOnChildren(mList);
    }

    @Override
    public void showData(StoreCarListModel storeCarListModel) {


        mDatas.clear();
        mDatas.addAll(storeCarListModel.list);
        int size = mDatas.size();
        if (size == 0) {
            mEmptyLayout.setVisibility(View.VISIBLE);
            mList.setVisibility(View.GONE);
        } else {
            mEmptyLayout.setVisibility(View.GONE);
            mList.setVisibility(View.VISIBLE);
            bindAdapterData(mDatas);
        }

    }

    /**
     * 筛选出有优惠的数据
     */
    private void filterCouponsDatas() {

        mCouponDatas.clear();
        for (StoreCarListModel.ListBean store : mDatas) {

            for (StoreCarListModel.ListBean.OrganizationBean org : store.organization) {

                if (org.benefit == 1) {
                    mCouponDatas.add(store);
                }
            }
        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            bindAdapterData(mCouponDatas);
        }
    };

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {
        dismissLoadingDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }
        KPlayCarApp.removeValue(Constants.IntentParams.CAR_ID);
    }
}
