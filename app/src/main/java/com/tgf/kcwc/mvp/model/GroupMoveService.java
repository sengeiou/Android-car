package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor Administrator
 * @time 2017/8/9
 * @describle
 */
public interface GroupMoveService {
    /**
     * 好友组间移动
     * @param token
     * @param name
     * @param tel
     * @param friendGroupId
     * @return
     */
    @GET("friend/friend/move")
    Observable<AddCustomerModel> moveFriend(@Query("token") String token,@Query("name") String name,@Query("tel") String tel,@Query("friend_group_id") int friendGroupId);
}
