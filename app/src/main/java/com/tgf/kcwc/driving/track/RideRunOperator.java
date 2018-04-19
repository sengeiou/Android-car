package com.tgf.kcwc.driving.track;

import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/7/18
 * E-mail:fishloveqin@gmail.com
 * 驾途运行状态接口
 */

public interface RideRunOperator {

    void start(Map<String, String> params); //开始

    void pause(Map<String, String> params);//暂停

    void resume(Map<String, String> params);//继续

    void stop(Map<String, String> params);//结束

}
