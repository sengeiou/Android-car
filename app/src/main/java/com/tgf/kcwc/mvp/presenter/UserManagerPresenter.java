package com.tgf.kcwc.mvp.presenter;

import android.text.TextUtils;

import com.tgf.kcwc.R;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.UploadAvatarService;
import com.tgf.kcwc.mvp.model.UserManagerService;
import com.tgf.kcwc.mvp.view.LoginView;
import com.tgf.kcwc.util.EncryptUtil;
import com.tgf.kcwc.util.RXUtil;
import com.tgf.kcwc.util.Validator;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;


/**
 * Author：Jenny
 * Date:2016/12/8 20:55
 * E-mail：fishloveqin@gmail.com
 */

public class UserManagerPresenter implements BasePresenter<LoginView> {
    private LoginView mView;
    private Subscription mSubscription;
    private UserManagerService mService = null;
    private UploadAvatarService mUploadService = null;

    private Account mModel;

    @Override
    public void attachView(LoginView view) {
        this.mView = view;
        mService = ServiceFactory.getUMService();
        mUploadService = ServiceFactory.getUploadService();
    }

    @Override
    public void detachView() {
        unsubscribe();
    }
    /**
     * 密码方式登录
     */
    public void pwd(String tel, String pwd,double latitude,double longitude) {

        if(TextUtils.isEmpty(tel)||tel.trim().length()!=11){
//                   if(!Validator.isMobile(tel)){
            mView.failure(mView.getContext().getText(R.string.prompt_moblienum_error).toString());
            return;
        }

        RXUtil.execute(mService.pwdLogin(tel.trim(), pwd.trim(),"current_city", latitude+"", longitude+"",2,"app_code"), new Observer<ResponseMessage<Account>>() {
            @Override
            public void onCompleted() {


            }

            @Override
            public void onError(Throwable e) {
                mView.failure("登录失败");
            }

            @Override
            public void onNext(ResponseMessage<Account> accountResponseMessage) {
                if (accountResponseMessage.statusCode!= 0){
                    mView.failure(accountResponseMessage.statusMessage);
                }else{
                    mModel = accountResponseMessage.data;
                    mView.success(mModel);
                }

            }
        });
    }

    /**
     * 验证码方式登录
     */
    public void verify(String tel, String verCode,double latitude,double longitude) {

//       if(!Validator.isMobile(tel)){
       if(TextUtils.isEmpty(tel)||tel.trim().length()!=11){
           mView.failure(mView.getContext().getText(R.string.prompt_moblienum_error).toString());
           return;
       }
       if(TextUtils.isEmpty(verCode)){
           mView.failure("请输入验证码");
           return;
       }
        RXUtil.execute(mService.vertifyCodeLogin(tel.trim(), verCode.trim(), "current_city", latitude + "", longitude + "", 2, "app_code", 2), new Observer<ResponseMessage<Account>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);

            }

            @Override
            public void onError(Throwable e) {
                mView.failure("登录失败");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<Account> accountResponseMessage) {
                if (accountResponseMessage.statusCode != 0) {
                    mView.failure(accountResponseMessage.statusMessage);
                } else {
                    mModel = accountResponseMessage.data;
                    mView.success(mModel);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
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

    /**
     *发送短信
     */
    public void sendSMS(String tel) {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String randonStr = EncryptUtil.getRandomString(10);
        if(TextUtils.isEmpty(tel)||tel.trim().length()!=11){
            mView.failure(mView.getContext().getText(R.string.prompt_moblienum_error).toString());
            return;
        }
        RXUtil.execute(mService.sendSMS(tel,timeStamp,randonStr, EncryptUtil.getEncyptStr(randonStr,tel+timeStamp)), new Observer() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
                mView.failure(e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                ResponseMessage responseMessage = (ResponseMessage) o;
                Logger.d(responseMessage.statusMessage);
                if (responseMessage.statusCode == 0) {
                    mView.sendMsgSuccess();
                } else {
                    mView.failure(responseMessage.statusMessage);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    public void sendVoice(String tel) {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String randonStr = EncryptUtil.getRandomString(8);
        RXUtil.execute(mService.sendVoice(tel,timeStamp,randonStr, EncryptUtil.getEncyptStr(randonStr,tel+timeStamp)), new Observer() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
                mView.failure(e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                ResponseMessage responseMessage = (ResponseMessage) o;
                Logger.d(responseMessage.statusMessage);
                if (responseMessage.statusCode == 0) {
                    mView.sendMsgSuccess();
                } else {
                    mView.failure(responseMessage.statusMessage);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
}
