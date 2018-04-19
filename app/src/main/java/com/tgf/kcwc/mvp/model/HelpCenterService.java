package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/8/22
 * @describle
 */
public interface HelpCenterService {
    /**
     * 获取帮助中心列表
     * @param token
     * @param platformType
     * @param title
     * @return
     */
    @GET("/help/question")
    Observable<ResponseMessage<HelpCenterModel>> getHelpList(@Query("token") String token,
                                                             @Query("platform_type") String platformType,
                                                             @Query("type") String title);
}
