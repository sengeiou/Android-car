package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Auther: Scott
 * Date: 2017/3/9 0009
 * E-mail:hekescott@qq.com
 * 我要买请求接口
 */

public interface BuyMotoService {

    @GET("order/offer/wait_list")
    Observable<ResponseMessage<RapBuymotoModel>> getRapOrdermotoList(@Query("token") String token, @Query("page") int page, @Query("pageSize") int pageSize);
    @GET("order/offer/lists")
    Observable<ResponseMessage<PreBuyMotoModel>> getPreBuymotoList(@Query("token") String token, @Query("status") int status, @Query("page") int page, @Query("pageSize") int pageSize);

    @GET("order/order/detail")
    Observable<ResponseMessage<IRapOrderModel>> getIRapOrderDetail(@Query("token") String token, @Query("id") int id);

    @GET("order/offer/save")
    Observable<ResponseMessage<IRapOrderModel>> postIRapOrderPrice(@Query("token") String token, @Query("id") int id, @Query("offer") int price);

    @GET("carorder/patrol_offer/track")
    Observable<ResponseMessage<OrderBuyTrackModel>> getBuyTrack(@Query("token") String token, @Query("offer_id") int offer_id,@Query("id")int orderId);
}
