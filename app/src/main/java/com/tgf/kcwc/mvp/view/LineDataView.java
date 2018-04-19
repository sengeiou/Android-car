package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.LineDataModel;

/**
 * @anthor noti
 * @time 2017/10/16
 * @describle
 */
public interface LineDataView extends WrapView{
    /**
     * 显示线路数据
     * @param model
     */
    void showLineData(LineDataModel model);
}
