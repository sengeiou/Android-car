package com.tgf.kcwc.mvp.presenter;


import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ContactUser;
import com.tgf.kcwc.mvp.model.MyFellowModel;
import com.tgf.kcwc.mvp.model.MyFellowlistModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketManageService;
import com.tgf.kcwc.mvp.model.TicketService;
import com.tgf.kcwc.mvp.view.MyFellowView;
import com.tgf.kcwc.util.RXUtil;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/2/10 0010
 * E-mail:hekescott@qq.com
 */

public class MyFellowPresenter extends WrapPresenter<MyFellowView> {
    private MyFellowView mView;
    private TicketManageService mService;
    @Override
    public void attachView(MyFellowView view) {
        mView = view;
        mService = ServiceFactory.getTicketManageService();
    }

    public void  getFellowList(String token,String userId){
     Subscription subscription = RXUtil.execute(mService.getMyFellow(token,userId), new Observer<ResponseMessage<MyFellowlistModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<MyFellowlistModel> arrayListResponseMessage) {

                ArrayList<ContactUser> contactUserList = new ArrayList<ContactUser>();
                contactUserList.clear();
                for(MyFellowModel myFellowModel:arrayListResponseMessage.getData().mFellowList){
                    ContactUser contactUser = new ContactUser();
                    contactUser.userId = myFellowModel.follow_id;
                    contactUser.avatar = myFellowModel.avatar;
                    contactUser.mobile = myFellowModel.tel;
                    contactUser.name=myFellowModel.nickname;
                    contactUser.type =2;
                    contactUserList.add(contactUser);
                }


                mView.showMyFellows(contactUserList);
            }
        });
        mSubscriptions.add(subscription);
    }
}
