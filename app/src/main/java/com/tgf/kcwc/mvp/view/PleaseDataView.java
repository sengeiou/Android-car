package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.PleasePlayModel;

/**
 * Created by Administrator on 2017/4/17.
 */

public interface PleaseDataView extends WrapView {

    void dataListSucceed(PleasePlayModel pleasePlayModel); //列表数据返回成功

    void dataListDefeated(String msg); //列表数据返回失败

    void dataBannerSucceed(BannerModel bannerModel); //banner数据返回成功

    void dataBannerDefeated(String msg); //banner数据返回失败


}
