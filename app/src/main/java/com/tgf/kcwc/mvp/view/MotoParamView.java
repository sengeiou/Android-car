package com.tgf.kcwc.mvp.view;

import java.util.List;

import com.tgf.kcwc.mvp.model.MotoParamTitleItem;

/**
 * Author：Jenny
 * Date:2016/12/13 09:20
 * E-mail：fishloveqin@gmail.com
 */

public interface MotoParamView extends WrapView {

   void showData(List<MotoParamTitleItem> datas);

}
