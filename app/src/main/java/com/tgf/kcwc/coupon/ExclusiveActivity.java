package com.tgf.kcwc.coupon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.BasePageModel;
import com.tgf.kcwc.mvp.model.CouponDetailModel;
import com.tgf.kcwc.mvp.model.ExclusiveCoupon;
import com.tgf.kcwc.mvp.model.MyCouponModel;
import com.tgf.kcwc.mvp.presenter.CouponPresenter;
import com.tgf.kcwc.mvp.view.CouponView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.FilterPopwinUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.SysUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.DropDownSingleSpinner;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.countdown.CountdownView;

import android.content.Context;
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

/**
 * Auther: Scott
 * Date: 2017/1/3 0003
 * E-mail:hekescott@qq.com
 */

public class ExclusiveActivity extends BaseActivity implements CouponView<List<ExclusiveCoupon>> {

    private ListView couponLv;
    private ArrayList<DataItem> expilreDataitems = new ArrayList<>();
    private CouponPresenter mPresenter;
//    private View layoutFilter;
    private TextView flterTitleTv;
    private int filterId =0;
    private TextView itemNumTv;
    private String token;
    private LinearLayout mExpireLayout;
    private DropDownSingleSpinner dropDownExpireSpinner;
    private View titleFilterLayout;
    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("专属代金券");
    }

    @Override
    protected void setUpViews() {
        couponLv = (ListView) findViewById(R.id.exclusive_coupon_lv);
        mPresenter = new CouponPresenter();
        mPresenter.attachView(this);
        token = IOUtils.getToken(mContext);
        mPresenter.getExCoupons(token, filterId + "");
        initFilter();
        findViewById(R.id.filterTitlelayout).setOnClickListener(this);
        itemNumTv = (TextView) findViewById(R.id.exclusive_itemnum);
    }

    private void initFilter() {
        titleFilterLayout = findViewById(R.id.layout_filter);
        mExpireLayout = (LinearLayout) findViewById(R.id.exclusive_expirely);
        mExpireLayout.setOnClickListener(this);
        FilterPopwinUtil.commonFilterTile(mExpireLayout,"有效");
        flterTitleTv = (TextView) findViewById(R.id.filterTitle);
        String arrayFilter[] = mRes.getStringArray(R.array.filter_expire);
        for (int i = 0; i < arrayFilter.length; i++) {
            DataItem dataItem = new DataItem(i, arrayFilter[i]);
            expilreDataitems.add(dataItem);
        }
        dropDownExpireSpinner = new DropDownSingleSpinner(getContext(),expilreDataitems);
        dropDownExpireSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                FilterPopwinUtil.commonFilterTitleColor(mRes, mExpireLayout,
                        R.color.text_content_color);
                FilterPopwinUtil.commonFilterImage(mExpireLayout, R.drawable.fitler_drop_down_n);
                DataItem dataItem = dropDownExpireSpinner.getSelectDataItem();
                if (dataItem != null) {
                    FilterPopwinUtil.commonFilterTile(mExpireLayout, dataItem.name);
                    if ( dataItem.id != filterId) {
                        mPageIndex=0;
                        filterId = dataItem.id;
                        mPresenter.getExCoupons(token, filterId + "");
                    }
                }
            }
        });
    }
//    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {
//
//        @Override
//        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
//            mPageIndex =0;
//            loadMore();
//        }
//
//        @Override
//        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
//            loadMore();
//            return false;
//        }
//    };

    /**
     * 加载更多
     */
//    private void loadMore() {
//        mPageIndex++;
//        mPresenter.getExCoupons(token, filterId+"");
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclusive);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.exclusive_root_lv).setPadding(0,
                    SysUtils.getStatusHeight(this), 0, 0);
        }
    }

    private List<ExclusiveCoupon> mExclusiveCoupons = new ArrayList<>();

    @Override
    public void showDatas(List<ExclusiveCoupon> exclusiveCoupons) {
        mExclusiveCoupons = exclusiveCoupons;
        itemNumTv.setText("共" + mExclusiveCoupons.size() + "条");
        couponLv.setAdapter(new WrapAdapter<ExclusiveCoupon>(mContext, R.layout.listitem_exculsive_coupon, mExclusiveCoupons) {
            @Override
            public void convert(ViewHolder helper, ExclusiveCoupon couponModel) {
                TextView titelTv = helper.getView(R.id.listitem_recoment_coupontitle);
                titelTv.setText(couponModel.coupon.title);
                CouponDetailModel.Dealers dealer = couponModel.dealers.get(0);
                TextView exClusiveGetTv = helper.getView(R.id.listviewitem_exclusive_get);

//                helper.setText(R.id.distance, dealer.distance + "KM");
                helper.setText(R.id.distance, "x " + couponModel.num);
                helper.setText(R.id.dealer_name, dealer.fullName);
                helper.getView(R.id.listviewitem_recomment_salenum);
                int nowPrice = Integer.parseInt(couponModel.coupon.price);
                TextView nowPriceTv = helper.getView(R.id.recyleitem_near_nowprice);
                if(filterId==1){
                    exClusiveGetTv.setVisibility(View.GONE);
                }
                if (nowPrice == 0) {
                    nowPriceTv.setText("免费");
                    nowPriceTv.setTextColor(mRes.getColor(R.color.style_bg1));
                    exClusiveGetTv.setText("领");
                    exClusiveGetTv.setBackground(mRes.getDrawable(R.drawable.shape_radiusgreenrect_bg));
                } else {
                    nowPriceTv.setText("￥" + couponModel.coupon.price);
                    nowPriceTv.setTextColor(mRes.getColor(R.color.voucher_yellow));
                    exClusiveGetTv.setText("抢");
                    exClusiveGetTv.setBackground(mRes.getDrawable(R.drawable.shape_radiusyellowrect_bg));
                }
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
                helper.setText(R.id.exclusive_nickname, couponModel.user.nickname + "发送");
                helper.setSimpleDraweeViewURL(R.id.exculsivecoupon_cover, coverUrl);
            }
        });
        couponLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(filterId==1){
                  return;
                }

                ExclusiveCoupon couponModel = mExclusiveCoupons.get(position);
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, couponModel.coupon.id);
                args.put(Constants.IntentParams.ID2, couponModel.id+"");
                CommonUtils.startNewActivity(mContext, args, CouponOrderActivity.class);
            }
        });
    }

    @Override
    public void shwoFailed(String statusMessage) {

    }

    @Override
    public void showMyCouponList(ArrayList<MyCouponModel.MyCouponOrder> list) {

    }

    @Override
    public void shoMyCouponCount(BasePageModel.Pagination pagination) {

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
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
