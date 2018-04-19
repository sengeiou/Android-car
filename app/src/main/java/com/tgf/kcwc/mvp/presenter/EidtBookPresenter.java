package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.EditbookModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.RideDataModel;
import com.tgf.kcwc.mvp.model.TripBookService;
import com.tgf.kcwc.mvp.view.EditTripBookView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import retrofit2.http.Query;
import rx.Observable;
import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/5/4 0004
 * E-mail:hekescott@qq.com
 */

public class EidtBookPresenter extends WrapPresenter<EditTripBookView> {

    private  EditTripBookView mView;
    private TripBookService mService;

    @Override
    public void attachView(EditTripBookView view) {
        mView = view;
        mService = ServiceFactory.getTripBookService();
    }

    public void getEditbookInfo(String token,int bookId){
        RXUtil.execute(mService.getEditbookInfo(token, bookId), new Observer<ResponseMessage<EditbookModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<EditbookModel> editbookModelResponseMessage) {
                EditbookModel editbookModel = editbookModelResponseMessage.getData();
                       mView.showEditTripbookView(editbookModel);
                mView.showTagList(editbookModel.topiclist);
            }
        });
    }
    public void postEditbookInfo(Map<String, Object> params){
        RXUtil.execute(mService.createRoadBook(params), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                if(responseMessage.statusCode==0){
                    mView.showSuccess(responseMessage.statusMessage);
                }else{
                    mView.showDoFailed(responseMessage.statusMessage);
                }

            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }


    public void getRoadLinelist(String token){
        RXUtil.execute(mService.getRideLines(token), new Observer<ResponseMessage<RideDataModel>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<RideDataModel> rideDataModelResponseMessage) {
                int rideId = rideDataModelResponseMessage.data.ride_id;
                if (rideId != 0) {
                    if(rideDataModelResponseMessage.data.status==0){//骑行中
                        mView.showUnStop(rideDataModelResponseMessage.getData().nodeList);
                    }else{//骑行中暂停状态
                        mView.showContinue(rideDataModelResponseMessage.getData().nodeList);
                    }
                }
            }
        });
    }
}
