package com.tgf.kcwc.download;


import android.database.ContentObserver;
import android.os.Handler;

/**
 * Auther: Scott
 * Date: 2017/7/12 0012
 * E-mail:hekescott@qq.com
 */

public class DownloadChangeObserver extends ContentObserver {
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    private Handler handler;
    private long downloadId;
    public DownloadChangeObserver(Handler handler, long downloadId) {
        super(handler);
        this.handler = handler;
        this.downloadId = downloadId;
    }
    @Override
    public void onChange(boolean selfChange) {
      //  updateView(handler, downloadId);
    }
}
