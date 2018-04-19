package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.SeekBean;
import com.tgf.kcwc.mvp.model.SeekDetailsBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface SeekDetailsView extends WrapView {

   void dataListSucceed(SeekDetailsBean seekBean); //模糊查询数据列表成功
    void dataListDefeated(String msg); //列表数据返回失败
}
