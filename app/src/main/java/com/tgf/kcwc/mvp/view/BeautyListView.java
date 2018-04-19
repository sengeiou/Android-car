package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.Beauty;
import com.tgf.kcwc.mvp.model.BeautyListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/17 0017
 * E-mail:hekescott@qq.com
 */

public interface BeautyListView extends WrapView {
    void showBeautylistView(ArrayList<BeautyListModel.BeautyBrand> beautylist);

    void showExhibitNameView(String exhibitName);

    void showBeautylistNomore(String msg);
}
