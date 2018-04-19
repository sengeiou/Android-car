package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.MySeecarMsgModel;
import com.tgf.kcwc.mvp.model.Pagination;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/4/18 0018
 * E-mail:hekescott@qq.com
 */

public interface MySeecarMsgView extends WrapView {
     void showMySeecarList(List<MySeecarMsgModel.OrderItem> mySeecarList);

    void showCount(Pagination pagination);
    void  showDeleteSuccess();
    void  showDeleteFailed();
}
