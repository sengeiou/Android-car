package com.tgf.kcwc.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by heke on 17/9/10.
 */

public class MyBitmapTransformation extends BitmapTransformation {
    public MyBitmapTransformation(Context context) {
        super(context);
    }
    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
//        Canvas canvas = new Canvas(toTransform);
//        BitmapShader bitmapShader = new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//        int min = Math.min(toTransform.getWidth(), toTransform.getHeight());
//        int radius = min / 2;
//        RadialGradient radialGradient = new RadialGradient(toTransform.getWidth() / 2 , toTransform.getHeight() / 2, radius, Color.TRANSPARENT, Color.WHITE, Shader.TileMode.CLAMP);
//        ComposeShader composeShader = new ComposeShader(bitmapShader, radialGradient, PorterDuff.Mode.SRC_OVER);
//        Paint paint = new Paint();
//        paint.setShader(composeShader);
//        canvas.drawRect(0, 0, toTransform.getWidth(), toTransform.getHeight(), paint);

        final Bitmap toReuse = pool.get(outWidth, outHeight, toTransform.getConfig() != null
                ? toTransform.getConfig() : Bitmap.Config.ARGB_8888);
        Bitmap transformed = BitmapUtils.centerCrop(toReuse, toTransform, outWidth, outHeight);
        if (toReuse != null && toReuse != transformed && !pool.put(toReuse)) {
            toReuse.recycle();
        }
        return transformed;

//        return toTransform;

    }

    @Override
    public String getId() {
        return "MyBitmapTransformation";
    }
}
