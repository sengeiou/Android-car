package com.tgf.kcwc.mvp.model;

import com.tgf.kcwc.mvp.view.WrapView;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/17
 * @describle
 */
public interface CommunityView extends WrapView{
//    /**
//     * 显示兴趣标签
//     * @param hobbyItem
//     */
//    void showCommunityHobby(ArrayList<CommunityModel.HobbyItem> hobbyItem);
//
//    /**
//     * 显示常搜索的关键字
//     * @param keywordsItem
//     */
//    void showCommunityKeywords(ArrayList<String> keywordsItem);
//    /**
//     * 显示最近浏览的帖子
//     */
//    void showCommunityNote();
//    /**
//     * 显示比较活跃的群
//     */
//    void showCommunityGroup();

    /**
     * 显示社区
     * @param communityModel
     */
    void showCommunity(CommunityModel communityModel);
}
