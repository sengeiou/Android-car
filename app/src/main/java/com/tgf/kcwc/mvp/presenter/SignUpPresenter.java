package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.AgreementModel;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.DrivingService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.SingUpView;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;

import rx.Observer;

/**
 * Created by Administrator on 2017/4/21.
 */

public class SignUpPresenter extends WrapPresenter<SingUpView> {

    private SingUpView mView;
    private DrivingService mService;

    @Override
    public void attachView(SingUpView view) {
        mView=view;
        mService = ServiceFactory.getDrivingService();
    }

    public void gainAppLsis( String token,String nickname,String num,String reason,String tel,String thread_id){
        RXUtil.execute(mService.getApply(token,nickname,num,reason,tel,thread_id), new Observer<BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(BaseBean attentionBean) {
                if (attentionBean.code==0){
                    mView.dataListSucceed(attentionBean);
                }else {
                    mView.dataListDefeated(attentionBean.msg);
                }
            }
        });
    }

    public void gainAppLsis( String place,String platform){
        RXUtil.execute(mService.getAgreement(place,platform), new Observer<ResponseMessage<List<AgreementModel>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<List<AgreementModel>> attentionBean) {
                if (attentionBean.statusCode==0){
                    mView.dataSucceed(attentionBean.data);
                }else {
                    mView.dataListDefeated(attentionBean.statusMessage);
                }
            }
        });
    }

}
