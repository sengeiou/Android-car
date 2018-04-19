package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.HomeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/5/15 0015
 * E-mail:hekescott@qq.com
 */

public interface HomePageView extends WrapView {
    void showBannerView(List<BannerModel.Data> data);

    void showHomeList(ArrayList<HomeModel.HomeModelItem> homeList);

    void showCommitOrderSuccess(int orderId);

    void showCommitOrderFailed(String statusMessage);

    void showFollowAddFailed(String msg,int position);

    void showFollowAddSuccess(int position);

    void showCancelSuccess(int position);

    void showCancelFailed(String msg);

    void showPraiseFailed(String statusMessage);

    void showPraiseSuccess(int position);

    void showShare(int position);

    void showDeletSucess(int position);
}
