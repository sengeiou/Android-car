package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.IntegralListBean;
import com.tgf.kcwc.mvp.model.IntegralUserinfoBean;
import com.tgf.kcwc.mvp.model.OrgIntegralListBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface OrgIntegralView extends WrapView {

    void  DatalistSucceed(OrgIntegralListBean integralListBean); //返回数据列表成功

    void dataListDefeated(String msg); //列表数据返回失败
}
