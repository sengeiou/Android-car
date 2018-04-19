package com.tgf.kcwc.driving.track;

import com.tgf.kcwc.mvp.presenter.RidingRunPresenter;

import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/7/18
 * E-mail:fishloveqin@gmail.com
 *
 *开车去
 */

public class DriverGoImp implements RideRunOperator {

    private RidingRunPresenter mPresenter;

    public  DriverGoImp(RidingRunPresenter presenter) {

        this.mPresenter = presenter;
    }


    @Override
    public void start(Map<String, String> params) {

        mPresenter.rideStart(params);
    }

    @Override
    public void pause(Map<String, String> params) {
        mPresenter.ridePause(params);
    }

    @Override
    public void resume(Map<String, String> params) {
        mPresenter.rideRestart(params);
    }

    @Override
    public void stop(Map<String, String> params) {

        mPresenter.rideEnd(params);
    }

}
