package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/9/29
 * @describle
 */
public interface TripBookRecordService {
    /**
     * 获取路书——我的记录
     * @param rideId
     * @return
     */
    @GET("roadbook/index/records")
    Observable<TripBookRecordModel> getTripBookRecord(@Query("ride_id") int rideId,
                                                      @Query("page") int mPageIndex,
                                                      @Query("pageSize") int mPageSize
                                                      );
}
