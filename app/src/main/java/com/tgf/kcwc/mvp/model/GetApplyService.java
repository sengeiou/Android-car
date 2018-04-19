package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/11/6
 * @describle
 */
public interface GetApplyService {
    @GET("car/consumersale/getapplys")
    Observable<ResponseMessage<GetApplyModel>> getApply(@Query("token") String token,
                                                        @Query("apply_id") int applyId
    );
}
