package com.tgf.kcwc.mvp.model;

/**
 * Created by Administrator on 2017/6/26.
 */

public class MorePopupwindowBean {
   public String title;
   public String type;
   public int id;
   public int icon;


   public MorePopupwindowBean( ) {

   }

   public MorePopupwindowBean(String title) {
      this.title = title;
   }
   public MorePopupwindowBean(String title, int icon) {
      this.title = title;
      this.icon = icon;
   }
   public MorePopupwindowBean(String title, int id, int icon) {
      this.title = title;
      this.id = id;
      this.icon = icon;
   }
}