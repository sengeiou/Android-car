package com.tgf.kcwc.view.bannerview;

import java.io.Serializable;

import android.content.Context;
import android.view.View;


public interface ImageLoaderInterface<T extends View> extends Serializable {

    void displayImage(Context context, Object path, T imageView);

    T createImageView(Context context);
}
