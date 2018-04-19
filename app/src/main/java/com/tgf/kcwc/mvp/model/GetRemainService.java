package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/11/6
 * @describle
 */
public interface GetRemainService {
    @GET("car/consumersale/getRemain")
    Observable<GetRemainModel> getRemain(@Query("token") String token,
                                          @Query("event_id") int eventId
                                          );
}
