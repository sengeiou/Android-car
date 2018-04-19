package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/8/18
 * @describle
 */
public interface SearchService {
    /**
     * 获取检索数据
     * @param token
     * @param friendId
     * @param time
     * @return
     */
    @GET("friend/friend/searchbehaviour")
    Observable<ResponseMessage<SearchModel>> getSearch(@Query("token") String token,@Query("friend_id") int friendId,@Query("time") int time);
}
