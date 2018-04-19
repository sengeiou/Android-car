package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.TripAroundTongxModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/16 0016
 * E-mail:hekescott@qq.com
 */

public interface TripAroundTongXView extends WrapView {
    void showTongXlist(ArrayList<TripAroundTongxModel.TongX> tongxList);
}
