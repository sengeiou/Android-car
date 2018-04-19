package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.RaffleRecordBean;
import com.tgf.kcwc.mvp.model.TransmitWinningDetailsBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface TransmitWinningRaffleRecordView extends WrapView {

    void dataListSucceed(RaffleRecordBean raffleRecordBean); //返回数据列表成功
    void dataListDefeated(String msg); //列表数据返回失败
}
