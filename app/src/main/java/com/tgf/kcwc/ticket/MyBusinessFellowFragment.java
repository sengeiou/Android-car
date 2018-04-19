package com.tgf.kcwc.ticket;


import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.ContactUser;
import com.tgf.kcwc.mvp.presenter.MyFellowPresenter;
import com.tgf.kcwc.mvp.view.MyFellowView;
import com.tgf.kcwc.util.IOUtils;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/10 0010
 * E-mail:hekescott@qq.com
 */

public class MyBusinessFellowFragment extends BaseFragment implements MyFellowView {
    private static final String TAG = "MyBusinessFellowFragment";
    String token;
    String userId;
    private MyFellowPresenter myFellowPresenter;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_myfellow;
    }

    @Override
    protected void initView() {
        userId =  IOUtils.getUserId(getContext());
        token = IOUtils.getToken(getContext());
        myFellowPresenter = new MyFellowPresenter();
        myFellowPresenter.attachView(this);
        myFellowPresenter.getFellowList(token,userId );


    }

    @Override
    public void showMyFellows(ArrayList<ContactUser> fellowlist) {
        Logger.d(TAG,fellowlist);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }
}
