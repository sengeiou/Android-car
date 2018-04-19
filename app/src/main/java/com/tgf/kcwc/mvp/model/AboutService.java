package com.tgf.kcwc.mvp.model;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/9/30
 * @describle
 */
public interface AboutService {
    @GET("agreement/agreement/detail")
    Observable<ResponseMessage<ArrayList<AboutModel>>> getAbout(@Query("token") String token,
                                                    @Query("platform") int platform,
                                                    @Query("place") String place
    );


    @GET("agreement/agreement/detail?place=login")
    Observable<ResponseMessage<ArrayList<AboutModel>>> getAgreement(@Query("token") String token,
                                                                @Query("platform") int platform
    );
}
