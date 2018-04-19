package com.tgf.kcwc.fragments;

import com.tgf.kcwc.R;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/4/28 0028
 * E-mail:hekescott@qq.com
 */

public class HomeMenuTool {
    private int    id;
    private int    iconId;
    private String name;
    private static ArrayList mMenuList;
    private static ArrayList mSeecarMenuList;
    private static ArrayList mPlaycarMenuList;
    private static ArrayList mFuliMenuList;

    private HomeMenuTool() { }
    private HomeMenuTool(int id, int iconId, String name) {
        this.id = id;
        this.iconId = iconId;
        this.name = name;
    }
    public static ArrayList<HomeMenuTool> getMenuList(){
        if(mMenuList ==null){
            mMenuList = new ArrayList();
            mMenuList.add(new HomeMenuTool(1, R.drawable.home_menu1,"展会看"));
            mMenuList.add(new HomeMenuTool(2, R.drawable.home_menu2,"店内看"));
            mMenuList.add(new HomeMenuTool(3, R.drawable.home_menu3,"选车"));
            mMenuList.add(new HomeMenuTool(5, R.drawable.home_menu5,"车主自售"));
            mMenuList.add(new HomeMenuTool(4, R.drawable.home_menu4,"优惠"));

            mMenuList.add(new HomeMenuTool(11, R.drawable.home_menu11,"代金券"));

            mMenuList.add(new HomeMenuTool(9, R.drawable.home_menu9,"路书"));
            mMenuList.add(new HomeMenuTool(7, R.drawable.home_menu7,"开车去"));
            mMenuList.add(new HomeMenuTool(8, R.drawable.home_menu8,"请你玩"));
            mMenuList.add(new HomeMenuTool(99, R.drawable.home_more,"更多"));
//            mMenuList.add(new HomeMenuTool(11, R.drawable.home_menu11,"代金券"));
//            mMenuList.add(new HomeMenuTool(12, R.drawable.home_menu12,"转发有奖"));
//            mMenuList.add(new HomeMenuTool(13, R.drawable.home_menu13,"话题"));
//            mMenuList.add(new HomeMenuTool(14, R.drawable.home_menu14,"驾途"));
//            mMenuList.add(new HomeMenuTool(15, R.drawable.home_menu15,"新车快报"));
        }
        return  mMenuList;
    }

    public static ArrayList<HomeMenuTool> getSeecarMenuList(){
        if(mSeecarMenuList ==null){
            mSeecarMenuList = new ArrayList();
            mSeecarMenuList.add(new HomeMenuTool(3, R.drawable.home_menu3,"选车"));
            mSeecarMenuList.add(new HomeMenuTool(1, R.drawable.home_menu1,"展会看"));
            mSeecarMenuList.add(new HomeMenuTool(2, R.drawable.home_menu2,"店内看"));
            mSeecarMenuList.add(new HomeMenuTool(5, R.drawable.home_menu5,"车主自售"));
            mSeecarMenuList.add(new HomeMenuTool(4, R.drawable.home_menu4,"优惠"));
            mSeecarMenuList.add(new HomeMenuTool(16, R.drawable.home_menu16,"评测"));
            mSeecarMenuList.add(new HomeMenuTool(17, R.drawable.home_menu17,"我要看"));


//            mSeecarMenuList.add(new HomeMenuTool(6, R.drawable.home_menu5,"评测"));
        }
        return  mSeecarMenuList;
    }
    public static ArrayList<HomeMenuTool> getPlaycarMenuList(){
        if(mPlaycarMenuList ==null){
            mPlaycarMenuList = new ArrayList();
            mPlaycarMenuList.add(new HomeMenuTool(7, R.drawable.home_menu7,"开车去"));
            mPlaycarMenuList.add(new HomeMenuTool(8, R.drawable.home_menu8,"请你玩"));
            mPlaycarMenuList.add(new HomeMenuTool(9, R.drawable.home_menu9,"路书"));
            mPlaycarMenuList.add(new HomeMenuTool(13, R.drawable.home_menu13,"话题"));
            mPlaycarMenuList.add(new HomeMenuTool(6, R.drawable.home_menu6,"车友"));
            mPlaycarMenuList.add(new HomeMenuTool(10, R.drawable.home_menu10,"玩得爽"));
//            mPlaycarMenuList.add(new HomeMenuTool(6, R.drawable.home_menu13,"玩的爽"));
        }
        return  mPlaycarMenuList;
    }
    public static ArrayList<HomeMenuTool> getFuliList(){
        if(mFuliMenuList ==null){
            mFuliMenuList = new ArrayList();
            mFuliMenuList.add(new HomeMenuTool(11, R.drawable.home_menu11,"代金券"));
            mFuliMenuList.add(new HomeMenuTool(12, R.drawable.home_menu12,"转发有奖"));
        }
        return  mFuliMenuList;
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
