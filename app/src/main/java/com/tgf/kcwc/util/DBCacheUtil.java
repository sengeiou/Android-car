package com.tgf.kcwc.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;

/**
 * Author:Jenny
 * Date:16/6/8 14:30
 * E-mail:fishloveqin@gmail.com
 * 数据缓存工具类
 **/
public class DBCacheUtil {

    /**
     * 判断当前图片是否缓存,此方法运行在UI线程,放在ListView中,会有阻塞UI的效果
     *
     * @param uri
     * @return
     */
    public static boolean isCacheImage(String uri) {

        boolean isCacheInDisk = Fresco.getImagePipelineFactory().getMainDiskStorageCache()
            .hasKey(new SimpleCacheKey(uri));
        return isCacheInDisk;
    }

    private static final int               MAX_CACHE    = 1024 * 1024 * 10;         //最大内存缓存大小10MB
    //将手机磁盘的数据缓存到内存
    public static LruCache<String, Bitmap> mMemoryCache = new LruCache<>(MAX_CACHE);

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 读取本地磁盘缓存的图片
     *
     * @param view
     * @param uri
     */
    public static void readImageByDisk(final ImageView view, final String uri) {
        DataSource<Boolean> dataSource = Fresco.getImagePipeline().isInDiskCache(Uri.parse(uri));
        final Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                if (1 == msg.what) {
                    //view.setImageBitmap((Bitmap) msg.obj);
                    Bitmap bitmap = getBitmapFromMemCache(uri);
                    view.setImageBitmap(bitmap);
                } else if (2 == msg.what) {
                    view.setImageURI(Uri.parse(uri));
                }

            }
        };

        DataSubscriber<Boolean> subscriber = new BaseDataSubscriber<Boolean>() {
            @Override
            protected void onNewResultImpl(DataSource<Boolean> dataSource) {
                Boolean isCache = dataSource.getResult();
                if (isCache != null && isCache) {
                    try {
                        Bitmap bitmap = getBitmapFromMemCache(uri);
                        if (bitmap == null) {

                            InputStream input = Fresco.getImagePipelineFactory()
                                .getMainDiskStorageCache().getResource(new SimpleCacheKey(uri))
                                .openStream();
                            bitmap = BitmapFactory.decodeStream(input);
                            Object o = view.getTag();
                            if (o != null) {
                                View v = (View) o;
                                int width = v.getWidth();
                                int height = v.getHeight();
                                if (width != 0 && height != 0) {
                                    bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                                }

                            }
                            addBitmapToMemoryCache(uri, bitmap);
                        }

                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                        mHandler.sendEmptyMessage(-1);
                    }

                } else {
                    mHandler.sendEmptyMessage(2);
                }
            }

            @Override
            protected void onFailureImpl(DataSource<Boolean> dataSource) {

            }
        };
        dataSource.subscribe(subscriber, Executors.newSingleThreadExecutor());
    }

    public static void writeBSDataToDisk() {

    }

}
