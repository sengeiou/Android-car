package com.tgf.kcwc.view.richtextview;


import android.text.Html;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;
import com.bumptech.glide.*;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.transcode.BitmapToGlideDrawableTranscoder;
import com.bumptech.glide.request.target.Target;
import java.util.ArrayList;
import java.util.Collection;
import com.bumptech.glide.Glide;

/**
 * Auther: Scott
 * Date: 2017/7/7 0007
 * E-mail:hekescott@qq.com
 */

public class GlideImageGetter implements Html.ImageGetter, Drawable.Callback {

    private final GenericRequestBuilder<String, ?, ?, GlideDrawable> glide;
    private final Collection<Target> imageTargets = new ArrayList<>();
    private final TextView targetView;
    private final int width;
    private final Context context;
    private final int height;

    public GlideImageGetter(Context context, RequestManager glide, TextView targetView, boolean animated, int width,int height) {
        this.context = context.getApplicationContext();
        this.glide = createGlideRequest(glide, animated);
        this.targetView = targetView;
        this.width = width;
        this.height = height;
        targetView.setTag(this);
    }

    private GenericRequestBuilder<String, ?, ?, GlideDrawable> createGlideRequest(RequestManager glide,
                                                                                  boolean animated) {
        GenericRequestBuilder<String, ?, ?, GlideDrawable> load;
        if (animated) {
            load = glide
                    .fromString()
                    //".asDrawable()" default loading handles animated GIFs and still images as well
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE) // animated GIFs need source cache
                    // show full image when animating
                    .fitCenter()
            ;
        } else {
            load = glide
                    .fromString()
                    // force still images
                    .asBitmap()
                    // make compatible with target
                    .transcode(new BitmapToGlideDrawableTranscoder(context), GlideDrawable.class)
                    // cache resized images (RESULT), and re-use SOURCE cached GIFs if any
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    // show part of the image when still
                    .centerCrop()
            ;
        }
        return load
                // common settings
                //.listener(new LoggingListener<String, GlideDrawable>())
                ;
    }

    @Override public Drawable getDrawable(String url) {
        // set up target for this Image inside the TextView
        WrapperTarget imageTarget = new WrapperTarget(width,height);
        Drawable asyncWrapper = imageTarget.getLazyDrawable();
        // listen for Drawable's request for invalidation
        asyncWrapper.setCallback(this);

        // start Glide's async load
        glide.load(url).into(imageTarget);
        // save target for clearing it later
        imageTargets.add(imageTarget);
        return asyncWrapper;
    }

    public void clear() {
        for (Target target : imageTargets) {
            Glide.clear(target);
        }
    }

    public static void clear(TextView view) {
        view.setText(null);
        Object tag = view.getTag();
        if (tag instanceof GlideImageGetter) {
            ((GlideImageGetter)tag).clear();
            view.setTag(null);
        }
    }
    @Override public void invalidateDrawable(Drawable who) {
        targetView.invalidate();
    }
    @Override public void scheduleDrawable(Drawable who, Runnable what, long when) {

    }
    @Override public void unscheduleDrawable(Drawable who, Runnable what) {

    }
}
