package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Auther: Scott
 * Date: 2017/1/4 0004
 * E-mail:hekescott@qq.com
 */

public interface  MotoParamService {

    /**
     *  摩托车参数
     * @param motoid
     * @return
     */
    @GET("car/car/Params")
    Observable<ResponseMessage<MotoParamModel>> getMotoParam(@Query("id") String motoid);
}
