package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/8/18
 * @describle
 */
public interface CustomizedCollectService {
    /**
     * 获取定制及收藏
     * @param token
     * @param friendId
     * @return
     */
    @GET("friend/friend/behaviour")
    Observable<ResponseMessage<CustomizedCollectModel>> getCustomizedCollect(@Query("token") String token,@Query("friend_id") int friendId);
}
