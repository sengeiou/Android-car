package com.tgf.kcwc.mvp.model;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/11/14.
 */

public interface WxStatusService {
    @POST("car/consumersale/getOrderStatus")
    @FormUrlEncoded
    Observable<ResponseMessage<String>> getWxStatus(@Field("token") String token,
                                                           @Field("order_id") String orderId);
}
