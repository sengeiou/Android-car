package com.tgf.kcwc.view;

import com.tgf.kcwc.mvp.model.CompleteServerModel;
import com.tgf.kcwc.mvp.model.YuyueBuyModel;
import com.tgf.kcwc.mvp.view.WrapView;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/2/27 0027
 * E-mail:hekescott@qq.com
 */

public interface CompleteServerView extends WrapView {

    void showHead(YuyueBuyModel completeServerModel);

    void showOrglist(List<YuyueBuyModel.OrgItem> orgItemList);
}
