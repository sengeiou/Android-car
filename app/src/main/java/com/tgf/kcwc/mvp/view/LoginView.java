package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.Account;

/**
 * Author：Jenny
 * Date:2016/12/8 21:06
 * E-mail：fishloveqin@gmail.com
 * 登录、注册View
 */

public interface LoginView extends WrapView {

    void success(Account a);

    void failure(String msg);

    void sendMsgSuccess();
}
