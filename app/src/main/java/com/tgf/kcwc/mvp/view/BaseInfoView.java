package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BaseInfoModel;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/16
 * @describle
 */
public interface BaseInfoView extends WrapView{
    /**
     * 显示基本信息
     * @param list
     */
    void showBaseInfo(BaseInfoModel list);

    /**
     * 显示认证信息
     * @param authItem
     */
    void showAuth(ArrayList<BaseInfoModel.AuthItem> authItem);
}
