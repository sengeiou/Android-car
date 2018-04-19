package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface CityService {


    /**
     * 城市列表
     */
    @GET("city/index/openlist")
    Observable<CityBean> getCityList(@Query("token") String token);
}
