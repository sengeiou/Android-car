package com.tgf.kcwc.mvp.model;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Author：Jenny
 * Date:2017/1/12 16:06
 * E-mail：fishloveqin@gmail.com
 *
 * 图片资源服务接口
 */

public interface ImageService {
    //pagecount=&model_ids=&activity_ids=&car_series_ids=&event_ids=&brand_ids=&token=&page=&is_car_new=
    @GET("event/photo")
    Observable<ResponseMessage<List<String>>> getPhotos(@Query("event_ids") String eventIds,
                                                        @Query("activity_ids") String activityIds,
                                                        @Query("brand_ids") String brandIds,
                                                        @Query("car_series_ids") String carSeriesIds,
                                                        @Query("model_ids") String modelIds,
                                                        @Query("page") String page,
                                                        @Query("pagecount") String pagecount,
                                                        @Query("is_car_new") String isCarNew,
                                                        @Query("token") String token);
}
