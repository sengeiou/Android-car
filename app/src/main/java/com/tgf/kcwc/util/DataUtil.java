package com.tgf.kcwc.util;

import com.tgf.kcwc.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Auther: Scott
 * Date: 2016/12/20 0020
 * E-mail:hekescott@qq.com
 */

public class DataUtil {
    public static final String[] AVATAR = {
            "http://img.wzfzl.cn/uploads/allimg/140820/co140R00Q925-14.jpg",
            "http://www.feizl.com/upload2007/2014_06/1406272351394618.png",
            "http://img3.duitang.com/uploads/item/201508/11/20150811220329_XyZAv.png",
            "http://v1.qzone.cc/avatar/201308/30/22/56/5220b2828a477072.jpg%21200x200.jpg",
            "http://v1.qzone.cc/avatar/201308/22/10/36/521579394f4bb419.jpg!200x200.jpg",
            "http://v1.qzone.cc/avatar/201408/20/17/23/53f468ff9c337550.jpg!200x200.jpg",
            "http://cdn.duitang.com/uploads/item/201408/13/20140813122725_8h8Yu.jpeg",
            "http://img.woyaogexing.com/touxiang/nv/20140212/9ac2117139f1ecd8%21200x200.jpg",
            "http://p1.qqyou.com/touxiang/uploadpic/2013-3/12/2013031212295986807.jpg"};
    public static final String[] BIGIMG = {
            "http://b.hiphotos.baidu.com/image/pic/item/a71ea8d3fd1f4134e61e0f90211f95cad1c85e36.jpg",
            "http://a.hiphotos.baidu.com/image/pic/item/a6efce1b9d16fdfa0fbc1ebfb68f8c5495ee7b8b.jpg",
            "http://e.hiphotos.baidu.com/image/pic/item/902397dda144ad343de8b756d4a20cf430ad858f.jpg",
            "http://g.hiphotos.baidu.com/image/pic/item/4b90f603738da977c76ab6fab451f8198718e39e.jpg"};
    public static final String[] BANNER = {
            "http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg",
            "http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg",
            "http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg",
            "http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg",
            "http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg"};
    public static ArrayList<String> listurl = new ArrayList<>();
    public static ArrayList<String> bannerurl = new ArrayList<>();
    public static String bigImageUrl ="http://c.hiphotos.baidu.com/image/pic/item/7dd98d1001e939011b9c86d07fec54e737d19645.jpg";
    static {
        for (int i=0;i<4;i++){
            listurl.add(AVATAR[i]);
        }
        for (int i=0;i<4;i++){
            bannerurl.add(BANNER[i]);
        }
    }

    public static Map<String,Integer> getTitleActionIcon(){
        Map titleAction= new HashMap();
        titleAction.put("首页", R.drawable.action_shouye);
        titleAction.put("消息", R.drawable.action_xiaoxi);
        titleAction.put("分享", R.drawable.action_fenxiang);
        titleAction.put("收藏", R.drawable.action_shouc);
        titleAction.put("取消收藏", R.drawable.action_shouc);
        titleAction.put("扫一扫", R.drawable.action_saoyisao);
        titleAction.put("反馈", R.drawable.action_fankui);
        titleAction.put("删除", R.drawable.action_del);
        titleAction.put("编辑", R.drawable.action_bianji);
        titleAction.put("举报", R.drawable.action_jubao);
        return titleAction;
    }
    public static Map<String,Integer> getTitleActionIcon2(){
        Map titleAction= new HashMap();
        titleAction.put("首页", R.drawable.action_shouye);
        titleAction.put("消息", R.drawable.action_xiaoxi);
        titleAction.put("分享", R.drawable.action_fenxiang);
        titleAction.put("收藏", R.drawable.action_shouc);
        titleAction.put("扫一扫", R.drawable.action_saoyisao);
        titleAction.put("反馈", R.drawable.action_fankui);
//        titleAction.put("删除", R.drawable.action_del);
//        titleAction.put("编辑", R.drawable.action_bianji);
        titleAction.put("举报", R.drawable.action_jubao);
        return titleAction;
    }
    public static Map<String,Integer> getTitleActionIcon3(){
        Map titleAction= new HashMap();
        titleAction.put("首页", R.drawable.action_shouye);
        titleAction.put("消息", R.drawable.action_xiaoxi);
        titleAction.put("分享", R.drawable.action_fenxiang);
        //titleAction.put("收藏", R.drawable.action_shouc);
        titleAction.put("扫一扫", R.drawable.action_saoyisao);
        titleAction.put("反馈", R.drawable.action_fankui);
//        titleAction.put("删除", R.drawable.action_del);
//        titleAction.put("编辑", R.drawable.action_bianji);
       // titleAction.put("举报", R.drawable.action_jubao);
        return titleAction;
    }
}
