package com.tgf.kcwc.imageloader;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）
 * 版    本：1.0
 * 创建日期：2016/3/28
 * 描    述：我的Github地址  https://github.com/jeasonlzy0216
 * 修订历史：
 * ================================================
 */

import android.app.Activity;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.lzy.imagepicker.loader.ImageLoader;
import com.squareup.picasso.Picasso;
import com.tgf.kcwc.R;
import com.tgf.kcwc.view.richeditor.MixedTextImageLayout;


import java.io.File;


public class PicassoImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Picasso.with(activity)//
                .load(new File(path))//
                .placeholder(R.mipmap.default_image)//
                .error(R.mipmap.default_image)//
                .resize(width, height)//
                .centerInside()//
                //.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)//

                .into(imageView);
    }

    @Override
    public void displayFileImage(Activity activity, String path, ImageView imageView) {
        File file = new File(path);
        Picasso.with(activity)
                .load(file)
                .placeholder(R.mipmap.default_image)//
                .error(R.mipmap.default_image)
                .into(imageView);
        Glide.with(activity).load(file).crossFade().into(new GlideDrawableImageViewTarget(imageView) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                //mAttacher.update();
            }
        });
    }

    @Override
    public void clearMemoryCache() {
    }
}
