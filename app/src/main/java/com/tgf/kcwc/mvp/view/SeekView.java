package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.SeekBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface SeekView extends WrapView {

   void dataListSucceed(SeekBean appListBean); //模糊查询数据列表成功
   void dataSucceed(SeekBean appListBean); //模糊查询数据列表成功
    void dataListDefeated(String msg); //列表数据返回失败
}
