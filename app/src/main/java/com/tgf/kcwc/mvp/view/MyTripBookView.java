package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.TripBookModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/4/28 0028
 * E-mail:hekescott@qq.com
 */

public interface MyTripBookView extends WrapView {
    void showMyBooklist(ArrayList<TripBookModel.TripBookModelItem> tripBookModelItemList);
}
