package com.tgf.kcwc.tripbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.coupon.CouponDetailActivity;
import com.tgf.kcwc.mvp.model.TripBookDetailModel;
import com.tgf.kcwc.mvp.presenter.TripAroundCoupnPresenter;
import com.tgf.kcwc.mvp.view.TripAroundCouponView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/5/15 0015
 * E-mail:hekescott@qq.com
 */

public class TripBookCouponActivity extends BaseActivity implements TripAroundCouponView {

    private Intent fromIntent;
    private List<TripBookDetailModel.Coupon> mCouponList;
    private ListView couponLv;
    private TripAroundCoupnPresenter tripAroundCouponPresenter;
    private int nodeId;

    @Override
    protected void setUpViews() {
        fromIntent = getIntent();
        nodeId = fromIntent.getIntExtra(Constants.IntentParams.ID, 0);
        couponLv = (ListView) findViewById(R.id.tripbook_couponlv);
        tripAroundCouponPresenter = new TripAroundCoupnPresenter();
        tripAroundCouponPresenter.attachView(this);
        tripAroundCouponPresenter.getAroudCouponlist(nodeId, 1, 999);

        couponLv.addFooterView(View.inflate(this, R.layout.bottom_hint_layout, null));
        couponLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toCouponIntent = new Intent(TripBookCouponActivity.this, CouponDetailActivity.class);
                toCouponIntent.putExtra(Constants.IntentParams.ID, mCouponList.get(position).couponId);
                startActivity(toCouponIntent);

            }
        });
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("附近代金券");
        backEvent(back);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripbookcoupon);
    }

    @Override
    public void showCouponlist(ArrayList<TripBookDetailModel.Coupon> couponList) {
        mCouponList = couponList;
        couponLv.setAdapter(new WrapAdapter<TripBookDetailModel.Coupon>(this,
                R.layout.listview_item_recomment_voucherlist, couponList) {
            @Override
            public void convert(ViewHolder holder, TripBookDetailModel.Coupon voucher) {
                TextView titleTv = holder.getView(R.id.listitem_recoment_coupontitle);
                titleTv.setText(voucher.couponTitle);
                holder.setSimpleDraweeViewURL(R.id.couponlist_cover, URLUtil.builderImgUrl(voucher.couponCover, 270, 203));
                Double nowPrice = Double.parseDouble(voucher.price);
                TextView nowPriceTv = holder.getView(R.id.recyleitem_near_nowprice);
                if (nowPrice == 0) {
                    nowPriceTv.setTextColor(mRes.getColor(R.color.text_color10));
                    nowPriceTv.setText("免费");
                    holder.setText(R.id.listviewitem_recomment_salenum, "已领" + voucher.couponused);
                } else {
                    nowPriceTv.setTextColor(mRes.getColor(R.color.voucher_yellow));
                    nowPriceTv.setText("￥ " + voucher.price);
                    holder.setText(R.id.listviewitem_recomment_salenum, "已售" + voucher.couponused);
                }
                SpannableString demoPrice = SpannableUtil.getDelLineString("￥ " + voucher.denomination);
                holder.setText(R.id.listviewitem_recomment_oldprice, demoPrice);

            }
        });
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
