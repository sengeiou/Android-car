package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/11/16.
 */

public interface ChangeExhibitionService {
    @GET("car/consumersale/getCurrentPark")
    Observable<ResponseMessage<ChangeExhibitionModel>> changeExhibition(@Query("token") String token,
                                                                        @Query("apply_id") int applyId
    );
}
