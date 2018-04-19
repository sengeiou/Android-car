package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.PopmanEslistModel;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/4/6 0006
 * E-mail:hekescott@qq.com
 */

public interface PopmanEsListView extends WrapView {
    void showPopmanEsList(List<PopmanEslistModel.PopmanEsItem> popmanEsList);
}
