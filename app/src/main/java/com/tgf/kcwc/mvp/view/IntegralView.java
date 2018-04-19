package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.AppListBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.IntegralListBean;
import com.tgf.kcwc.mvp.model.IntegralUserinfoBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface IntegralView extends WrapView {

    void  userDataSucceed(IntegralUserinfoBean integralUserinfoBean); //返回用户数据成功
    void  DatalistSucceed(IntegralListBean integralListBean); //返回数据列表成功

    void dataListDefeated(String msg); //列表数据返回失败
}
