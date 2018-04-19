package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/9/18
 * @describle
 */
public interface SelectExhibitionPosService {
    /**
     * 获取展位相关
     * @param token
     * @param timeSlotId
     * @return
     */
    @GET("car/consumersale/parklist")
    Observable<SelectExhibitionPosModel> getSelectExhibitionPos(@Query("token") String token,
                                                                  @Query("booth_id") int boothId,
                                                                  @Query("time_slot_id") int timeSlotId
                                                                  );
}
