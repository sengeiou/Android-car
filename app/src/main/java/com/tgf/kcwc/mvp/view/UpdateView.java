package com.tgf.kcwc.mvp.view;


import com.tgf.kcwc.mvp.model.UpdateModel;

/**
 * Auther: Scott
 * Date: 2017/7/11 0011
 * E-mail:hekescott@qq.com
 */

public interface UpdateView extends WrapView {
       void showUpdate(UpdateModel update);

    void showNoUpdate();
}
