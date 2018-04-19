package com.tgf.kcwc.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.view.HomePopWindow;
import com.tgf.kcwc.view.home.CustomImageView;
import com.tgf.kcwc.view.home.NineGridlayout;

/**
 * Auther: Scott
 * Date: 2017/9/12 0012
 * E-mail:hekescott@qq.com
 */

public  class HomeSayViewHolder  extends BaseGuanzhuHolder{
    public final SimpleDraweeView mAvatarIv;
    public final ImageView mGenderImg;
    public final TextView mUsernameTv;
    public final ImageView mModelTv;
    public final ImageView mPopmanTv;
    public final SimpleDraweeView mBrandLogoIv;
    public final TextView mCreateTimeTv;
    public final TextView mTitletv;
    public final RelativeLayout mTopicLayout;
    public final SimpleDraweeView mTopicCoverIv;
    public final TextView mTopincTagTv;
    public final TextView mTopicFellowNumTv;
    public final TextView mCreatePlaceTv;
    public final TextView mCreateDistanceTv;
    public final TextView mVisitors;
    public final ImageView mIsPraiseIv;
    public final TextView mPraiseNumTv;
    public final TextView mComment;
    public  final CustomImageView oneIv;
    public  final NineGridlayout nineGrid;

    public final View mCreatePlaceLayout;

    public  HomeSayViewHolder(View viewRoot) {
        nineGrid = (NineGridlayout) viewRoot.findViewById(R.id.homesay_ninegrid);
        oneIv = (CustomImageView) viewRoot.findViewById(R.id.homesay_oneiv);
//        titleTv = (TextView) viewRoot.findViewById(R.id.homesay_titletv);

        mAvatarIv = (SimpleDraweeView) viewRoot.findViewById(R.id.homeSay_avatarIv);
        mGenderImg = (ImageView) viewRoot.findViewById(R.id.genderImg);
        mUsernameTv = (TextView) viewRoot.findViewById(R.id.homeSay_usernameTv);
        mModelTv = (ImageView) viewRoot.findViewById(R.id.homeSay_model_tv);
        mPopmanTv = (ImageView) viewRoot.findViewById(R.id.homeSay_popman_tv);
        mBrandLogoIv = (SimpleDraweeView) viewRoot.findViewById(R.id.homeSay_brandLogoIv);
        mCreateTimeTv = (TextView) viewRoot.findViewById(R.id.homeSay_createTimeTv);
        mMeDoIv = (ImageView) viewRoot.findViewById(R.id.homeSay_meDoIv);
        mAddFellowIv = (ImageView) viewRoot.findViewById(R.id.homeSay_addFellowIv);
        mTitletv = (TextView) viewRoot.findViewById(R.id.homesay_titletv);
        mTopicLayout = (RelativeLayout) viewRoot.findViewById(R.id.homesay_topicLayout);
        mTopicCoverIv = (SimpleDraweeView) viewRoot.findViewById(R.id.homesay_topicCoverIv);
        mTopincTagTv = (TextView) viewRoot.findViewById(R.id.homesay_topincTagTv);
        mTopicFellowNumTv = (TextView) viewRoot.findViewById(R.id.homesay_topicFellowNumTv);
        mCreatePlaceTv = (TextView) viewRoot.findViewById(R.id.homesay_createPlaceTv);
        mCreatePlaceLayout = viewRoot.findViewById(R.id.home_createplaceLayout);
        mCreateDistanceTv = (TextView) viewRoot.findViewById(R.id.homesay_createDistanceTv);
        mVisitors = (TextView) viewRoot.findViewById(R.id.homeSay_visitors);
        mIsPraiseIv = (ImageView) viewRoot.findViewById(R.id.homeSay_isPraiseIv);
        mPraiseNumTv = (TextView) viewRoot.findViewById(R.id.homeSay_praiseNumTv);
        mComment = (TextView) viewRoot.findViewById(R.id.homeSay_comment);
        homePopWindow = new HomePopWindow(viewRoot.getContext());
    }


}
