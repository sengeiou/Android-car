package com.tgf.kcwc.mvp.view;



import com.tgf.kcwc.mvp.model.ScanOffListModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/24 0024
 * E-mail:hekescott@qq.com
 */

public interface ScanOffCouponView extends WrapView {
    void showScanOffList(ArrayList<ScanOffListModel.ScanOffItem> list);

    void showsIsOnline(boolean isOnline);

    void showSalerSetSuccess();
}
