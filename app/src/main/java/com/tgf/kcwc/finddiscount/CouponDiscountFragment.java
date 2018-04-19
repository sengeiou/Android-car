package com.tgf.kcwc.finddiscount;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.DiscountCouponModel;
import com.tgf.kcwc.mvp.presenter.DiscountCouponPresenter;
import com.tgf.kcwc.mvp.view.DiscountCouponView;
import com.tgf.kcwc.util.FilterPopwinUtil;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.DropDownBrandSpinner;
import com.tgf.kcwc.view.DropDownPriceView;
import com.tgf.kcwc.view.DropDownSingleSpinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/4/24 0024
 * E-mail:hekescott@qq.com
 */

public class CouponDiscountFragment extends BaseFragment implements DiscountCouponView {
    private LinearLayout                                 mFilterLayout;
    private LinearLayout                                 mBrandLayout;
    private LinearLayout                                 mPriceLayout;
    private LinearLayout                                 mSortLayout;
    private DropDownBrandSpinner                         mDropDownBrandSpinner;
    private DropDownPriceView                            mDropDownPriceView;
    private DropDownSingleSpinner                        mDropDownSortSpinner;
    private ArrayList<DataItem>                          mSortItems;
    private ListView                                     couponLV;
    private WrapAdapter                                  dicountCouponAdapter;
    private List<DiscountCouponModel.DiscountCouponItem> mCoupontModellist;
    private DataItem                                     priceDataItem = new DataItem();
    private int                                          priceId;
    private int                                          cityid        = 22;
    private Integer                                      endPrice, brandId, orderId, special,
            startPrice;
    private DiscountCouponPresenter                      discountCouponPresenter;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_coupondiscount;
    }

    @Override
    protected void initData() {
        discountCouponPresenter = new DiscountCouponPresenter();
        discountCouponPresenter.attachView(this);
        discountCouponPresenter.getLimitDiscountList(cityid, null, null, null, null, null);
    }

    @Override
    protected void initView() {
        initfilters();
        couponLV = findView(R.id.dicountcoupon_lv);
        setUserVisibleHint(true);
        if (dicountCouponAdapter != null) {
            couponLV.setAdapter(dicountCouponAdapter);
        }
       CheckBox checkBox = findView(R.id.coupon_checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    special = 1;
                }else{
                    special = 0;
                }
                discountCouponPresenter.getLimitDiscountList(cityid, endPrice, brandId,
                        orderId, special, startPrice);
            }
        });
    }

    private void initfilters() {
        mFilterLayout = (LinearLayout) findView(R.id.coupon_filterlayout);
        mBrandLayout = (LinearLayout) findView(R.id.coupon_brand);
        mPriceLayout = (LinearLayout) findView(R.id.coupon_price);
        mSortLayout = (LinearLayout) findView(R.id.coupon_sort);
        mBrandLayout.setOnClickListener(this);
        mPriceLayout.setOnClickListener(this);
        mSortLayout.setOnClickListener(this);
        FilterPopwinUtil.commonFilterTile(mBrandLayout, "品牌");
        FilterPopwinUtil.commonFilterTile(mPriceLayout, "价格");
        FilterPopwinUtil.commonFilterTile(mSortLayout, "排序");
        mDropDownPriceView = new DropDownPriceView(getContext());
        mDropDownBrandSpinner = new DropDownBrandSpinner(getContext(),1,"1",0);

        mDropDownBrandSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                FilterPopwinUtil.commonFilterTitleColor(mRes, mBrandLayout,
                    R.color.text_content_color);
                FilterPopwinUtil.commonFilterImage(mBrandLayout, R.drawable.fitler_drop_down_n);
                DataItem dataItem = mDropDownBrandSpinner.getSelectDataItem();
                if (dataItem != null) {
                    FilterPopwinUtil.commonFilterTile(mBrandLayout, dataItem.name);
                    if (brandId == null || dataItem.id != brandId) {
                        brandId = dataItem.id;
                        discountCouponPresenter.getLimitDiscountList(cityid, endPrice, brandId,
                            orderId, special, startPrice);
                    }
                }
            }
        });
        mDropDownPriceView.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                FilterPopwinUtil.commonFilterTitleColor(mRes, mPriceLayout,
                    R.color.text_content_color);
                FilterPopwinUtil.commonFilterImage(mPriceLayout, R.drawable.fitler_drop_down_n);
                DataItem dataItem = mDropDownPriceView.getSelectDataItem();
                if (dataItem != null) {
                    if (dataItem.id != priceId) {
                        priceId = dataItem.id;
                        priceDataItem = dataItem;
                        endPrice = dataItem.priceMax;
                        startPrice = dataItem.priceMin;
                        discountCouponPresenter.getLimitDiscountList(cityid, endPrice, brandId,
                            orderId, special, startPrice);
                    }
                    FilterPopwinUtil.commonFilterTile(mPriceLayout, "价格");
                }
            }
        });
        mSortItems = new ArrayList<DataItem>();
        String[] arrays = mRes.getStringArray(R.array.filter_discount_limit);
        int id = 1;
        for (String s : arrays) {
            DataItem dataItem = new DataItem();
            dataItem.name = s;
            dataItem.id = id;
            mSortItems.add(dataItem);
            id++;
        }
        mDropDownSortSpinner = new DropDownSingleSpinner(getContext(), mSortItems);
        mDropDownSortSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                FilterPopwinUtil.commonFilterTitleColor(mRes, mSortLayout,
                    R.color.text_content_color);
                FilterPopwinUtil.commonFilterImage(mSortLayout, R.drawable.fitler_drop_down_n);
                DataItem dataItem = mDropDownSortSpinner.getSelectDataItem();
                if (dataItem != null) {
                    FilterPopwinUtil.commonFilterTile(mSortLayout, "排序");
                    if (orderId == null || dataItem.id != orderId) {
                        orderId = dataItem.id;
                        discountCouponPresenter.getLimitDiscountList(cityid, endPrice, brandId,
                            orderId, special, startPrice);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.coupon_price:
                FilterPopwinUtil.commonFilterTitleColor(mRes, mPriceLayout, R.color.text_color1);
                FilterPopwinUtil.commonFilterImage(mPriceLayout, R.drawable.filter_drop_down_r);
                mDropDownPriceView.showAsDropDownBelwBtnView(mFilterLayout);
                break;
            case R.id.coupon_brand:
                FilterPopwinUtil.commonFilterTitleColor(mRes, mBrandLayout, R.color.text_color1);
                FilterPopwinUtil.commonFilterImage(mBrandLayout, R.drawable.filter_drop_down_r);
                mDropDownBrandSpinner.showAsDropDownBelwBtnView(mFilterLayout);
                break;
            case R.id.coupon_sort:
                FilterPopwinUtil.commonFilterTitleColor(mRes, mSortLayout, R.color.text_color1);
                FilterPopwinUtil.commonFilterImage(mSortLayout, R.drawable.filter_drop_down_r);
                mDropDownSortSpinner.showAsDropDownBelwBtnView(mFilterLayout);
                break;
            default:
                break;
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showLimitList(List<DiscountCouponModel.DiscountCouponItem> coupontModellist) {
        mCoupontModellist = coupontModellist;
        if (dicountCouponAdapter == null) {
            initAdapter();
        } else {
            dicountCouponAdapter.notifyDataSetChanged(mCoupontModellist);
        }
    }

    private void initAdapter() {
        dicountCouponAdapter = new WrapAdapter<DiscountCouponModel.DiscountCouponItem>(getContext(),
            R.layout.listitem_coupondiscount, mCoupontModellist) {
            @Override
            public void convert(ViewHolder helper, DiscountCouponModel.DiscountCouponItem item) {
                String coverUrl = URLUtil.builderImgUrl(item.cover, 270, 203);
                helper.setSimpleDraweeViewURL(R.id.coupon_cover, coverUrl);
                helper.setText(R.id.coupon_title, item.title);
                helper.setText(R.id.coupon_desc, item.desc);
                helper.setText(R.id.coupon_guideprice,
                    SpannableUtil.getDelLineString("¥" + item.denomination));
                helper.setText(R.id.coupon_rate_guideprice, "¥" + item.price);
                helper.setText(R.id.coupon_salenum, "已售" + item.sale);
                helper.setText(R.id.coupon_meter, item.meter + "km");
            }
        };
        couponLV.setAdapter(dicountCouponAdapter);
    }
}
