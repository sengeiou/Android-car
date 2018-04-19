package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.ExhibitEvent;

import java.util.List;


/**
 * Auther: Scott
 * Date: 2017/1/18 0018
 * E-mail:hekescott@qq.com
 */

public  interface EventListView extends WrapView {
    void showEventList(List<ExhibitEvent> exhibitEventList);

    void showNomore(String msg);
}
