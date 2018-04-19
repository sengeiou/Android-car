package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.TopicBean;
import com.tgf.kcwc.mvp.model.TopicDetailsBean;
import com.tgf.kcwc.mvp.model.TopicDetailsListBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface TopicDetailsView extends WrapView {

   void dataSucceed(TopicDetailsBean topicDetailsBean); //返回详情成功
   void dataListSucceed(TopicDetailsListBean topicDetailsListBean); //返回列表成功
   void Attention(BaseArryBean BaseBean); //关注成功
   void AttentionDelete(BaseArryBean BaseBean); //取消关注成功
   void ApplyHostSucceed(BaseArryBean BaseBean); //申请成为主持人成功
    void dataListDefeated(String msg); //列表数据返回失败
}
