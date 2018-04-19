package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/8/22
 * @describle
 */
public interface HelpCenterDetailService {
    /**
     * 获取帮助中心详情
     * @param token
     * @param id
     * @return
     */
    @GET("/help/question/detail")
    Observable<ResponseMessage<HelpCenterDetailModel>> getHelpDetail(@Query("token") String token,@Query("id") int id);
}
