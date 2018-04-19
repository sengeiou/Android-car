package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.AppListBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.StoreBelowBean;
import com.tgf.kcwc.mvp.model.StreBelowAmendBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface StoreBelowView extends WrapView {

    void dataSucceed(StoreBelowBean storeBelowBean); //数据返回成功
    void revocationSucceed(StreBelowAmendBean baseBean, int num); //撤销成功
    void releaseSucceed(StreBelowAmendBean baseBean,int num); //发布成功

    void dataListDefeated(String msg); //列表数据返回失败
}
