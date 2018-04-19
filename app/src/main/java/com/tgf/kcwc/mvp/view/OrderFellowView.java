package com.tgf.kcwc.mvp.view;


import com.tgf.kcwc.mvp.model.YuyueBuyModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/3/7 0007
 * E-mail:hekescott@qq.com
 */

public interface OrderFellowView extends WrapView {
     void showOrgList(ArrayList<YuyueBuyModel.OrgItem> offer_list);
     void showPostDataFailed(String statusMessage);
    void showNoDisturbSuccess(String msg);
    void showCompleteSuccess(String msg);
}
