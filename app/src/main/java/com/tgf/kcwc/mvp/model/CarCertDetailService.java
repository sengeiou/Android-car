package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/10/18
 * @describle
 */
public interface CarCertDetailService {
    /**
     * 新增电子证件详情
     * @param isNeedReceive
     * @param id
     * @param isNeedTotal
     * @param code
     * @param token
     * @return
     */
    @GET("cert/cert/details")
    Observable<ResponseMessage<CarCertDetailModel>> getCertDetail(@Query("is_need_receive") String isNeedReceive,
                                                               @Query("id") String id,
                                                               @Query("is_need_tongji") String isNeedTotal,
                                                               @Query("code") String code,
                                                               @Query("token") String token); //is_need_receive=&id=&is_need_tongji=&code=
}
