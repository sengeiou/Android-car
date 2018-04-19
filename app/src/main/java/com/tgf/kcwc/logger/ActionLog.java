package com.tgf.kcwc.logger;

import com.google.gson.Gson;
import com.lzy.imagepicker.ImagePicker;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.util.IOUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Author:Jenny
 * Date:2017/11/28
 * E-mail:fishloveqin@gmail.com
 * 行为日志
 */

public class ActionLog {

    public static String filePath = Constants.RIDE_LINE_CACHE;

    public static ConcurrentLinkedQueue tempQueue = new ConcurrentLinkedQueue<Object>();


    /**
     * 记录行为信息
     *
     * @param ai
     */
    public static synchronized void recordActionInfoLog(IActionInfo ai) {
        tempQueue.add(ai);
        if (!ActionLogThread.isWriteThreadLive) {//检查写线程是否工作中，没有 则创建
            new ActionLogThread().start();
        }
    }

    /**
     * I
     * 打开日志文件并写入日志
     *
     * @return
     **/
    public static void recordStringLog(String text) {// 新建或打开日志文件

        File file = ImagePicker
                .createFile(new File(filePath),
                        "action_log.txt");
        FileWriter filerWriter = null;
        BufferedWriter bufWriter = null;
        try {
            filerWriter = new FileWriter(file, true);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(text);
            bufWriter.newLine();
            Logger.d("行为日志写入成功", text);
        } catch (IOException e) {
            Logger.d("行为日志写入失败", e.getMessage());
        } finally {
            IOUtils.close(bufWriter);
            IOUtils.close(filerWriter);
        }
    }

    public static ArrayList<IActionInfo> readActionLogData() {


        File file = ImagePicker
                .createFile(new File(filePath),
                        "action_log.txt");


        InputStreamReader reader = null;
        ArrayList<IActionInfo> datas = new ArrayList<IActionInfo>();
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            Gson gson = new Gson();
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {

                RideActionInfo actionInfo = gson.fromJson(str,RideActionInfo.class);
                datas.add(actionInfo);
            }
        } catch (Exception e) {
            Logger.e("行为日志读取失败:" + e.getMessage());
        } finally {
            IOUtils.close(bufferedReader);
            IOUtils.close(inputStreamReader);
        }

        return datas;
    }

}
