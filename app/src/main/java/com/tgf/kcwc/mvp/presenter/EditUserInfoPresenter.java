package com.tgf.kcwc.mvp.presenter;

import android.text.TextUtils;

import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.UploadAvatarService;
import com.tgf.kcwc.mvp.model.UserManagerService;
import com.tgf.kcwc.mvp.view.EditUserInfoView;
import com.tgf.kcwc.util.RXUtil;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observer;

/**
 * Auther: Scott
 * Date: 2017/1/10 0010
 * E-mail:hekescott@qq.com
 */

public class EditUserInfoPresenter implements BasePresenter<EditUserInfoView> {
    private  EditUserInfoView mEditUserInfoView;
    private UserManagerService mService = null;
    private UploadAvatarService mUploadService = null;
    @Override
    public void attachView(EditUserInfoView view) {
        this.mEditUserInfoView = view;
        mService =  ServiceFactory.getUMService();
        mUploadService =  ServiceFactory.getUploadService();
    }

    @Override
    public void detachView() {

    }

    /**
     * 头像，昵称，性别修改
     */
    public void editUserInfo(String avatarUrl, String nickName,int gender,String userid) {
        if(TextUtils.isEmpty(nickName)){
            mEditUserInfoView.showOnFailure("你昵称不能为空");
            return;
        }
        RXUtil.execute(mService.editUserInfo(avatarUrl, userid, "", "", gender, "", nickName), new Observer<ResponseMessage<Object>>() {
            @Override
            public void onCompleted() {
                mEditUserInfoView.showOnSuccess();
            }

            @Override
            public void onError(Throwable e) {
                mEditUserInfoView.showOnFailure(e.getMessage());
            }

            @Override
            public void onNext(ResponseMessage<Object> objectResponseMessage) {

            }
        });
    }
    /**
     * 修改密码
     */
    public void resetPWD(String tel, String verCode,String pwd) {
        if(TextUtils.isEmpty(pwd)){
            mEditUserInfoView.showOnFailure("密码不能为空");
            return;
        }
        RXUtil.execute(mService.setPWD(verCode,tel,pwd), new Observer<ResponseMessage<Object>>() {
            @Override
            public void onCompleted() {
                mEditUserInfoView.showOnSuccess();
            }

            @Override
            public void onError(Throwable e) {
                mEditUserInfoView.showOnFailure(e.getMessage());
            }

            @Override
            public void onNext(ResponseMessage<Object> objectResponseMessage) {
                if (objectResponseMessage.statusCode==0){
                    mEditUserInfoView.showOnSuccess();
                }

            }
        });
    }

    /**
     * 上传头像
     */
    public  void uploadAvartar(File file){
        RXUtil.execute(mUploadService.uploadFile( RequestBody.create(MediaType.parse("image/png"), file)), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {
                Logger.d("success");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                Logger.d(responseMessage.statusMessage);
            }
        });
    }

}
