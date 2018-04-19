package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.Exhibition;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/13 0013
 * E-mail:hekescott@qq.com
 */

public interface ExhibitionListView extends WrapView {
    void showExhibitionList(List<Exhibition> exhibitionList);

}
