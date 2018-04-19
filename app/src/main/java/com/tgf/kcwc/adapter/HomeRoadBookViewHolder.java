package com.tgf.kcwc.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;

/**
 * Auther: Scott
 * Date: 2017/9/14 0014
 * E-mail:hekescott@qq.com
 */

public class HomeRoadBookViewHolder {
    public final SimpleDraweeView mHomeAvatarIv;
    public final ImageView mGenderImg;
    public final TextView mNametv;
    public final ImageView mCommentModelTv;
    public final ImageView mCommentPopmanTv;
    public final SimpleDraweeView mBrandLogo;
    public final SimpleDraweeView mHomeCoverIv;
    public final TextView mTripbookNumtv;
    public final View mTripBookLayout;
    public final TextView mTripbookSpeedmaxtv;
    public final TextView mTripbookSpeedavgtv;
    public final TextView mTripbookAltitudetv;
    public final TextView mTripbookMileage;
    public final TextView mTripbookActualtimetv;
    public final TextView mHomeTitleTv;
    public final TextView mHomeCreatePlaceTv;
    public final TextView mHomeCreateDistanceTv;
    public final TextView mHomeVisitors;
    public final ImageView mHomeIsPraiseIv;
    public final TextView mHomeFocuson;
    public final TextView mHomeComment;
    public final TextView homeCreateTimeTv;


    public HomeRoadBookViewHolder(View viewRoot) {
        mHomeAvatarIv = (SimpleDraweeView) viewRoot.findViewById(R.id.home_avatarIv);
        mGenderImg = (ImageView) viewRoot.findViewById(R.id.genderImg);
        mNametv = (TextView) viewRoot.findViewById(R.id.nametv);
        mCommentModelTv = (ImageView) viewRoot.findViewById(R.id.comment_model_tv);
        mCommentPopmanTv = (ImageView) viewRoot.findViewById(R.id.comment_popman_tv);
        mBrandLogo = (SimpleDraweeView) viewRoot.findViewById(R.id.brandLogo);
        mHomeCoverIv = (SimpleDraweeView) viewRoot.findViewById(R.id.home_coverIv);
        mTripbookNumtv = (TextView) viewRoot.findViewById(R.id.tripbook_numtv);
        mTripbookSpeedmaxtv = (TextView) viewRoot.findViewById(R.id.tripbook_speedmaxtv);
        mTripbookSpeedavgtv = (TextView) viewRoot.findViewById(R.id.tripbook_speedavgtv);
        mTripbookAltitudetv = (TextView) viewRoot.findViewById(R.id.tripbook_altitudetv);
        mTripbookMileage = (TextView) viewRoot.findViewById(R.id.tripbook_mileage);
        mTripbookActualtimetv = (TextView) viewRoot.findViewById(R.id.tripbook_actualtimetv);
        mHomeTitleTv = (TextView) viewRoot.findViewById(R.id.home_titleTv);
        mHomeCreatePlaceTv = (TextView) viewRoot.findViewById(R.id.home_createPlaceTv);
        mHomeCreateDistanceTv = (TextView) viewRoot.findViewById(R.id.home_createDistanceTv);
        mHomeVisitors = (TextView) viewRoot.findViewById(R.id.home_visitors);
        mHomeIsPraiseIv = (ImageView) viewRoot.findViewById(R.id.home_isPraiseIv);
        mHomeFocuson = (TextView) viewRoot.findViewById(R.id.home_focuson);
        mHomeComment = (TextView) viewRoot.findViewById(R.id.home_comment);
        homeCreateTimeTv = (TextView) viewRoot.findViewById(R.id.home_createTimeTv);
        mTripBookLayout = viewRoot.findViewById(R.id.layou_item_recom);
    }
}
