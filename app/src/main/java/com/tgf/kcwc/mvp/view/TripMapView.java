package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.TripBookMapModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/17 0017
 * E-mail:hekescott@qq.com
 */

public interface TripMapView extends WrapView{
    void showTripMapdetail(ArrayList<TripBookMapModel.MapItem> mapItemList);

    void showTripNodeList(ArrayList<TripBookMapModel.NodeItem> nodeList);
}
