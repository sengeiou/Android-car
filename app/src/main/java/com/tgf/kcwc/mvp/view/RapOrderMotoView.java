package com.tgf.kcwc.mvp.view;


import com.tgf.kcwc.mvp.model.Pagination;
import com.tgf.kcwc.mvp.model.RapBuymotoModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/3/9 0009
 * E-mail:hekescott@qq.com
 */

public interface RapOrderMotoView extends WrapView{

    void showPreBuyList(ArrayList<RapBuymotoModel.RapbuyItem> prebuyList);

    void showHead(Pagination pagination);
}
