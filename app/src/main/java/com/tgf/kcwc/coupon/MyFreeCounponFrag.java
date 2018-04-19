package com.tgf.kcwc.coupon;

import android.content.Intent;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.BasePageModel;
import com.tgf.kcwc.mvp.model.CouponDetailModel;
import com.tgf.kcwc.mvp.model.ExclusiveCoupon;
import com.tgf.kcwc.mvp.presenter.CouponPresenter;
import com.tgf.kcwc.mvp.view.CouponView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.FilterPopwinUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.DropDownSingleSpinner;
import com.tgf.kcwc.view.countdown.CountdownView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Auther: Scott
 * Date: 2017/9/22 0022
 * E-mail:hekescott@qq.com
 */

public class MyFreeCounponFrag extends BaseFragment implements CouponView<List<ExclusiveCoupon>> {
    private ListView couponLv;
    private ArrayList<DataItem> expilreDataitems = new ArrayList<>();
    private CouponPresenter mPresenter;
    //    private View layoutFilter;
    private TextView flterTitleTv;
    private int filterId = 0;
    private TextView itemNumTv;
    private String token;
    private LinearLayout mExpireLayout;
    private DropDownSingleSpinner dropDownExpireSpinner;
    private View titleFilterLayout;
    private int mPageIndex = 1;
    private List<ExclusiveCoupon> mExclusiveCoupons;
    private View emptyBoxView;

    @Override
    protected void updateData() {
        mPresenter.getExCoupons(token, filterId + "");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_vouchermyfree;
    }

    @Override
    protected void initView() {
        couponLv = findView(R.id.exclusive_coupon_lv);
        mPresenter = new CouponPresenter();
        mPresenter.attachView(this);
        token = IOUtils.getToken(mContext);

        initFilter();
        emptyBoxView = findView(R.id.emptyboxTv);
        findView(R.id.filterTitlelayout).setOnClickListener(this);
        itemNumTv = findView(R.id.exclusive_itemnum);
        couponLv.addFooterView(View.inflate(getContext(), R.layout.footer_myfreecoupon_listview, null));
        couponLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < mExclusiveCoupons.size() - 1) {
                    Intent toCouponDetailAct = new Intent(mContext, CouponDetailActivity.class);
                    toCouponDetailAct.putExtra(Constants.IntentParams.ID, mExclusiveCoupons.get(position).coupon.id);
                    toCouponDetailAct.putExtra(Constants.IntentParams.ID2, mExclusiveCoupons.get(position).id);
                    toCouponDetailAct.putExtra(Constants.IntentParams.DATA, mExclusiveCoupons.get(position).num);
                    startActivity(toCouponDetailAct);
                }
            }
        });
    }

    private void initFilter() {
        titleFilterLayout = findView(R.id.layout_filter);
        mExpireLayout = findView(R.id.exclusive_expirely);
        mExpireLayout.setOnClickListener(this);
        FilterPopwinUtil.commonFilterTile(mExpireLayout, "有效");
        flterTitleTv = findView(R.id.filterTitle);
        String arrayFilter[] = mRes.getStringArray(R.array.filter_expire);
        for (int i = 0; i < arrayFilter.length; i++) {
            DataItem dataItem = new DataItem(i, arrayFilter[i]);
            expilreDataitems.add(dataItem);
        }
        dropDownExpireSpinner = new DropDownSingleSpinner(getContext(), expilreDataitems);
        dropDownExpireSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                FilterPopwinUtil.commonFilterTitleColor(mRes, mExpireLayout,
                        R.color.text_color3);
                FilterPopwinUtil.commonFilterImage(mExpireLayout, R.drawable.fitler_drop_down_n);
                DataItem dataItem = dropDownExpireSpinner.getSelectDataItem();
                if (dataItem != null) {
                    FilterPopwinUtil.commonFilterTile(mExpireLayout, dataItem.name);
                    if (dataItem.id != filterId) {
                        mPageIndex = 0;
                        filterId = dataItem.id;
                        mPresenter.getExCoupons(token, filterId + "");
                    }
                }
            }
        });
    }


    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }


    @Override
    public void showDatas(List<ExclusiveCoupon> exclusiveCoupons) {

        mExclusiveCoupons = exclusiveCoupons;
        itemNumTv.setText("共" + mExclusiveCoupons.size() + "条");
        if (mExclusiveCoupons == null || mExclusiveCoupons.size() == 0) {
            emptyBoxView.setVisibility(View.VISIBLE);
            couponLv.setVisibility(View.GONE);
        } else {
            emptyBoxView.setVisibility(View.GONE);
            couponLv.setVisibility(View.VISIBLE);
        }


        couponLv.setAdapter(new WrapAdapter<ExclusiveCoupon>(mContext, R.layout.listitem_exculsive_coupon, mExclusiveCoupons) {
            @Override
            public void convert(ViewHolder helper, final ExclusiveCoupon couponModel) {
                TextView titelTv = helper.getView(R.id.listitem_recoment_coupontitle);
                titelTv.setText(couponModel.coupon.title);
                final CouponDetailModel.Dealers dealer = couponModel.dealers.get(0);
                TextView exClusiveGetTv = helper.getView(R.id.listviewitem_exclusive_get);

//                helper.setText(R.id.distance, dealer.distance + "KM");
                helper.setText(R.id.distance, "x " + couponModel.num);
                helper.setText(R.id.dealer_name, couponModel.issueOrg.name);
                helper.getView(R.id.listviewitem_recomment_salenum);
                Double nowPrice = Double.parseDouble(couponModel.coupon.price);
                TextView nowPriceTv = helper.getView(R.id.recyleitem_near_nowprice);
                if (filterId == 1) {
                    exClusiveGetTv.setVisibility(View.GONE);
                }
                if (nowPrice == 0) {
                    nowPriceTv.setText("免费");
                    exClusiveGetTv.setText("去领取");
                    exClusiveGetTv.setBackground(mRes.getDrawable(R.drawable.shape_radiusorangerect_bg));
                } else {
                    nowPriceTv.setText("￥" + couponModel.coupon.price);
                    exClusiveGetTv.setText("去购买");
                    exClusiveGetTv.setBackground(mRes.getDrawable(R.drawable.shape_radiusrect_bg));
                }
                exClusiveGetTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (filterId == 1) {
                            return;
                        }
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, couponModel.coupon.id);
                        args.put(Constants.IntentParams.ID2, couponModel.id + "");
                        args.put(Constants.IntentParams.COUNT, couponModel.num );
                        CommonUtils.startNewActivity(mContext, args, CouponOrderActivity.class);
                    }
                });
                SpannableString oldPrice = SpannableUtil.getDelLineString("￥" + couponModel.coupon.denomination);
                helper.setText(R.id.listviewitem_recomment_oldprice, oldPrice);
                String coverUrl = URLUtil.builderImgUrl(couponModel.coupon.cover, 270, 203);
                View v = helper.getView(R.id.counterLayout);
                CountdownView setTimeText = helper.getView(R.id.setTimeText);
                if (!TextUtils.isEmpty(couponModel.expirationTime)) {
                    v.setVisibility(View.VISIBLE);
                    setTimeText.start(DateFormatUtil.getTime(couponModel.expirationTime) - DateFormatUtil.getTime(couponModel.currentTime));
                } else {
                    v.setVisibility(View.GONE);
                }
                helper.getView(R.id.nickname_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent toUserPageAct = new Intent(getContext(), UserPageActivity.class);
                        toUserPageAct.putExtra(Constants.IntentParams.ID, couponModel.user.id);
                        startActivity(toUserPageAct);
                    }
                });
                helper.setText(R.id.exclusive_nickname, couponModel.user.nickname + "发送");
                helper.setSimpleDraweeViewURL(R.id.exculsivecoupon_cover, coverUrl);
            }
        });
        couponLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (filterId == 1) {
                    return;
                }
                if (position < 0 || position >= mExclusiveCoupons.size()) {
                    return;
                }

                ExclusiveCoupon couponModel = mExclusiveCoupons.get(position);
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, couponModel.coupon.id);
                args.put(Constants.IntentParams.ID2, couponModel.id);
                args.put(Constants.IntentParams.DATA, couponModel.num);
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
            case R.id.exclusive_expirely:
                dropDownExpireSpinner.showAsDropDownBelwBtnView(titleFilterLayout);

                break;
//            case R.id.filterTitlelayout:
//                layoutFilter.setVisibility(View.VISIBLE);
//                break;
            default:
                break;
        }
    }

    @Override
    public void shwoFailed(String statusMessage) {

    }

    @Override
    public void shoMyCouponCount(BasePageModel.Pagination pagination) {

    }

    @Override
    public void showMyCouponList(ArrayList list) {

    }
}
