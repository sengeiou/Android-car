package com.tgf.kcwc.adapter;

import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.view.MyGridView;

/**
 * Auther: Scott
 * Date: 2017/9/12 0012
 * E-mail:hekescott@qq.com
 */

public  class HomeStoreViewHolder {
    public final TextView mHomeTitleTv;
    public final SimpleDraweeView mHomestorePiconeiv;
    public final SimpleDraweeView mHomestorePicTwoiv;
    public final TextView mHomestoreOutcolor;
    public final TextView mHomestoreIncolor;
    public final SimpleDraweeView mHomeStoreCoverIv;
    public final TextView mHomeStoreNameTv;
    public final TextView mHomeStorLocTv;
    public final View orgCallBtn;
    public final View homeOrglayout;
    public final View titleLayout;

    public HomeStoreViewHolder(View viewRoot) {

        mHomeTitleTv = (TextView) viewRoot.findViewById(R.id.home_titleTv);
        titleLayout = viewRoot.findViewById(R.id.latest_exhibition_rl);
        mHomestorePiconeiv = (SimpleDraweeView) viewRoot.findViewById(R.id.homestore_piconeiv);
        mHomestorePicTwoiv = (SimpleDraweeView) viewRoot.findViewById(R.id.homestore_picTwoiv);
        mHomestoreOutcolor = (TextView) viewRoot.findViewById(R.id.homestore_outcolor);
        mHomestoreIncolor = (TextView) viewRoot.findViewById(R.id.homestore_incolor);
        mHomeStoreCoverIv = (SimpleDraweeView) viewRoot.findViewById(R.id.home_storeCoverIv);
        mHomeStoreNameTv = (TextView) viewRoot.findViewById(R.id.home_storeNameTv);
        mHomeStorLocTv = (TextView) viewRoot.findViewById(R.id.home_storLocTv);
        orgCallBtn = viewRoot.findViewById(R.id.home_orgcallBtn);
        homeOrglayout = viewRoot.findViewById(R.id.home_orglayout);
    }


}
