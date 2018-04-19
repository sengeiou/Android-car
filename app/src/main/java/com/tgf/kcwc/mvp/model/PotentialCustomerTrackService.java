package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/7/31 11:00
 */

public interface PotentialCustomerTrackService {
    /**
     * 潜客跟踪列表
     */
    @GET("friend/latent")
    Observable<ResponseMessage<CustomerTrackModel>> getTrackList(@Query("token") String token,@Query("page") int page);

}
