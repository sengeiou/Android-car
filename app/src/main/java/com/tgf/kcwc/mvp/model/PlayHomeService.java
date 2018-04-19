package com.tgf.kcwc.mvp.model;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface PlayHomeService {


    /**
     * 轮播
     */
    @GET("carousel")
    Observable<BannerModel> getDrivingBanner(@Query("token") String token,
                                             @Query("t") String t,
                                             @Query("mark") String mark);

    /**
     * 精选
     */
    @GET("thread/lists")
    Observable<PlayListBean> getDataLists(@Query("token") String token,
                                          @Query("type") String type,
                                          @Query("check") String check,
                                          @Query("page") int page);

    /**
     * 话题
     */
    @GET("thread/lists/show_topic")
    Observable<ResponseMessage<List<PlayTopicBean>>> getShowTopic();


}
