package com.tgf.kcwc.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.view.MyGridView;
import com.tgf.kcwc.view.countdown.CountdownView;

/**
 * Auther: Scott
 * Date: 2017/9/14 0014
 * E-mail:hekescott@qq.com
 */

public class HomeLimitbrandViewHolder {
    public final RelativeLayout mLatestExhibitionRl;
    public final TextView mLastestExhibitTiltetv;
    public final SimpleDraweeView mBrandicon;
    public final TextView mBrandName;
    public final SimpleDraweeView mHomeCoverIv;
    public final MyGridView mHomeMygridview;
    public final TextView mBranddiscountMaxprice;
    public final TextView mBranddiscountData;
    public final TextView mLimitbrandBaomingTv;
    public final View mLimitTickeLayout;
    public final TextView mGotoTicketTv;


    public HomeLimitbrandViewHolder(View viewRoot) {
        mLatestExhibitionRl = (RelativeLayout) viewRoot.findViewById(R.id.latest_exhibition_rl);
        mLastestExhibitTiltetv = (TextView) viewRoot.findViewById(R.id.lastest_exhibit_tiltetv);
        mBrandicon = (SimpleDraweeView) viewRoot.findViewById(R.id.brandicon);
        mBrandName = (TextView) viewRoot.findViewById(R.id.brand_name);
        mHomeCoverIv = (SimpleDraweeView) viewRoot.findViewById(R.id.home_coverIv);
        mHomeMygridview = (MyGridView) viewRoot.findViewById(R.id.home_mygridview);
        mBranddiscountMaxprice = (TextView) viewRoot.findViewById(R.id.branddiscount_maxprice);
        mBranddiscountData = (TextView) viewRoot.findViewById(R.id.branddiscount_data);
        mLimitbrandBaomingTv = (TextView) viewRoot.findViewById(R.id.limitbrand_baomingTv);
        mGotoTicketTv = (TextView) viewRoot.findViewById(R.id.home_gototicketTv);
        mLimitTickeLayout = viewRoot.findViewById(R.id.home_gototicketLayout);

    }
}
