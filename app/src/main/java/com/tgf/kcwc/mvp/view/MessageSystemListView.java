package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.MessageListBean;
import com.tgf.kcwc.mvp.model.MessagePrivateListBean;
import com.tgf.kcwc.mvp.model.MessageSystemListBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface MessageSystemListView extends WrapView {

    void StatisticsListSucceed(MessageSystemListBean messageListBean); //返回固定列表成功

    void PrivateListSucceed(MessagePrivateListBean messageListBean); //返回固定列表成功

    void DeleteSucceed(BaseArryBean baseBean); //删除成功

    void dataListDefeated(String msg); //列表数据返回失败
}
