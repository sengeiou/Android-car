package com.tgf.kcwc.mvp.model;

import android.database.Observable;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @anthor noti
 * @time 2017/11/6
 * @describle
 */
public interface ApplyHintService  {
    /**
     *
     * @param token
     * @param eventId
     * @param type
     * @return
     */
    @GET("car/consumersale/getText")
    rx.Observable<ResponseMessage<ApplyHintModel>> getApplyHint(@Query("token") String token,
                                                             @Query("event_id") int eventId,
                                                             @Query("type") int type
                                                             );
}
