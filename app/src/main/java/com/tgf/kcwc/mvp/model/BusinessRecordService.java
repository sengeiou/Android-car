package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/8/15
 * @describle
 */
public interface BusinessRecordService {
    /**
     * 获取商务记录
     * @param token
     * @param friendId 好友id
     * @return
     */
    @GET("friend/friend/businessstatistics")
    Observable<ResponseMessage<BusinessRecordModel>> getBusinessRecord(@Query("token") String token,@Query("friend_id") int friendId);
}
