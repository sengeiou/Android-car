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

public class HomeLimittimeViewHolder {
    public final RelativeLayout mHomeTitleLayout;
    public final SimpleDraweeView mHomeCoverIv;
    public final MyGridView mHomeMygridview;
    public final TextView mHomeTitleTv;
    public final TextView mCountericon;
    public final CountdownView mLimittimeSettimetext;
    public final SimpleDraweeView mHomeStoreCoverIv;
    public final TextView mHomeStoreNameTv;
    public final TextView mHomeStoreAddressTv;
    public final ImageView mHomeStoreTelTv;
    public final View limitTimeLayout;
    public final View mHomeStoreLayout;
    public final SimpleDraweeView mBrandicon;
    public final TextView mBrandName;

    public HomeLimittimeViewHolder(View viewRoot) {
        mHomeTitleLayout = (RelativeLayout) viewRoot.findViewById(R.id.home_titleLayout);
        mHomeCoverIv = (SimpleDraweeView) viewRoot.findViewById(R.id.home_coverIv);
        mHomeStoreLayout = viewRoot.findViewById(R.id.home_storeLayout);

        mHomeMygridview = (MyGridView) viewRoot.findViewById(R.id.home_mygridview);
        mHomeTitleTv = (TextView) viewRoot.findViewById(R.id.home_titleTv);
        mCountericon = (TextView) viewRoot.findViewById(R.id.countericon);
        mLimittimeSettimetext = (CountdownView) viewRoot.findViewById(R.id.limittime_settimetext);
        mHomeStoreCoverIv = (SimpleDraweeView) viewRoot.findViewById(R.id.home_storeCoverIv);
        mHomeStoreNameTv = (TextView) viewRoot.findViewById(R.id.home_storeNameTv);
        mHomeStoreAddressTv = (TextView) viewRoot.findViewById(R.id.home_storeAddressTv);
        mHomeStoreTelTv = (ImageView) viewRoot.findViewById(R.id.home_storeTelTv);
        limitTimeLayout = viewRoot.findViewById(R.id.home_limitLayout);
        mBrandicon = (SimpleDraweeView) viewRoot.findViewById(R.id.brandicon);
        mBrandName = (TextView) viewRoot.findViewById(R.id.brand_name);

    }
}
