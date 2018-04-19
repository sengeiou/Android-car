package com.tgf.kcwc.mvp.view;


import com.tgf.kcwc.mvp.model.IRapOrderModel;
import com.tgf.kcwc.mvp.model.RapOrderPostModel;

/**
 * Auther: Scott
 * Date: 2017/3/10 0010
 * E-mail:hekescott@qq.com
 */

public interface IrapOrderDetailView extends WrapView {
  void  showRapDetail(IRapOrderModel iRapOrderModel);

    void showPostSuccess(RapOrderPostModel data);

  void showPostFailed(String statusMessage);
}
