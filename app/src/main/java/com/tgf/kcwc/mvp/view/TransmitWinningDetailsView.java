package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.TransmitDrawSucceedBean;
import com.tgf.kcwc.mvp.model.TransmitWinningDetailsBean;
import com.tgf.kcwc.mvp.model.TransmitWinningHomeListBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface TransmitWinningDetailsView extends WrapView {

    void dataListSucceed(TransmitWinningDetailsBean transmitWinningHomeListBean); //返回数据列表成功

    void dataForwardSucceed(TransmitDrawSucceedBean BaseBean); //分享成功

    void dataListDefeated(String msg); //列表数据返回失败
}
