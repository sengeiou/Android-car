package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/10/16
 * @describle
 */
public interface LineDataService {
    /**
     *
     * @param token
     * @param longitude
     * @param latitude
     * @param bookId 路书id
     * @param lat
     * @param lng
     * @return
     */
    @GET("roadbook/index/map")
    Observable<ResponseMessage<LineDataModel>> getLineData(@Query("token") String token,
                                                           @Query("longitude") String longitude,
                                                           @Query("latitude") String latitude,
                                                           @Query("bookId") int bookId,
                                                           @Query("lat") String lat,
                                                           @Query("lng") String lng
                                                           );
}
