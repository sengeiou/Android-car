package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.TripBookModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/2 0002
 * E-mail:hekescott@qq.com
 */

public interface TripBookFindView extends WrapView {
    void showTripBookList(ArrayList<TripBookModel.TripBookModelItem> list);
}
