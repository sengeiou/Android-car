package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.DiscountLimitModel;
import com.tgf.kcwc.mvp.model.GiftPackDetailModel;
import com.tgf.kcwc.mvp.model.OrgModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/4/26 0026
 * E-mail:hekescott@qq.com
 */

public interface GiftPackDetailView extends WrapView{
    void showHead(GiftPackDetailModel giftPackDetailModel);

    void showGoodsList(List<GiftPackDetailModel.GiftCar> giftCars);

    void showOrgList(ArrayList<OrgModel> giftOrgs);
}
