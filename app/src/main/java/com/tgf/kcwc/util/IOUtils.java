package com.tgf.kcwc.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.lzy.imagepicker.ImagePicker;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.login.LoginActivity;
import com.tgf.kcwc.login.LoginOutSevice;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.RideData;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/1/6 0006
 * E-mail:hekescott@qq.com
 */

public class IOUtils {
    private static Account mAccount = null;
    private static String mToken = "";
    private static String mUserId = "";

    public synchronized static <T> T getObject(Context context, String fileName) {
        T t1 = null;
        ObjectInputStream objectInputStream = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = context.openFileInput(fileName);
            objectInputStream = new ObjectInputStream(fileInputStream);
            t1 = (T) objectInputStream.readObject();

        } catch (Exception e) {
            Logger.e("IOUtils【反序列化对象失败:】" + e.getMessage());
        } finally {
            close(fileInputStream);
            close(objectInputStream);
        }
        return t1;
    }


    /**
     * 反序列化Ride对象
     *
     * @param fileName
     * @param <T>
     * @return
     */
    public static <T> T getRideObject(String fileName) {
        T t1 = null;
        ObjectInputStream objectInputStream = null;
        FileInputStream fileInputStream = null;
        File file = ImagePicker
                .createFile(new File(Constants.RIDE_LINE_CACHE),
                        fileName);
        try {
            fileInputStream = new FileInputStream(file.getAbsolutePath());
            objectInputStream = new ObjectInputStream(fileInputStream);
            t1 = (T) objectInputStream.readObject();

            Logger.d("IOUtils:反序列化对象值：" + t1);
        } catch (Exception e) {
            Logger.e("IOUtils【反序列化对象失败:】" + e.getMessage());
        } finally {
            close(fileInputStream);
            close(objectInputStream);
        }
        return t1;
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Logger.e(e.getLocalizedMessage());
            }
        }
    }

    public static synchronized String getToken(Context context) {

        Account a = getAccount(context);
        if (TextUtils.isEmpty(mToken)) {
            if (a != null && a.userInfo != null) {
                mToken = a.userInfo.token;
            }
        }
        return mToken;
    }

    /**
     * 是否登录
     *
     * @param context
     * @return false 未登录 true 登录
     */
    public static synchronized boolean isLogin(Context context) {

        if (TextUtils.isEmpty(getToken(context))) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return false;
        }
        return true;
    }

    public static synchronized String getUserId(Context context) {

        Account a = getObject(context, Constants.KeyParams.ACCOUNT_KEY);
        if (TextUtils.isEmpty(mUserId) || "0".equals(mUserId)) {
            if (a != null && a.userInfo != null) {
                mUserId = a.userInfo.id + "";
            }
        }

        return mUserId;
    }

    public static synchronized Account getAccount(Context context) {

        if (mAccount == null) {
            mAccount = getObject(context, Constants.KeyParams.ACCOUNT_KEY);
        }
        return mAccount;
    }

    public static <T> void writeObject(Context context, T t, String name) {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(t);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        } finally {
            close(objectOutputStream);
            close(fileOutputStream);
        }

    }

    public static boolean deleteFile(Context context, String name) {

        try {
            File f = context.getFileStreamPath(name);
            if (f.isFile()) {
                return f.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean loginOut(Context context, String name) {
        clearData();
        boolean isOut;
        if (deleteFile(context, name)) {
//            EMClient.getInstance().logout(true);//退出环信登录
            context.startService(new Intent(context, LoginOutSevice.class));
            SharedPreferenceUtil.setIsJpush(context, false);
            isOut = true;
            Logger.d("login out" + isOut);
        } else {
            isOut = false;
        }

        return isOut;
    }

    public static void clearData() {

        mAccount = null;
        mToken = "";
        mUserId = "";

    }


    /**
     * 读取行驶点数据
     *
     * @param fileName
     * @return
     */
    public static synchronized ArrayList<RideData> readPointsFromSD(String fileName) {
        File file = ImagePicker
                .createFile(new File(Constants.RIDE_LINE_CACHE),
                        fileName + ".txt");

        ArrayList<RideData> datas = null;

        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF-8");
            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.setLenient(true);
            datas = gson.fromJson(jsonReader, new TypeToken<ArrayList<RideData>>() {
            }.getType());
        } catch (Exception e) {

            Logger.e("读取行驶点失败:" + e.getMessage());
            datas = new ArrayList<>();
        }


        if (datas == null) {
            datas = new ArrayList<RideData>();
        }
        ArrayList<RideData> tDatas = new ArrayList<RideData>();
        for (RideData data : datas) {
            if (!(data.latitude + "").contains("0.") && !(data.longitude + "").contains("0.")) {
                tDatas.add(data);

            }
        }
        Logger.e("Total points:" + datas.size() + ",Actual points:" + tDatas.size());
        return tDatas;
    }


    /**
     * 写入行驶点数据
     *
     * @param rideData
     * @param fileName
     */
    public static void writePointsToSD(final RideData rideData, final String fileName) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                ArrayList<RideData> datas = readPointsFromSD(fileName);
                datas.add(rideData);
                File file = ImagePicker
                        .createFile(new File(Constants.RIDE_LINE_CACHE),
                                fileName + ".txt");
                Gson gson = new Gson();
                String json = gson.toJson(datas);
                FileUtil.writeFile(file.getAbsolutePath(), json, false);
            }
        }).start();

    }

    /**
     * 序列化Ride对象
     *
     * @param t
     * @param fileName
     * @param <T>
     */
    public static <T> void writeRideObject(T t, String fileName) {
        File file = ImagePicker
                .createFile(new File(Constants.RIDE_LINE_CACHE),
                        fileName);
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file.getAbsolutePath());
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(t);

        } catch (Exception e) {
            Logger.e("IOUtils【序列化对象失败:】" + e.getMessage());
        } finally {
            close(objectOutputStream);
            close(fileOutputStream);
        }

    }

}
