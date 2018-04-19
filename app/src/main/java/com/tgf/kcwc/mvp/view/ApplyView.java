package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.AppListBean;
import com.tgf.kcwc.mvp.model.ApplyListBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface ApplyView extends WrapView {

   void dataListSucceed(ApplyListBean applyListBean); //返回数据列表成功
    void dataListDefeated(String msg); //列表数据返回失败

   void dataFollowSucceed(int num,String follow_id,String msg); //关注成功

   void dataFollowRelationSucceed(int num,String relation); //查询状态成功

   void dataCheckSucceed(int num,int type); //处理状态成功
}
