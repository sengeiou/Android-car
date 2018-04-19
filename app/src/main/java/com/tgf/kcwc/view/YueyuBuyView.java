package com.tgf.kcwc.view;

import java.util.List;

import com.tgf.kcwc.mvp.model.YuyueBuyModel;
import com.tgf.kcwc.mvp.view.WrapView;

/**
 * Auther: Scott
 * Date: 2017/2/27 0027
 * E-mail:hekescott@qq.com
 */

public interface YueyuBuyView extends WrapView {

    void showHead(YuyueBuyModel completeServerModel);

    void showOrglist(List<YuyueBuyModel.OrgItem> orgItemList);

    void showCompleteSuccess();

    void showCompleteFailed();

    void showCloseSuccess();

    void showCloseFailed(String statusMessage);
}
