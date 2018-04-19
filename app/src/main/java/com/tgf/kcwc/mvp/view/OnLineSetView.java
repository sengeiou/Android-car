package com.tgf.kcwc.mvp.view;

/**
 * Auther: Scott
 * Date: 2017/9/5 0005
 * E-mail:hekescott@qq.com
 */

public interface OnLineSetView  extends WrapView{
   void showSalerSetSuccess();
   void showSalerSetFailed();

   void showsIsOnline(boolean isOnline);
}
