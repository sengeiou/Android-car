package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/8/14
 * @describle
 */
public interface BusinessRecordDetailService {
    /**
     * 获取代金卷
     * @param friendId
     * @param type
     * @return
     */
    @GET("friend/friend/businessrecord")
    Observable<ResponseMessage<RecordCouponModel>> getCoupon(@Query("token") String token , @Query("friend_id") int friendId,@Query("type") int type,@Query("page") int page);
    /**
     * 获取活动
     * @param friendId
     * @param type
     * @return
     */
    @GET("friend/friend/businessrecord")
    Observable<ResponseMessage<RecordActivityModel>> getActivity(@Query("token") String token , @Query("friend_id") int friendId,@Query("type") int type,@Query("page") int page);
    /**
     * 获取看车
     * @param friendId
     * @param type
     * @return
     */
    @GET("friend/friend/businessrecord")
    Observable<ResponseMessage<RecordSeeCarModel>> getSeeCar(@Query("token") String token , @Query("friend_id") int friendId,@Query("type") int type,@Query("page") int page);
    /**
     * 获取票证
     * @param friendId
     * @param type
     * @return
     */
    @GET("friend/friend/businessrecord")
    Observable<ResponseMessage<RecordTicketModel>> getTicket(@Query("token") String token , @Query("friend_id") int friendId,@Query("type") int type,@Query("page") int page);
}
