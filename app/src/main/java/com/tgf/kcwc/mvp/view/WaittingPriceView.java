package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.NewPriceModel;
import com.tgf.kcwc.mvp.model.WaittingPriceModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/22 0022
 * E-mail:hekescott@qq.com
 */

public interface WaittingPriceView extends  WrapView{
    void showOrgList(ArrayList<WaittingPriceModel.Org> orgArrayList);

    void showNewPrice(NewPriceModel data);

    void showTitle(WaittingPriceModel data);

    void cancelSuccess();

    void cancleFailed(String statusMessage);
}
