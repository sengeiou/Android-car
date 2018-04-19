package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.CarOwnerSalerModel;
import com.tgf.kcwc.mvp.model.DaRenEvaluateModel;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.mvp.model.OrgModel;

import java.util.ArrayList;

/**
 * Author：Scott
 * Date:2016/12/8 21:06
 * E-mail：fishloveqin@gmail.com
 * 登录、注册View
 */

public interface MotordetailView extends BaseView {

    void showORGlist(ArrayList<OrgModel> organizationList);

    void failure(String msg);

    void showMotoDeatail(CarBean motodetail);

    void showDarenEvaluate(ArrayList<DaRenEvaluateModel> daRenEvaluateModelList);

    void showCarOwnerList(ArrayList<CarOwnerSalerModel> carOwnerSalerList);

//    void  showMotoOilMoto(Moto motodetail);
//
//    void  showMotoBatterryMoto(Moto motodetail);



}
