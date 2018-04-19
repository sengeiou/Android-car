package com.tgf.kcwc.view.bannerview;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;


public class FrescoImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //用fresco加载图片
//        Uri uri = Uri.parse((String) path);
         String url = (String) path;
//        imageView.setImageURI(uri);
        Picasso.with(context).load(url).into(imageView);

    }
    //提供createImageView 方法，方便fresco自定义ImageView
//    @Override
//    public ImageView createImageView(Context context) {
//        SimpleDraweeView simpleDraweeView=new SimpleDraweeView(context);
//        simpleDraweeView.setScaleType(ImageView.ScaleType.FIT_XY);
//
//        return simpleDraweeView;
//    }
}
