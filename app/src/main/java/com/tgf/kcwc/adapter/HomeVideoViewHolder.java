package com.tgf.kcwc.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;

/**
 * Auther: Scott
 * Date: 2017/9/14 0014
 * E-mail:hekescott@qq.com
 */

public class HomeVideoViewHolder {
    public final SimpleDraweeView mHomeAvatarIv;
    public final ImageView mHomeGenderImg;
    public final TextView mHomeUserNameTv;
    public final ImageView mHomeIsModelIv;
    public final ImageView mHomeIsDarenIv;
    public final SimpleDraweeView mBrandLogo;
    public final TextView mHomeCreateTimeTv;
    public final ImageView mHomeDoMeIv;
    public final SimpleDraweeView mHomeCoverIv;
    public final ImageView mHomePlayBtnIv;
    public final TextView mHomeTitleTv;
    public final TextView mHomeCreatePlaceTv;
    public final TextView mHomeCreateDistanceTv;
    public final TextView mHomeVisitors;
    public final ImageView mHomeIsPraiseIv;
    public final TextView mHomePraiseNumTv;
    public final TextView mHomeCommentNumTv;

    public HomeVideoViewHolder(View viewRoot) {
        mHomeAvatarIv = (SimpleDraweeView) viewRoot.findViewById(R.id.home_avatarIv);
        mHomeGenderImg = (ImageView) viewRoot.findViewById(R.id.home_genderImg);
        mHomeUserNameTv = (TextView) viewRoot.findViewById(R.id.home_userNameTv);
        mHomeIsModelIv = (ImageView) viewRoot.findViewById(R.id.home_isModelIv);
        mHomeIsDarenIv = (ImageView) viewRoot.findViewById(R.id.home_isDarenIv);
        mBrandLogo = (SimpleDraweeView) viewRoot.findViewById(R.id.brandLogo);
        mHomeCreateTimeTv = (TextView) viewRoot.findViewById(R.id.home_createTimeTv);
        mHomeDoMeIv = (ImageView) viewRoot.findViewById(R.id.home_doMeIv);
        mHomeCoverIv = (SimpleDraweeView) viewRoot.findViewById(R.id.home_coverIv);
        mHomePlayBtnIv = (ImageView) viewRoot.findViewById(R.id.home_playBtnIv);
        mHomeTitleTv = (TextView) viewRoot.findViewById(R.id.home_titleTv);
        mHomeCreatePlaceTv = (TextView) viewRoot.findViewById(R.id.home_createPlaceTv);
        mHomeCreateDistanceTv = (TextView) viewRoot.findViewById(R.id.home_createDistanceTv);
        mHomeVisitors = (TextView) viewRoot.findViewById(R.id.home_visitors);
        mHomeIsPraiseIv = (ImageView) viewRoot.findViewById(R.id.home_isPraiseIv);
        mHomePraiseNumTv = (TextView) viewRoot.findViewById(R.id.home_praiseNumTv);
        mHomeCommentNumTv = (TextView) viewRoot.findViewById(R.id.home_commentNumTv);
    }
}
