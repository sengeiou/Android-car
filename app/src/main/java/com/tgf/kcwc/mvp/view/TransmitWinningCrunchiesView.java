package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.TransmitWinningCrunchiesBean;
import com.tgf.kcwc.mvp.model.TransmitWinningDetailsBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface TransmitWinningCrunchiesView extends WrapView {

   void dataListSucceed(TransmitWinningCrunchiesBean transmitWinningHomeListBean); //返回数据列表成功
    void dataListDefeated(String msg); //列表数据返回失败
}
