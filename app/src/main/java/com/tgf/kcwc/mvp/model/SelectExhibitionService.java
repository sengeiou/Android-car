package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/9/13
 * @describle
 */
public interface SelectExhibitionService {
    /**
     * 获取展会列表
     * @param token
     * @param cityId 城市ID
     * @return
     */
    @GET("car/consumersale/eventlist")
    Observable<SelectExhibitionModel> getSelectExhibition(@Query("token") String token,
                                                                           @Query("city_id") int cityId);
}
