package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/8/17
 * @describle
 */
public interface CommunityService {
    /**
     * 获取社区分析数据
     * @param token
     * @param friendId
     * @param time
     * @return
     */
    @GET("friend/friend/communitybehaviour")
    Observable<ResponseMessage<CommunityModel>> getCommunity(@Query("token") String token ,
                                                             @Query("friend_id") int friendId,
                                                             @Query("time") int time);
}
