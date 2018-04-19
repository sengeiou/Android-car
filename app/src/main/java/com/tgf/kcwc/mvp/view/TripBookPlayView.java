package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.TripBookModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/11/23 0023
 * E-mail:hekescott@qq.com
 */

public interface TripBookPlayView extends  WrapView {
    void showTripBookList(ArrayList<TripBookModel.TripBookModelItem> list);
}
