package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/7/31 11:26
 */

public interface RobOrderDecryptService {

    /**
     * 潜客跟踪详情
     */
    @GET("/friend/latent/detail")
    Observable<ResponseMessage<OrderProcessModel>> getOrderProcess(@Query("token") String token,@Query("user_id") String userId,@Query("page") int page);

}
