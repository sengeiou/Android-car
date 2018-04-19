package com.tgf.kcwc.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.view.link.Link;
import com.tgf.kcwc.view.link.LinkBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Author:Jenny
 * Date:16/5/16 16:09
 * E-mail:fishloveqin@gmail.com
 * View 公共工具类
 **/
public final class ViewUtil {

    //构造方法私有
    private ViewUtil() {
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int size = listAdapter.getCount();
        for (int i = 0; i < size; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem != null) {
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (size - 1));
        listView.setLayoutParams(params);
    }

    public static int setListViewHeightBasedOnChildren3(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }

        int totalHeight = 0;
        int size = listAdapter.getCount();
        for (int i = 0; i < size; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        return totalHeight + (listView.getDividerHeight() * (size - 1));
    }

    /**
     * 获取Listview的高度，然后设置ViewPager的高度
     *
     * @param listView
     * @return
     */
    public static int setListViewHeightBasedOnChildren1(ListView listView) {
        //获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return 0;
        }

        int totalHeight = 0;
        int len = listAdapter.getCount();
        for (int i = 0; i < len; i++) { //listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
        return params.height;
    }

    /**
     * 获取Listview的高度，然后设置ViewPager的高度
     *
     * @param listView
     * @return
     */
    public static int setListViewHeightBasedOnChildren2(ListView listView) {
        //获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null || listAdapter.getCount() == 0) {
            // pre-condition
            return 0;
        }
        Context context = listView.getContext();
        int totalHeight = 0;
        int len = listAdapter.getCount();
        View listItem = listAdapter.getView(0, null, listView);
        listItem.measure(0, 0);
        int height = listItem.getMeasuredHeight();
        for (int i = 0; i < len; i++) { //listAdapter.getCount()返回数据项的数目
            //View listItem = listAdapter.getView(i, null, listView);
            //listItem.measure(0, 0); //计算子项View 的宽高
            totalHeight += height;//统计所有子项的总高度
        }
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (len - 1));
        listView.setLayoutParams(params);
        return params.height;
    }

    /**
     * 动态绘制， 等同于TextView 的drawableLeft功能
     *
     * @param context
     * @param resId
     * @param tv
     */
    public static void canvasTextDrawLeft(Context context, int resId, TextView tv) {

        Drawable img = null;
        Resources res = context.getResources();
        img = res.getDrawable(resId);
        //调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img.setBounds(0, 0, BitmapUtils.dp2px(context, 15), BitmapUtils.dp2px(context, 15));

        tv.setCompoundDrawables(img, null, null, null);
    }

    /**
     * /**
     * 动态绘制， 等同于TextView 的drawableRight功能
     *
     * @param context
     * @param resId
     * @param tv
     * @param context
     * @param resId
     * @param tv
     * @param heightDP
     */
    public static void canvasTextDrawRight(Context context, int resId, TextView tv, int heightDP) {
        Drawable img = null;
        Resources res = context.getResources();
        img = res.getDrawable(resId);
        //调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img.setBounds(0, 0, img.getMinimumWidth(), BitmapUtils.dp2px(context, heightDP));

        tv.setCompoundDrawables(null, null, img, null);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setListViewHeightBasedOnChildren(GridView listView, int col) {
        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        int count = listAdapter.getCount();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        //int col = 3;// listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < count; i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        int vel = listView.getVerticalSpacing() * (count / col + 1);
        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight + vel;
        // 设置margin
        // ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        listView.setLayoutParams(params);
    }

    public static GradientDrawable getGradientDrawable(boolean isStroke, int color, int radius) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(radius);
        if (isStroke) {
            shape.setStroke(2, Color.GRAY);
        }
        shape.setColor(color);
        return shape;
    }

    //获取状态选择器
    public static StateListDrawable getSelector(Drawable normal, Drawable press) {
        StateListDrawable selector = new StateListDrawable();
        selector.addState(new int[]{android.R.attr.state_pressed}, press);
        selector.addState(new int[]{}, normal);
        return selector;
    }

    //获取状态选择器
    public static StateListDrawable getSelector(boolean isStroke, int normal, int press,
                                                int radius) {
        GradientDrawable bgNormal = getGradientDrawable(isStroke, normal, radius);
        GradientDrawable bgPress = getGradientDrawable(isStroke, press, radius);
        StateListDrawable selector = getSelector(bgNormal, bgPress);
        return selector;
    }

    /**
     * 给ListView 、GridView增加FooterView
     *
     * @param moreText
     * @param type
     * @param parent
     * @return
     */
    public static View createFooterView(String moreText, int type, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View loadMoreView = layoutInflater.inflate(R.layout.common_load_more_item_layout,
                parent, false);
        TextView moreCountTextView = (TextView) loadMoreView.findViewById(R.id.loadMoreCountText);
        TextView moreTextView = (TextView) loadMoreView.findViewById(R.id.loadMoreText);
        switch (type) {
            case Constants.Types.LOAD_MORE_STYLE_1:
                moreCountTextView.setVisibility(View.VISIBLE);
                moreCountTextView.setText(moreText);
                moreTextView.setVisibility(View.GONE);
                break;
            case Constants.Types.LOAD_MORE_STYLE_2:
                moreTextView.setVisibility(View.VISIBLE);
                moreTextView.setText(moreText);
                moreCountTextView.setVisibility(View.GONE);
                break;
        }

        return loadMoreView;
    }

    /**
     * 文本超链接实现
     *
     * @param linkText
     * @param textView
     */
    public static void link(String linkText, TextView textView,
                            Link.OnClickListener onClickListener, int linkColor, boolean isLink) {

        Link link = new Link(linkText)//.setPrependedText("#").setAppendedText("#")
                .setTextColor(linkColor) // optional, defaults to holo blue
                .setHighlightAlpha(.4f)// optional, defaults to .15f
                .setUnderlined(false) // optional, defaults to true
                .setBold(true);
        textView.setLinksClickable(isLink);
        if (isLink) {
            link.setOnClickListener(onClickListener);
        }
        link.setTag(textView.getTag());
        LinkBuilder.on(textView).addLink(link).build(); // create the clickable links
    }


    /**
     * 文本超链接实现
     *
     * @param linkTexts
     * @param textView
     */
    public static void link(List<String> linkTexts, TextView textView,
                            List<Link.OnClickListener> listeners, int linkColor, boolean isLink) {


        List<Link> links = new ArrayList<Link>();
        int index = 0;
        for (String linkText : linkTexts) {

            Link link = new Link(linkText)//.setPrependedText("#").setAppendedText("#")
                    .setTextColor(linkColor) // optional, defaults to holo blue
                    .setHighlightAlpha(.4f)// optional, defaults to .15f
                    .setUnderlined(false) // optional, defaults to true
                    .setBold(true);
            if (isLink) {
                link.setOnClickListener(listeners.get(index));
            }
            index++;
            link.setTag(textView.getTag());
            links.add(link);
        }

        LinkBuilder.on(textView).addLinks(links).build(); // create the clickable links
    }

    public static void setEnabled(View view, boolean enabled) {
        if (null == view) {
            return;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            LinkedList<ViewGroup> queue = new LinkedList<ViewGroup>();
            queue.add(viewGroup);
            // 遍历viewGroup
            while (!queue.isEmpty()) {
                ViewGroup current = queue.removeFirst();
                current.setEnabled(enabled);
                for (int i = 0; i < current.getChildCount(); i++) {
                    if (current.getChildAt(i) instanceof ViewGroup) {
                        queue.addLast((ViewGroup) current.getChildAt(i));
                    } else {
                        current.getChildAt(i).setEnabled(enabled);
                    }
                }
            }
        } else {
            view.setEnabled(enabled);
        }
    }
}
