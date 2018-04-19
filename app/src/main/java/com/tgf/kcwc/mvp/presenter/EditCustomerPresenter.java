package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.AddCustomerModel;
import com.tgf.kcwc.mvp.model.EditCustomerService;
import com.tgf.kcwc.mvp.model.ExhibitionListModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.presenter.WrapPresenter;
import com.tgf.kcwc.mvp.view.EditCustomerView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @anthor noti
 * @time 2017/8/11 8:59
 */

public class EditCustomerPresenter extends WrapPresenter<EditCustomerView> {
    EditCustomerView mView;
    EditCustomerService mService;
    Subscription mSubscription;
    @Override
    public void attachView(EditCustomerView view) {
        mView = view;
        mService = ServiceFactory.saveCustomerDetailService();
    }
    public void editCustomer(String token,String birthday,String company,String department,String homeAddress,int id,String latitude,String longitude,String name,String position,String qq,String remark,String sAddress,String tel,String wechat,String weibo  ){
        mSubscription = RXUtil.execute(mService.saveCustomerDetail(token,birthday, company, department, homeAddress, id, latitude, longitude, name, position, qq, remark, sAddress, tel, wechat, weibo), new Observer<ResponseMessage<AddCustomerModel>>() {
                    @Override
                    public void onCompleted() {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onNext(ResponseMessage<AddCustomerModel> addCustomerModelResponseMessage) {
                        if (addCustomerModelResponseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS){
                            mView.saveSuccess();
                        }else {
                            mView.saveFail(addCustomerModelResponseMessage.statusMessage);
                        }
                    }
                }
                , new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                });
    }
}
