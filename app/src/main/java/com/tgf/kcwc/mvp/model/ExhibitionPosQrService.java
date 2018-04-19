package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/10/10
 * @describle
 */
public interface ExhibitionPosQrService {
    /**
     * 进入展位
     * @param token
     * @param applyId
     * @param parkCode
     * @param eventId
     * @return
     */
    @GET("car/consumersale/qcInput")
    Observable<CommonEmptyModel> signIn(@Query("token") String token,
                                                                  @Query("apply_id") int applyId,
                                                                  @Query("code") String parkCode,
                                                                  @Query("event_id") int eventId
                                                                  );
    /**
     * 离开展位
     * @param token
     * @param applyId
     * @param parkCode
     * @param eventId
     * @return
     */
    @GET("car/consumersale/qcOutput")
    Observable<CommonEmptyModel> signOut(@Query("token") String token,
                                        @Query("apply_id") int applyId,
                                        @Query("code") String parkCode,
                                        @Query("event_id") int eventId
    );
}
