package com.tgf.kcwc.mvp.view;


import com.tgf.kcwc.mvp.model.ScanOffDetailModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/25 0025
 * E-mail:hekescott@qq.com
 */

public interface ScanOffCouponDetailView extends WrapView {
  void  showScanOffList(ArrayList<ScanOffDetailModel.ScanOffDetailItem> scanOffDetailList);
}
