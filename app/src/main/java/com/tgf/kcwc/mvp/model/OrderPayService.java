package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/9/18
 * @describle
 */
public interface OrderPayService  {
    /**
     * 获取订单详情
     * @param token
     * @param orderSn
     * @return
     */
    @GET("car/consumersale/orderInfo")
    Observable<ResponseMessage<OrderPayModel>> getOrderDetail(@Query("token") String token,
                                                 @Query("order_sn") String orderSn
                                                 );
}
