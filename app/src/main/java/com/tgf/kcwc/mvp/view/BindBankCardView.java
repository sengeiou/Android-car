package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BankCardModel;

/**
 * Author:Jenny
 * Date:2017/10/18
 * E-mail:fishloveqin@gmail.com
 */

public interface BindBankCardView extends WrapView{


    public  void  loadCardInfo(BankCardModel model);

    public void   bindCard(boolean isSuccess);
}
