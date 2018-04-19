package com.tgf.kcwc.finddiscount;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.DiscountGiftModel;
import com.tgf.kcwc.mvp.presenter.DiscountGiftPresenter;
import com.tgf.kcwc.mvp.view.DiscountGiftView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.FilterPopwinUtil;
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

public class GiftDiscountFragment extends BaseFragment implements DiscountGiftView {
    private LinearLayout                             mFilterLayout;
    private LinearLayout                             mBrandLayout;
    private LinearLayout                             mPriceLayout;
    private LinearLayout                             mSortLayout;
    private DropDownBrandSpinner                     mDropDownBrandSpinner;
    private DropDownPriceView                        mDropDownPriceView;
    private DropDownSingleSpinner                    mDropDownSortSpinner;
    private ArrayList<DataItem>                      mSortItems;
    private ListView                                 dicountGiftLv;
    private WrapAdapter                              discountGiftAdapter;
    private List<DiscountGiftModel.DiscountGiftItem> mgiftModellist;
    private DiscountGiftPresenter                    mDiscountGiftPresenter;
    private int                                      priceId;
    private DataItem                                 priceDataItem = new DataItem();
    private int                                      cityid        = 22;
    private Integer                                  endPrice, brandId, orderId, special,
            startPrice;

    @Override
    protected void updateData() {

    }

    @Override
    protected void initData() {
        mDiscountGiftPresenter = new DiscountGiftPresenter();
        mDiscountGiftPresenter.attachView(this);
        mDiscountGiftPresenter.getLimitDiscountList(22, null, null, null, null, null);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_giftdiscount;
    }

    @Override
    protected void initView() {
        initFilters();
        dicountGiftLv = findView(R.id.discount_giftlv);
        setUserVisibleHint(true);
        if (discountGiftAdapter != null) {
            dicountGiftLv.setAdapter(discountGiftAdapter);
        }
        dicountGiftLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toIntent = new Intent(getContext(), GiftPackDetailActivity.class);
                toIntent.putExtra(Constants.IntentParams.ID, mgiftModellist.get(position).pri_id);
                startActivity(toIntent);
            }
        });
        CheckBox checkBox = findView(R.id.gift_checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    special = 1;
                } else {
                    special = 0;
                }
                mDiscountGiftPresenter.getLimitDiscountList(cityid, endPrice, brandId, orderId,
                    special, startPrice);
            }
        });
    }

    private void initFilters() {
        mFilterLayout =  findView(R.id.gift_filterlayout);
        mBrandLayout =  findView(R.id.gift_brand);
        mPriceLayout = findView(R.id.gift_price);
        mSortLayout =  findView(R.id.gift_sort);
        mBrandLayout.setOnClickListener(this);
        mPriceLayout.setOnClickListener(this);
        mSortLayout.setOnClickListener(this);
        FilterPopwinUtil.commonFilterTile(mBrandLayout, "品牌");
        FilterPopwinUtil.commonFilterTile(mPriceLayout, "价格");
        FilterPopwinUtil.commonFilterTile(mSortLayout, "排序");
        mDropDownBrandSpinner = new DropDownBrandSpinner(getContext(),1,"1",0);
        mDropDownPriceView = new DropDownPriceView(getContext());

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
                        mDiscountGiftPresenter.getLimitDiscountList(cityid, endPrice, brandId,
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
                        mDiscountGiftPresenter.getLimitDiscountList(cityid, endPrice, brandId,
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
                        mDiscountGiftPresenter.getLimitDiscountList(cityid, endPrice, brandId,
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
            case R.id.gift_price:
                FilterPopwinUtil.commonFilterTitleColor(mRes, mPriceLayout, R.color.text_color1);
                FilterPopwinUtil.commonFilterImage(mPriceLayout, R.drawable.filter_drop_down_r);
                mDropDownPriceView.showAsDropDownBelwBtnView(mFilterLayout);
                break;
            case R.id.gift_brand:
                FilterPopwinUtil.commonFilterTitleColor(mRes, mBrandLayout, R.color.text_color1);
                FilterPopwinUtil.commonFilterImage(mBrandLayout, R.drawable.filter_drop_down_r);
                mDropDownBrandSpinner.showAsDropDownBelwBtnView(mFilterLayout);
                break;
            case R.id.gift_sort:
                FilterPopwinUtil.commonFilterTitleColor(mRes, mSortLayout, R.color.text_color1);
                FilterPopwinUtil.commonFilterImage(mSortLayout, R.drawable.filter_drop_down_r);
                mDropDownSortSpinner.showAsDropDownBelwBtnView(mFilterLayout);
                break;
            default:
                break;
        }
    }

    private void initAdatper() {
        //        dicountGiftLv.setAdapter(new WrapAdapter<DiscountCouponModel.DiscountCouponItem>(R.layout.listitem_giftdiscount,) {
        //            @Override
        //            public void convert(ViewHolder helper, DiscountCouponModel.DiscountCouponItem item) {
        //
        //            }
        //        });
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showLimitList(List<DiscountGiftModel.DiscountGiftItem> giftModellist) {

        //mgiftModellist.clear();
        mgiftModellist = giftModellist;
        //        initAdapter();
        if (discountGiftAdapter == null) {
            initAdapter();
        } else {
            discountGiftAdapter.notifyDataSetChanged();
        }

    }

    private void initAdapter() {
        discountGiftAdapter = new WrapAdapter<DiscountGiftModel.DiscountGiftItem>(getContext(),
            mgiftModellist, R.layout.listitem_giftdiscount) {
            @Override
            public void convert(ViewHolder helper, DiscountGiftModel.DiscountGiftItem item) {
                String coverUrl = URLUtil.builderImgUrl(item.cover, 270, 203);
                helper.setSimpleDraweeViewURL(R.id.gift_cover, coverUrl);
                helper.setText(R.id.gift_title, item.title);
                helper.setText(R.id.gift_desc, item.desc);
                View statusMoreIv = helper.getView(R.id.gift_orgmore);
                statusMoreIv.setVisibility(View.GONE);
                if (item.giftOrgs != null && item.giftOrgs.size() != 0) {
                    helper.setText(R.id.gift_org, item.giftOrgs.get(0).name);
                    if (item.giftOrgs.size() > 1) {
                        statusMoreIv.setVisibility(View.VISIBLE);
                    }
                }
                statusMoreIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommonUtils.showToast(getContext(), "更多");
                    }
                });
            }
        };
        dicountGiftLv.setAdapter(discountGiftAdapter);
    }
}
