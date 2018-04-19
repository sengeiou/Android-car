package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.HelpCenterModel;

/**
 * @anthor noti
 * @time 2017/8/22
 * @describle
 */
public interface HelpCenterView extends WrapView{
    /**
     * 显示帮助中心列表
     * @param list
     */
    void showHelpList(HelpCenterModel list);
}
