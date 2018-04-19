package com.tgf.kcwc.mvp.model;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Author:Jenny
 * Date:2017/11/16
 * E-mail:fishloveqin@gmail.com
 */
public interface ThirdPartyService {

    @GET("https://api.weixin.qq.com/sns/oauth2/access_token")
    Observable<ResponseBody> getWXAuthToken(@QueryMap Map<String,String> params);
}
