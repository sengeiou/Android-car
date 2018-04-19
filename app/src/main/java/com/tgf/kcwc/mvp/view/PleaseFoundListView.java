package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.FoundListBean;
import com.tgf.kcwc.mvp.model.PleaseFoundListBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface PleaseFoundListView extends WrapView {

   void dataListSucceed(PleaseFoundListBean foundListBean); //返回数据列表成功
   void dataSucceed(BaseBean baseBean); //取消活动成功
    void dataListDefeated(String msg); //列表数据返回失败
}
