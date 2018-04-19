package com.tgf.kcwc.mvp.view;

import android.webkit.WebView;

import com.tgf.kcwc.mvp.model.Account;

/**
 * Auther: Scott
 * Date: 2017/7/4 0004
 * E-mail:hekescott@qq.com
 */

public interface WXloginView extends WrapView {
   void showWxLoginSuccess(Account account);

   void bindPhoneNum(String string);

   void showLoginFailed(String statusMessage);

   void sendMsgSuccess();

   void sendMsgFailure(String statusMessage);

   void showRebindPhone(Account account);

   void showBindSuccess(Account account);

    void showReBindSuccess();
}
