package com.tgf.kcwc.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.tgf.kcwc.R;

/**
 * Author:Jenny
 * Date:2017/8/28
 * E-mail:fishloveqin@gmail.com
 *  自定义GridView分割线
 */

public class LineGridView extends GridView {
    public LineGridView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public LineGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        View localView1 = getChildAt(0);

        if(localView1!=null){
            int column = getWidth() / localView1.getWidth();//计算出一共有多少列
            int childCount = getChildCount();//子view的总数
            Paint localPaint;//画笔
            localPaint = new Paint();
            localPaint.setStyle(Paint.Style.STROKE);
            localPaint.setColor(getContext().getResources().getColor(R.color.split_line_color));//设置画笔的颜色
            for (int i = 0; i < childCount; i++) {//遍历子view
                View cellView = getChildAt(i);//获取子view
                if ((i + 1) % column == 0) {//第四列
                    //画子view底部横线
                    canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
                } else {//如果view不是最后一行
                    //画子view的右边竖线
                    canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
                    //画子view的底部横线
                    canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
                }
            }
        }

    }
}

