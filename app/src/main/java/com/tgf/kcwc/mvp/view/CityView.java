package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.ApplyListBean;
import com.tgf.kcwc.mvp.model.CityBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface CityView extends WrapView {

   void dataListSucceed(CityBean cityBean); //返回数据列表成功
    void dataListDefeated(String msg); //列表数据返回失败
}
