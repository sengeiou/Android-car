package com.tgf.kcwc.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.mvp.model.Image;
import com.tgf.kcwc.view.HomePopWindow;

/**
 * Auther: Scott
 * Date: 2017/9/12 0012
 * E-mail:hekescott@qq.com
 */

public class HomeHuoDongViewHolder {
    public final SimpleDraweeView avatarIv;
    public final ImageView genderImg;
    public final TextView mUserNameTv;
    public final ImageView mModelIv;
    public final ImageView mHotmanIv;
    public final SimpleDraweeView mBrandIv;
    public final TextView mCreateTimeTv;
    public final ImageView mDomoreIv;
    public final TextView mTypeTv;
    public final SimpleDraweeView mCoverIv;
    public final TextView mJoinNumTv;
    public final TextView mTitleTv;
    public final TextView mDirectionTv;
    public final TextView mTimeTv;
    public final LinearLayout mOrgLayout;
    public final SimpleDraweeView mOrgCoverIv;
    public final TextView mOrgNameTv;
    public final TextView mCreatePlaceTv;
    public final TextView mDistanceTv;
    public final TextView mVisitors;
    public final TextView mFocuson;
    public final TextView mComment;
    public final ImageView mIsFellow;
    public final HomePopWindow homePopWindow;
    public final View mCreatePlaceLayout;
    public final ImageView mHomeHuodonStatusIv;
    public HomeHuoDongViewHolder(View viewRoot) {
        avatarIv = (SimpleDraweeView) viewRoot.findViewById(R.id.home_huodong_avatarIv);
        genderImg = (ImageView) viewRoot.findViewById(R.id.genderImg);
        mHomeHuodonStatusIv = (ImageView) viewRoot.findViewById(R.id.homeview_state);

        mUserNameTv = (TextView) viewRoot.findViewById(R.id.homeHuodong_userNameTv);
        mModelIv = (ImageView) viewRoot.findViewById(R.id.homeHuodong_modelIv);
        mHotmanIv = (ImageView) viewRoot.findViewById(R.id.homeHuodong_hotmanIv);
        mBrandIv = (SimpleDraweeView) viewRoot.findViewById(R.id.homeHuodong_brandIv);
        mCreateTimeTv = (TextView) viewRoot.findViewById(R.id.homeHuodong_createTimeTv);
        mDomoreIv = (ImageView) viewRoot.findViewById(R.id.homeHuodong_domoreIv);
        mTypeTv = (TextView) viewRoot.findViewById(R.id.homeHuodong_typeTv);
        mCoverIv = (SimpleDraweeView) viewRoot.findViewById(R.id.homeHuodong_coverIv);
        mJoinNumTv = (TextView) viewRoot.findViewById(R.id.homeHuodong_joinNumTv);
        mTitleTv = (TextView) viewRoot.findViewById(R.id.homeHuodong_titleTv);
        mDirectionTv = (TextView) viewRoot.findViewById(R.id.homeHuodong_directionTv);
        mTimeTv = (TextView) viewRoot.findViewById(R.id.homeHuodong_timeTv);
        mOrgLayout = (LinearLayout) viewRoot.findViewById(R.id.homeHuodong_orgLayout);
        mOrgCoverIv = (SimpleDraweeView) viewRoot.findViewById(R.id.homeHuodong_orgCoverIv);
        mOrgNameTv = (TextView) viewRoot.findViewById(R.id.homeHuodong_orgNameTv);
        mCreatePlaceTv = (TextView) viewRoot.findViewById(R.id.homeHuodong_createPlaceTv);
        mDistanceTv = (TextView) viewRoot.findViewById(R.id.homeHuodong_distanceTv);
        mVisitors = (TextView) viewRoot.findViewById(R.id.homeHuodong_visitors);
        mFocuson = (TextView) viewRoot.findViewById(R.id.homeHuodong_focuson);
        mComment = (TextView) viewRoot.findViewById(R.id.homeHuodong_comment);
        mIsFellow = (ImageView) viewRoot.findViewById(R.id.homeHuodong_islikeIv);
        mCreatePlaceLayout = viewRoot.findViewById(R.id.home_createPlaceLayout);
        homePopWindow = new HomePopWindow(viewRoot.getContext());
    }

}
