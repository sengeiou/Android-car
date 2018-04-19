package com.tgf.kcwc.view.autoscrolltext;

/**
 * Author:Jenny
 * Date:2017/11/7
 * E-mail:fishloveqin@gmail.com
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:2017/11/7
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 循环滚动的ListView
 */
public class VerticalScrollTextView extends TextView {

    private float step = 0f;
    private Paint mPaint = new Paint();
    ;
    private String text;
    private float width;

    public void setTextList(List<String> textList) {
        this.textList = textList;
    }

    private List<String> textList = new ArrayList<String>();    //分行保存textview的显示信息。

    public VerticalScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public VerticalScrollTextView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }


    //下面代码是利用上面计算的显示行数，将文字画在画布上，实时更新。
    @Override
    public void onDraw(Canvas canvas) {
        if (textList.size() == 0) return;

        mPaint.setTextSize(40f);//设置字体大小
        for (int i = 0; i < textList.size(); i++) {
            canvas.drawText(textList.get(i), 0, this.getHeight() + (i + 1) * mPaint.getTextSize() - step + 30, mPaint);
        }
        invalidate();

        step = step + 0.3f;
        if (step >= this.getHeight() + textList.size() * mPaint.getTextSize()) {
            step = 0;
        }
    }

}