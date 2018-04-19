package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.AppListBean;
import com.tgf.kcwc.mvp.model.TopicBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface TopicListView extends WrapView {

   void dataListSucceed(TopicBean topicBean); //返回数据列表成功
    void dataListDefeated(String msg); //列表数据返回失败
}
