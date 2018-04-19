package com.tgf.kcwc.mvp.view;

import java.util.List;

import com.tgf.kcwc.mvp.model.GiftPackDetailModel;
import com.tgf.kcwc.mvp.model.LimitDiscountDetailModel;
import com.tgf.kcwc.mvp.model.OrgModel;

/**
 * Auther: Scott
 * Date: 2017/4/26 0026
 * E-mail:hekescott@qq.com
 */

public interface LimitDiscountDetailView extends WrapView{
    void showHead(LimitDiscountDetailModel giftPackDetailModel);

    void showGoodsList(List<LimitDiscountDetailModel.GiftCar> giftCars);
    void showOrgList(List<OrgModel> giftOrgs);
}
