package com.tgf.kcwc.mvp.model;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/9/21
 * @describle
 */
public interface PayParamService {
    /**
     *
     * @param token
     * @return
     */
    @POST("car/consumersale/wechatAppPay")
    @FormUrlEncoded
    Observable<PayParamModel> getPayParam(@Field("token") String token,
                                          @Field("order_id") int orderId,
                                          @Field("trade_type") String tradeType
                                            );
}
