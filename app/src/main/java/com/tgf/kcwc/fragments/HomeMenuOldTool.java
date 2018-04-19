package com.tgf.kcwc.fragments;

import com.tgf.kcwc.R;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/4/28 0028
 * E-mail:hekescott@qq.com
 */

public class HomeMenuOldTool {
    private int    id;
    private int    iconId;
    private String name;
    private static ArrayList mMenuList;

    public HomeMenuOldTool() {

    }
    public HomeMenuOldTool(int id, int iconId, String name) {
        this.id = id;
        this.iconId = iconId;
        this.name = name;
    }
    public static ArrayList<HomeMenuOldTool> getMenuList(){
        if(mMenuList ==null){
            mMenuList = new ArrayList();
            mMenuList.add(new HomeMenuOldTool(1, R.drawable.home_menu1,"展会看"));
            mMenuList.add(new HomeMenuOldTool(2, R.drawable.home_menu2,"店内看"));
            mMenuList.add(new HomeMenuOldTool(3, R.drawable.home_menu3,"选车"));
            mMenuList.add(new HomeMenuOldTool(4, R.drawable.home_menu4,"找优惠"));
            mMenuList.add(new HomeMenuOldTool(5, R.drawable.home_menu5,"车主自售"));
            mMenuList.add(new HomeMenuOldTool(6, R.drawable.home_menu6,"车友"));
            mMenuList.add(new HomeMenuOldTool(7, R.drawable.home_menu7,"开车去"));
            mMenuList.add(new HomeMenuOldTool(8, R.drawable.home_menu8,"请你玩"));
            mMenuList.add(new HomeMenuOldTool(9, R.drawable.home_menu9,"路书"));
            mMenuList.add(new HomeMenuOldTool(10, R.drawable.home_menu10,"玩的爽"));
//            mMenuList.add(new HomeMenuTool(11, R.drawable.home_menu11,"代金券"));
//            mMenuList.add(new HomeMenuTool(12, R.drawable.home_menu12,"转发有奖"));
//            mMenuList.add(new HomeMenuTool(13, R.drawable.home_menu13,"话题"));
//            mMenuList.add(new HomeMenuTool(14, R.drawable.home_menu14,"驾途"));
//            mMenuList.add(new HomeMenuTool(15, R.drawable.home_menu15,"新车快报"));
        }
        return  mMenuList;
    }

    public int getId() {
        return id;
    }

    public int getIconId() {
        return iconId;
    }

    public String getName() {
        return name;
    }
}
