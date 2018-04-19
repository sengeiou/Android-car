package com.tgf.kcwc.view.chart;

import com.tgf.kcwc.mvp.model.TripBookMapModel;

import java.util.List;

/**
 * 巴掌
 * https://github.com/JeasonWong
 */

public class Line {

  //线条数据源集合
  private List<TripBookMapModel.NodeItem> mLineData;
  //线条颜色
  private int mLineColor = 0x36a95c;
  //线条宽度
  private float mLineWidth = 1f;

  public Line(List<TripBookMapModel.NodeItem> lineData) {
    mLineData = lineData;
  }

  public List<TripBookMapModel.NodeItem> getLineData() {
    return mLineData;
  }

  public int getLineColor() {
    return mLineColor;
  }

  public float getLineWidth() {
    return mLineWidth;
  }
}
