package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.ExhibitPlace;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/16 0016
 * E-mail:hekescott@qq.com
 */

public interface  ExhibitPlacelistView extends WrapView {
  void  showHeadView(String totalimage);
  void  showGridView(List<ExhibitPlace> exhibitPlaceList);
  void  showListView(List<ExhibitPlace> exhibitPlaceList);
}
