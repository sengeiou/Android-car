package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/10/9
 * @describle
 */
public interface MyExhibitionInfoService {
    /**
     * 获取参展所有信息
     * @param token
     * @param applyId
     * @return
     */
    @GET("car/consumersale/getapply")
    Observable<ResponseMessage<MyExhibitionInfoModel>> getInfo(
                                                    @Query("token") String token ,
                                                    @Query("apply_id") int applyId
                                                    );
}
