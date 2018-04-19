package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.TopicDetailsBean;
import com.tgf.kcwc.mvp.model.TopicDetailsListBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface CompileTopicDetailsView extends WrapView {

   void dataSucceed(TopicDetailsBean topicDetailsBean); //返回详情成功
   void dataListSucceed(TopicDetailsListBean topicDetailsListBean); //返回列表成功
   void UpdateSucceed(BaseArryBean baseArryBean); //修改数据成功
   void TopSucceed(BaseArryBean baseArryBean); //修改数据成功
    void dataListDefeated(String msg); //列表数据返回失败
}
