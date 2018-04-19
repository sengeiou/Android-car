package com.tgf.kcwc.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.view.HomePopWindow;
import com.tgf.kcwc.view.MyGridView;
import com.tgf.kcwc.view.home.CustomImageView;
import com.tgf.kcwc.view.home.NineGridlayout;

/**
 * Auther: Scott
 * Date: 2017/9/12 0012
 * E-mail:hekescott@qq.com
 */

public  class HomeExhibitViewHolder {
    public final SimpleDraweeView mHomeCoverIv;
    public final TextView mHomeTitleTv;
    public final TextView mHomeExhibitTimeTv;
    public final MyGridView mHomeExhibitMenuTv;
    public final TextView mHomeCreatePlaceTv;
    public final TextView mHomeExhibitBuyTv;
    public final View titleLayout;

    public HomeExhibitViewHolder(View viewRoot) {

        mHomeCoverIv = (SimpleDraweeView) viewRoot.findViewById(R.id.home_coverIv);
        titleLayout =  viewRoot.findViewById(R.id.latest_exhibition_rl);
        mHomeTitleTv = (TextView) viewRoot.findViewById(R.id.home_titleTv);
        mHomeExhibitTimeTv = (TextView) viewRoot.findViewById(R.id.home_exhibitTimeTv);
        mHomeExhibitMenuTv = (MyGridView) viewRoot.findViewById(R.id.home_exhibitMenuTv);
        mHomeCreatePlaceTv = (TextView) viewRoot.findViewById(R.id.home_createPlaceTv);
        mHomeExhibitBuyTv = (TextView) viewRoot.findViewById(R.id.home_exhibitBuyTv);
    }


}
