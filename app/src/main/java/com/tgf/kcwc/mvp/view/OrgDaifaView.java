package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.OrgDaifaModel;
import com.tgf.kcwc.mvp.model.OrgFenfaModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/10/18 0018
 * E-mail:hekescott@qq.com
 */

public interface OrgDaifaView extends WrapView {
    void showFenfaStatitics(ArrayList<OrgDaifaModel.UserInfo> list);
}
