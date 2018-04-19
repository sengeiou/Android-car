package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.MeCrunchiesBean;
import com.tgf.kcwc.mvp.model.RaffleRecordBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface MeRaffleRecordView extends WrapView {

    void dataListSucceed(MeCrunchiesBean meCrunchiesBean); //返回数据列表成功
    void dataListDefeated(String msg); //列表数据返回失败
}
