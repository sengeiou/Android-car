package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.AppListBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.TransmitWinningHomeListBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface TransmitWinningHomeView extends WrapView {

   void dataListSucceed(TransmitWinningHomeListBean transmitWinningHomeListBean); //返回数据列表成功
    void dataListDefeated(String msg); //列表数据返回失败
}
