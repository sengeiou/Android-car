package com.tgf.kcwc.driving.track;

import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/7/18
 * E-mail:fishloveqin@gmail.com
 */

public class RideRunWrapper implements RideRunOperator {

    public RideRunWrapper(RideRunOperator operator) {

        this.mOperator = operator;

    }

    private RideRunOperator mOperator;

    @Override
    public void start(Map<String, String> params) {

        mOperator.start(params);
    }

    @Override
    public void pause(Map<String, String> params) {

        mOperator.pause(params);
    }

    @Override
    public void resume(Map<String, String> params) {
        mOperator.resume(params);
    }

    @Override
    public void stop(Map<String, String> params) {

        mOperator.stop(params);
    }

}
