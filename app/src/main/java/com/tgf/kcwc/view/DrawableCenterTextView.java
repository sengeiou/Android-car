package com.tgf.kcwc.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

public class DrawableCenterTextView extends TextView {
    public DrawableCenterTextView(Context paramContext) {
        super(paramContext);
    }

    public DrawableCenterTextView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public DrawableCenterTextView(Context paramContext, AttributeSet paramAttributeSet,
                                  int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    protected void onDraw(Canvas paramCanvas) {
        Drawable[] arrayOfDrawable = getCompoundDrawables();
        if (arrayOfDrawable != null) {
            Drawable localDrawable = arrayOfDrawable[0];
            if (localDrawable != null) {
                float f1 = getPaint().measureText(getText().toString());
                int i = getCompoundDrawablePadding();
                float f2 = f1 + localDrawable.getIntrinsicWidth() + i;
                paramCanvas.translate((getWidth() - f2) / 2.0F, 0.0F);
            }
        }
        super.onDraw(paramCanvas);
    }
}
