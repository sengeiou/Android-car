package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.HelpCenterDetailModel;

/**
 * @anthor noti
 * @time 2017/8/22
 * @describle
 */
public interface HelpCenterDetailView extends WrapView{
    /**
     * 显示帮助中心详情
     * @param detailModel
     */
    void showHelpCenterDetail(HelpCenterDetailModel detailModel);
}
