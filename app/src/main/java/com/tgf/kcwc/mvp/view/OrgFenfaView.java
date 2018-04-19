package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.OrgFenfaModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/10/18 0018
 * E-mail:hekescott@qq.com
 */

public interface OrgFenfaView extends WrapView {
    void showFenfaStatitics(ArrayList<OrgFenfaModel.UserInfo> list);
}
