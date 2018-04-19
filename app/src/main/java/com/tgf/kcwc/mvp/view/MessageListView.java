package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.ApplyListBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.MessageListBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface MessageListView extends WrapView {

   void StatisticsListSucceed(MessageListBean messageListBean); //返回固定列表成功
   void PrivateletterListSucceed(MessageListBean messageListBean); //返回私有列表成功
   void ReadSucceed(BaseBean baseBean); //全部标记为已读
    void dataListDefeated(String msg); //列表数据返回失败
}
