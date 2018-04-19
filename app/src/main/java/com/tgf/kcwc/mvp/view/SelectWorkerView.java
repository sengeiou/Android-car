package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.ContactUser;
import com.tgf.kcwc.mvp.model.WorkeModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/10/20 0020
 * E-mail:hekescott@qq.com
 */

public interface SelectWorkerView extends WrapView{
    void showWorkerList(ArrayList<ContactUser> workeModellist);
}
