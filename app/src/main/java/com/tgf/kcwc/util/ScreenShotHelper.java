package com.tgf.kcwc.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.MapView;
import com.lzy.imagepicker.ImagePicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by yiyi.qi on 2016/10/31.
 */

public class ScreenShotHelper {
    /**
    * 组装地图截图和其他View截图，并且将截图存储在本地sdcard，需要注意的是目前提供的方法限定为MapView与其他View在同一个ViewGroup下
    *@param    bitmap             地图截图回调返回的结果
     *@param   viewContainer      MapView和其他要截图的View所在的父容器ViewGroup
     *@param   mapView            MapView控件
     *@param   views              其他想要在截图中显示的控件
    * */
    public static void saveScreenShot(final Bitmap bitmap, final ViewGroup viewContainer,
                                      final MapView mapView, final Handler handler,
                                      final View... views) {
        new Thread() {
            public void run() {

                Bitmap screenShotBitmap = getMapAndViewScreenShot(bitmap, viewContainer, mapView,
                    views);
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                    File file = ImagePicker.createFile(
                        new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                                 + "/kcwc/ride_shot/"),
                        "ride", ".png");

                    try {
                        FileOutputStream outputStream = new FileOutputStream(file);
                        screenShotBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                        //根据自己需求，如果外边对bitmp还有别的需求就不要recycle的
                        screenShotBitmap.recycle();
                        bitmap.recycle();
                        Message msg = handler.obtainMessage();
                        msg.what = 3;//截图已保存
                        msg.obj = file;
                        handler.sendMessage(msg);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

        }.start();

    }

    /**
     * 组装地图截图和其他View截图，需要注意的是目前提供的方法限定为MapView与其他View在同一个ViewGroup下
     *@param    bitmap             地图截图回调返回的结果
     *@param   viewContainer      MapView和其他要截图的View所在的父容器ViewGroup
     *@param   mapView            MapView控件
     *@param   views              其他想要在截图中显示的控件
     * */
    public static Bitmap getMapAndViewScreenShot(Bitmap bitmap, ViewGroup viewContainer,
                                                 MapView mapView, View... views) {
        int width = viewContainer.getWidth();
        int height = viewContainer.getHeight();
        final Bitmap screenBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(screenBitmap);
        canvas.drawBitmap(bitmap, mapView.getLeft(), mapView.getTop(), null);
        if (views != null) {
            for (View view : views) {
                if(view!=null){
                    view.setDrawingCacheEnabled(true);
                    canvas.drawBitmap(view.getDrawingCache(), view.getLeft(), view.getTop(), null);
                }

            }
        }

        return screenBitmap;
    }

    /**
     * 组装地图截图和其他View截图，需要注意的是目前提供的方法限定为MapView与其他View在同一个ViewGroup下
     *@param    bitmap             地图截图回调返回的结果
     *@param   mapView            MapView控件
     * */
    public static Bitmap getMapAndViewScreenShot(Bitmap bitmap,MapView mapView, int width,int height) {
        final Bitmap screenBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(screenBitmap);
        canvas.drawBitmap(bitmap, mapView.getLeft(), mapView.getTop(), null);

        return screenBitmap;
    }

}
