package com.tgf.kcwc.mvp.view;

import java.util.ArrayList;
import java.util.List;

import com.tgf.kcwc.mvp.model.ExhibitPlace;
import com.tgf.kcwc.mvp.model.Exhibition;
import com.tgf.kcwc.mvp.model.ExhibitionDetailModel;
import com.tgf.kcwc.mvp.model.Image;

/**
 * Auther: Scott
 * Date: 2017/1/16 0016
 * E-mail:hekescott@qq.com
 */

public interface ExhibitDetailView extends WrapView {
    void showHead(Exhibition exhibition);

    void showMenu(List<ExhibitionDetailModel.MenuItem> menuItemList);

    void showBanner(ArrayList<Image> bannerList);

    void showPlaceList(ArrayList<ExhibitPlace> exhibitPlacelist);

    /**
     *
     * @param pinkImgs
     * @param type 1 本届 2上届 0不显示
     */
    void showPlink(ArrayList<String> pinkImgs, int type);

    void showJump();

    void showExhibitionList(ArrayList<Exhibition> exhibitionList);
}
