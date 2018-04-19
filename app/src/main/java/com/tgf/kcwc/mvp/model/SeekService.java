package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 搜索接口
 * Created by Administrator on 2017/4/21.
 */

public interface SeekService {

    /**
     * 模糊查询
     */
    @GET("search")
    Observable<SeekBean> getsSearchList(@Query("token") String token,@Query("name") String name);

    /**
     * 查询
     */
    @GET("search/dispatch")
    Observable<SeekDetailsBean> getsDispatchList(@Query("token") String token,@Query("dispatch") String dispatch,@Query("thread_model") String thread_model,@Query("name") String name,@Query("page") int page);
}
