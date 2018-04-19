package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.PlayListBean;
import com.tgf.kcwc.mvp.model.PlayTopicBean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public interface PlayDataView extends WrapView {

    void dataBannerSucceed(BannerModel bannerModel); //banner数据返回成功

    void dataBannerDefeated(String msg); //banner数据返回失败

    void dataListfeated(PlayListBean playListBean); //list数据返回成功

    void dataTopicSucceed(List<PlayTopicBean> playListBean); //话题数据返回成功


}
