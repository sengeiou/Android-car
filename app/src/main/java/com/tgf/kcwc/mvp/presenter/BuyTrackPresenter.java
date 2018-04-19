package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BuyMotoService;
import com.tgf.kcwc.mvp.model.OrderBuyTrackModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.BuyTrackView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/3/10 0010
 * E-mail:hekescott@qq.com
 */

public class BuyTrackPresenter extends WrapPresenter<BuyTrackView>{

    private BuyTrackView mView;
    private BuyMotoService mService;
    private Subscription mSubscription;

    @Override
    public void attachView(BuyTrackView view) {
        mView = view;
        mService = ServiceFactory.getBuyMotoService();
    }
    public  void  getTrackInfo(String token,int offerId,int orderId){
        mSubscription = RXUtil.execute(mService.getBuyTrack(token, offerId,orderId), new Observer<ResponseMessage<OrderBuyTrackModel>>() {


            @Override
               public void onCompleted() {

               }
               @Override
               public void onError(Throwable e) {

               }

               @Override
               public void onNext(ResponseMessage<OrderBuyTrackModel> orderBuyTrackModelResponseMessage) {

                   OrderBuyTrackModel   oderBuyTrackModel = orderBuyTrackModelResponseMessage.getData();
                   mView.showBuyTrackView(oderBuyTrackModel);
//                   mView.showHead(oderBuyTrackModel.orderInfo);
               }
           });
        mSubscriptions.add(mSubscription);
    }
}
