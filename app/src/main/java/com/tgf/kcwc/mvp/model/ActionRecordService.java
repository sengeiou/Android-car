package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/8/1 11:21
 */

public interface ActionRecordService {
    /**
     * 获取行为记录
     * @param token
     * @param friendId
     * @return
     */
    @GET("friend/friend/behaviourrecord")
    Observable<ResponseMessage<ActionRecordModel>> getActionRecord(@Query("token") String token,@Query("friend_id") int friendId,@Query("page") int page);
}
