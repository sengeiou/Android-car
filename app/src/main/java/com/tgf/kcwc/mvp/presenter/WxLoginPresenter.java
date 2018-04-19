package com.tgf.kcwc.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.UserManagerService;
import com.tgf.kcwc.mvp.view.WXloginView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.EncryptUtil;
import com.tgf.kcwc.util.RXUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.http.Field;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/7/4 0004
 * E-mail:hekescott@qq.com
 */

public class WxLoginPresenter extends WrapPresenter<WXloginView> {
   private WXloginView mView;
    private UserManagerService mService;
    private Subscription mSubscription;

    @Override
    public void attachView(WXloginView view) {
        mView = view;
        mService = ServiceFactory.getUMService();
    }

    public void getWxLogin(String code){
        mSubscription = RXUtil.execute(mService.getWxLogin(code), new Observer<ResponseMessage<Account>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
                if(responseMessage.statusCode==10000){
                    Account jsonMap = (Account)responseMessage.getData();
//                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(responseMessage.getData().toString());
//JSONObject jsonObject = new JSONObject(responseMessage.getData().toString());
//                    Logger.d("openid == " + jsonMap.get("openid"));
                    mView.bindPhoneNum(jsonMap.openid);
                }else if(responseMessage.statusCode==0){
                    mView.showWxLoginSuccess((Account)responseMessage.getData());
                }else{
                    mView.showLoginFailed(responseMessage.statusMessage);
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
     *获取验证码
     */
    public void sendSMS(String tel) {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String randonStr = EncryptUtil.getRandomString(10);
        Subscription subscription  = RXUtil.execute(mService.sendSMS(tel,timeStamp,randonStr, EncryptUtil.getEncyptStr(randonStr,tel+timeStamp)), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
                mView.sendMsgFailure(e.getMessage());
            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                Logger.d(responseMessage.statusMessage);
                if (responseMessage.statusCode == 0) {
                    mView.sendMsgSuccess();
                } else {
                    mView.sendMsgFailure(responseMessage.statusMessage);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
        mSubscriptions.add(subscription);
    }

    /**
     *绑定手机号
     */
    public void bindPhone(String openId,String verCode,String tel) {

        Subscription subscription  = RXUtil.execute(mService.postBindPhone(openId, verCode,tel), new Observer<ResponseMessage<Account>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                Logger.d("bindPhone "+responseMessage.statusMessage);
                Account account = (Account) responseMessage.getData();
                if(account.isOpenPage==1){
                    Logger.d("showRebindPhone 0");
                    mView.showRebindPhone(account);
                }else{
                    Logger.d("showBindSuccess 0");
                    mView.showBindSuccess(account);
                }
            }


        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
        mSubscriptions.add(subscription);
    }
    /**
     *绑定手机号
     */
    public void updateBindPhone(String openId,String userId, String token) {

        Subscription subscription  = RXUtil.execute(mService.postUpdateBindPhone(openId, userId,token), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                if(responseMessage.statusCode==0){
                    mView.showReBindSuccess();
                }else{
                    mView.showLoginFailed(responseMessage.statusMessage);
                }

            }


        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void detachView() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
