package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.model.PayParamModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface AlipayService {
    /**
     *
     * @param token
     * @param orderId
     * @return
     */
    @POST("car/consumersale/aliAppPay")
    @FormUrlEncoded
    Observable<ResponseMessage<String>> alipay(@Field("token") String token,
                                                      @Field("order_id") String orderId);
}
