package com.tgf.kcwc.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.view.HomePopWindow;

/**
 * Auther: Scott
 * Date: 2017/9/14 0014
 * E-mail:hekescott@qq.com
 */

public class HomeSalecarViewHolder  extends BaseGuanzhuHolder{
    public final SimpleDraweeView mHomeAvatarIv;
    public final ImageView mHomeGenderImg;
    public final TextView mHomeUserNameTv;
    public final ImageView mHomeIsModelIv;
    public final ImageView mHomeIsDarenIv;
    public final SimpleDraweeView mHomeBrandLogoIv;
    public final TextView mHomeCreateTimeTv;
    public final TextView mHomeTitleTv;
    public final SimpleDraweeView mHomeCoverIv;
    public final TextView mHomeSalerTitleTv;
    public final TextView mHomeSalerPriceTv;
    public final LinearLayout mHomeSalerExhibitLayout;
    public final TextView mHomeSalerExihibitTimeTv;
    public final TextView mHomeSalerExihibitPaTv;
    public final TextView mHomeCreatePlaceTv;
    public final TextView mHomeSalerGoTicketBtn;
    public final TextView mHomeVisitorsNumTv;
    public final ImageView mHomeIsPraiseIv;
    public final TextView mHomePraiseNumIv;
    public final TextView mHomeCommentNumTv;

    public HomeSalecarViewHolder(View viewRoot) {
        mHomeAvatarIv = (SimpleDraweeView) viewRoot.findViewById(R.id.home_avatarIv);
        mHomeGenderImg = (ImageView) viewRoot.findViewById(R.id.home_genderImg);
        mHomeUserNameTv = (TextView) viewRoot.findViewById(R.id.home_userNameTv);
        mHomeIsModelIv = (ImageView) viewRoot.findViewById(R.id.home_isModelIv);
        mHomeIsDarenIv = (ImageView) viewRoot.findViewById(R.id.home_isDarenIv);
        mHomeBrandLogoIv = (SimpleDraweeView) viewRoot.findViewById(R.id.home_brandLogoIv);
        mHomeCreateTimeTv = (TextView) viewRoot.findViewById(R.id.home_createTimeTv);
        mHomeTitleTv = (TextView) viewRoot.findViewById(R.id.home_titleTv);
        mHomeCoverIv = (SimpleDraweeView) viewRoot.findViewById(R.id.home_coverIv);
        mHomeSalerTitleTv = (TextView) viewRoot.findViewById(R.id.homeSaler_titleTv);
        mHomeSalerPriceTv = (TextView) viewRoot.findViewById(R.id.homeSaler_priceTv);
        mHomeSalerExhibitLayout = (LinearLayout) viewRoot.findViewById(R.id.homeSaler_exhibitLayout);
        mHomeSalerExihibitTimeTv = (TextView) viewRoot.findViewById(R.id.homeSaler_exihibitTimeTv);
        mHomeSalerExihibitPaTv = (TextView) viewRoot.findViewById(R.id.homeSaler_exihibitPaTv);
        mHomeCreatePlaceTv = (TextView) viewRoot.findViewById(R.id.home_createPlaceTv);
        mHomeSalerGoTicketBtn = (TextView) viewRoot.findViewById(R.id.homeSaler_goTicketBtn);
        mHomeVisitorsNumTv = (TextView) viewRoot.findViewById(R.id.home_visitorsNumTv);
        mHomeIsPraiseIv = (ImageView) viewRoot.findViewById(R.id.home_isPraiseIv);
        mHomePraiseNumIv = (TextView) viewRoot.findViewById(R.id.home_praiseNumIv);
        mHomeCommentNumTv = (TextView) viewRoot.findViewById(R.id.home_commentNumTv);
        mMeDoIv = viewRoot.findViewById(R.id.homeSaler_meDoIv);
        mAddFellowIv = viewRoot.findViewById(R.id.homeSaler_addFellowIv);
        homePopWindow = new HomePopWindow(viewRoot.getContext());
    }
}
