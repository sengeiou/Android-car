package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.IRapOrderModel;
import com.tgf.kcwc.mvp.model.RapOrderPostModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.SeecarService;
import com.tgf.kcwc.mvp.view.IrapOrderDetailView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/3/10 0010
 * E-mail:hekescott@qq.com
 */

public class IRapSeecarPresenter extends WrapPresenter<IrapOrderDetailView>{
    private IrapOrderDetailView mView;
    private SeecarService motoService;
    @Override
    public void attachView(IrapOrderDetailView view) {
        motoService= ServiceFactory.getSeecarService();
        mView = view;
    }

    public void getIrapOderDetail(String token,int id){
        RXUtil.execute(motoService.getIRapOrderDetail(token, id), new Observer<ResponseMessage<IRapOrderModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<IRapOrderModel> iRapOrderModelResponseMessage) {
                if(iRapOrderModelResponseMessage.statusCode==0){

                    mView.showRapDetail(iRapOrderModelResponseMessage.getData());
                }else {
                    CommonUtils.showToast(mView.getContext(),iRapOrderModelResponseMessage.statusMessage);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
    public void postIRapOrderPrice(String token, int id,  int price){
        RXUtil.execute(motoService.postIRapOrderPrice(token, id, price), new Observer<ResponseMessage<RapOrderPostModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<RapOrderPostModel> responseMessage) {
                if (responseMessage.statusCode == 0) {
                    mView.showPostSuccess(responseMessage.getData());
                } else {
                    mView.showPostFailed(responseMessage.statusMessage);
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
