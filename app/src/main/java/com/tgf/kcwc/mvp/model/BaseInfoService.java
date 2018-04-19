package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/8/15
 * @describle
 */
public interface BaseInfoService {
    /**
     * 获取基本信息
     * @return
     */
    @GET("friend/friend/detail")
    Observable<ResponseMessage<BaseInfoModel>> getBaseInfo(@Query("token") String token,@Query("id") int friendId);
}
