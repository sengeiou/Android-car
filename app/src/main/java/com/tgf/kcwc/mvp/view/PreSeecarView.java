package com.tgf.kcwc.mvp.view;


import com.tgf.kcwc.mvp.model.Pagination;
import com.tgf.kcwc.mvp.model.PreSeecarModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/3/9 0009
 * E-mail:hekescott@qq.com
 */

public interface PreSeecarView extends WrapView {

    void showPreBuylist(ArrayList<PreSeecarModel.PreSeecarItem> preBuyItemList);

    void showListCount(Pagination pagination);
}
