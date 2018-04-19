package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.ExhibitionEventPicsModel;

/**
 * Auther: Scott
 * Date: 2017/1/19 0019
 * E-mail:hekescott@qq.com
 */

public interface ExhibitionDataView<T> extends WrapView {

    void  showData(T t);
}
