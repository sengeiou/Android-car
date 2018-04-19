package com.tgf.kcwc.logger;

import com.google.gson.Gson;

/**
 * Author:Jenny
 * Date:2017/11/28
 * E-mail:fishloveqin@gmail.com
 *
 * 定义行为日志线程
 */

public class ActionLogThread  extends  Thread{


    public static boolean isWriteThreadLive=false;//写日志线程是否已经在运行了

    public ActionLogThread() {
        isWriteThreadLive=true;
    }

    @Override
    public void run() {
        isWriteThreadLive=true;
        Gson gson=new Gson();
        while(!ActionLog.tempQueue.isEmpty()){//队列不空时
            try {
                //写日志到SD卡
                ActionLog.recordStringLog(gson.toJson(ActionLog.tempQueue.poll()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        isWriteThreadLive=false;//队列中的日志都写完了，关闭线程（也可以常开 要测试下）
    }
}
