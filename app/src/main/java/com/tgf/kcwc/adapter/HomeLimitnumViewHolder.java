package com.tgf.kcwc.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class HomeLimitnumViewHolder {
    public final RelativeLayout mLatestExhibitionRl;
    public final TextView mLastestExhibitTiltetv;
    public final SimpleDraweeView mHomeCoverIv;
    public final MyGridView mHomeMygridview;
    public final TextView mHomeTitleTv;
    public final TextView mBranddiscountMaxprice;
    public final TextView mBranddiscountData;
    public final LinearLayout mGotoTicketLayoutBtn;
    public final TextView mHomelimtBaomingTV;
    public final TextView mNumdiscountTV;
    public final TextView mBrandName;
    public final SimpleDraweeView mBrandicon;


    public HomeLimitnumViewHolder(View viewRoot) {
        mLatestExhibitionRl = (RelativeLayout) viewRoot.findViewById(R.id.latest_exhibition_rl);
        mLastestExhibitTiltetv = (TextView) viewRoot.findViewById(R.id.lastest_exhibit_tiltetv);
        mHomeCoverIv = (SimpleDraweeView) viewRoot.findViewById(R.id.home_coverIv);
        mHomeMygridview = (MyGridView) viewRoot.findViewById(R.id.home_mygridview);
        mHomeTitleTv = (TextView) viewRoot.findViewById(R.id.home_titleTv);
        mBranddiscountMaxprice = (TextView) viewRoot.findViewById(R.id.branddiscount_maxprice);
        mBranddiscountData = (TextView) viewRoot.findViewById(R.id.branddiscount_data);
        mGotoTicketLayoutBtn = (LinearLayout) viewRoot.findViewById(R.id.gotoTicket_layoutBtn);
        mHomelimtBaomingTV = (TextView) viewRoot.findViewById(R.id.homelimt_baomingTV);
        mNumdiscountTV = (TextView) viewRoot.findViewById(R.id.home_numdiscountTv);
        mBrandicon = (SimpleDraweeView) viewRoot.findViewById(R.id.brandicon);
        mBrandName = (TextView) viewRoot.findViewById(R.id.brand_name);
    }
}
