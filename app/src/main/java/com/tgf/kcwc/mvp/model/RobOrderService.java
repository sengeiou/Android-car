package com.tgf.kcwc.mvp.model;

import com.tgf.kcwc.util.IOUtils;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @anthor Administrator
 * @time 2017/8/11
 * @describle
 */
public interface RobOrderService {
    /**
     * 潜客抢单
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @POST("/friend/latent/rob")
    Observable<AddCustomerModel> rob(@Field("token") String token,@Field("user_id") String userId);
}
