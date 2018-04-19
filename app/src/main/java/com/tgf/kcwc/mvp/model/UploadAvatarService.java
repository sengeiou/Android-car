package com.tgf.kcwc.mvp.model;

import com.tgf.kcwc.entity.PathItem;

import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Auther: Scott
 * Date: 2016/12/27 0027
 * E-mail:hekescott@qq.com
 * 上传头像
 */

public interface UploadAvatarService {

    @POST("publics/public_interface/uplod_file")
    @Multipart
    Observable<ResponseMessage> uploadFile(@Part("file\"; filename=\"avatar.png\"") RequestBody file);

    @Multipart
    @POST("attachment/upload")
    Observable<ResponseMessage<PathItem>> uploadImage(@Part("file\"; filename=\"img_file.png") RequestBody file,
                                                      @Part("module") RequestBody module,
                                                      @Part("itemid") RequestBody itemId, @Part("token") RequestBody token);
}
