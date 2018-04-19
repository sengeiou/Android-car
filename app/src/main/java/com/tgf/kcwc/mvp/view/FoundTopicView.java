package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.FoundTypeBean;
import com.tgf.kcwc.mvp.model.TopicDetailsBean;
import com.tgf.kcwc.mvp.model.TopicDetailsListBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface FoundTopicView extends WrapView {

   void dataSucceed(BaseBean baseArryBean); //创建成功
   void typeListSucceed(FoundTypeBean topicBean); //总类型成功
   void typerListSucceed(FoundTypeBean topicBean); //子类型成功
    void dataListDefeated(String msg); //列表数据返回失败
}
