package com.tgf.kcwc.adapter;

import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.view.nestlistview.NestFullListView;

/**
 * Auther: Scott
 * Date: 2017/9/12 0012
 * E-mail:hekescott@qq.com
 */

public  class HomeNewcarViewHolder {
    public final SimpleDraweeView mHomeCoverIv;
    public final SimpleDraweeView mHomenewCarCoveriv;
    public final TextView mHomenewCarTitleTv;
    public final TextView mHomenewCarAddressTv;
    public final TextView mHomenewCarTimeTv;
    public final TextView mHomenewCarNameTv;
    public final TextView mHomenewCarStarTv;
    public final TextView mHomenewCarBugTicketTv;

    public HomeNewcarViewHolder(View viewRoot) {
        mHomeCoverIv = (SimpleDraweeView) viewRoot.findViewById(R.id.home_coverIv);
        mHomenewCarCoveriv = (SimpleDraweeView) viewRoot.findViewById(R.id.homenewCar_coveriv);
        mHomenewCarTitleTv = (TextView) viewRoot.findViewById(R.id.homenewCar_titleTv);
        mHomenewCarAddressTv = (TextView) viewRoot.findViewById(R.id.homenewCar_addressTv);
        mHomenewCarTimeTv = (TextView) viewRoot.findViewById(R.id.homenewCar_timeTv);
        mHomenewCarNameTv = (TextView) viewRoot.findViewById(R.id.homenewCar_nameTv);
        mHomenewCarStarTv = (TextView) viewRoot.findViewById(R.id.homenewCar_starTv);
        mHomenewCarBugTicketTv = (TextView) viewRoot.findViewById(R.id.homenewCar_bugTicketTv);
    }


}
