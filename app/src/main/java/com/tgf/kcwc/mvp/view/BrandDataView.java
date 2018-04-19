package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.Brand;

import java.util.List;

/**
 * Author：Jenny
 * Date:2016/12/14 14:41
 * E-mail：fishloveqin@gmail.com
 */

public interface BrandDataView extends WrapView {

    public void showData(List<Brand> data);
}
