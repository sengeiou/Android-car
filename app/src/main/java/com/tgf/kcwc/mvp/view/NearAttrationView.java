package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.NearAttractionModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/10 0010
 * E-mail:hekescott@qq.com
 */

public interface NearAttrationView extends WrapView {
       void showNearAttrationlist(ArrayList<NearAttractionModel.NearAttractionItem> list);
}
