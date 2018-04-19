package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/9/18
 * @describle
 */
public interface ExhibitionIntervalService {
    /**
     * 获取展位时段
     * @param token
     * @param eventId
     * @return
     */
    @GET("car/consumersale/eventdetail")
    Observable<ResponseMessage<ExhibitionIntervalModel>> getExhibitionInterval(@Query("token") String token,
                                                                           @Query("event_id") int eventId
    );
}
