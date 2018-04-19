package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.AppListBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.PleaseAppBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface PleaseApplyListView extends WrapView {

   void dataListSucceed(PleaseAppBean appListBean); //返回数据列表成功
   void dataSucceed(BaseBean baseBean); //返回数据列表成功
    void dataListDefeated(String msg); //列表数据返回失败
}
