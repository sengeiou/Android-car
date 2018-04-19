package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.TripBookThreadModel;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/5/15 0015
 * E-mail:hekescott@qq.com
 */

public interface TripbookThreadView extends WrapView {
    void showThreadList(List<TripBookThreadModel.Thread> threadList);
}
