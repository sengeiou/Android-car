package com.tgf.kcwc.adapter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.view.nestlistview.NestFullListView;

/**
 * Auther: Scott
 * Date: 2017/9/12 0012
 * E-mail:hekescott@qq.com
 */

public  class HomeCouponViewHolder {
    public final SimpleDraweeView mHomeCoverIv;
    public final TextView mHomeTitleTv;
    public final TextView mHomecouponPriceTv;
    public final TextView mHomecouponGotoGetBtn;
    public final NestFullListView mHomeCouponlist;
    public final TextView mHomeMoreCouponBtn;
    public final RelativeLayout mHomeRelativeLayout;

    public HomeCouponViewHolder(View viewRoot) {
        mHomeCoverIv = (SimpleDraweeView) viewRoot.findViewById(R.id.home_coverIv);
        mHomeRelativeLayout = viewRoot.findViewById(R.id.latest_exhibition_rl);
        mHomeTitleTv = (TextView) viewRoot.findViewById(R.id.home_titleTv);
        mHomecouponPriceTv = (TextView) viewRoot.findViewById(R.id.homecoupon_priceTv);
        mHomecouponGotoGetBtn = (TextView) viewRoot.findViewById(R.id.homecoupon_gotoGetBtn);
        mHomeCouponlist = (NestFullListView) viewRoot.findViewById(R.id.home_couponlist);
        mHomeMoreCouponBtn = (TextView) viewRoot.findViewById(R.id.home_moreCouponBtn);
    }


}
