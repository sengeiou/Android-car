package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/8/7 14:55
 */

public interface BusinessAttentionService {
    /**
     * 获取搜索好友列表
     */
    @GET("friend/friend/lists")
    Observable<ResponseMessage<FriendListModel>> getSearchFriendList(@Query("token") String token,@Query("name") String name,@Query("page") int page);

    /**
     * getGroupingFriendList
     * @param token
     * @param friendGroupId
     * @param page
     * @return
     */
    @GET("friend/friend/lists")
    Observable<ResponseMessage<FriendListModel>> getGroupingFriendList(@Query("token") String token,@Query("friend_group_id") int friendGroupId,@Query("page") int page);
    /**
     * 获取好友全部列表
     */
    @GET("friend/friend/lists")
    Observable<ResponseMessage<FriendListModel>> getFriendAllList(@Query("token") String token,@Query("page") int page);
    /**
     * 获取好友分组
     */
    @GET("friend/group/lists")
    Observable<FriendGroupingModel> getFriendGrouping(@Query("token") String token);
}
