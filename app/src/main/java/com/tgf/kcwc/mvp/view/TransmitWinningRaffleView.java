package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.TransmWinningResultBean;
import com.tgf.kcwc.mvp.model.TransmitWinningDetailsBean;
import com.tgf.kcwc.mvp.model.TransmitWinningRaffleBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface TransmitWinningRaffleView extends WrapView {

    void dataListSucceed(TransmitWinningRaffleBean baseBean); //返回数据列表成功

    void dataForwardSucceed(TransmWinningResultBean BaseBean); //分享成功

    void dataListDefeated(String msg); //列表数据返回失败
}
