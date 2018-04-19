package com.tgf.kcwc.mvp.view;

import java.util.List;

import com.tgf.kcwc.mvp.model.CarModelPicsModel;

/**
 * Auther: Scott
 * Date: 2017/1/20 0020
 * E-mail:hekescott@qq.com
 */

public interface MotoDetailPicsView  extends WrapView{
    /**
     * 整车
     * @param wholecarImglist
     */
    void showWholeCar(List<CarModelPicsModel.CarModelPic> wholecarImglist);
    /**
     * 细节
     * @param interiorImglist
     */
    void showInterior(List<CarModelPicsModel.CarModelPic> interiorImglist);
    /**
     * 现场
     * @param livingImglist
     */
    void showLiving(List<CarModelPicsModel.CarModelPic> livingImglist);
}
