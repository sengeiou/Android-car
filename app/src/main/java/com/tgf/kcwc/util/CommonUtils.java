package com.tgf.kcwc.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.MainActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author:Jenny
 * Date:16/4/29 11:52
 * E-mail:fishloveqin@gmail.com
 * <p/>
 * 公共资源工具类
 **/
public final class CommonUtils {

    private CommonUtils() {

    }

    /**
     * 启动新的Activity页面
     *
     * @param context
     * @param cls
     */
    public static void startNewActivity(Context context, Class cls) {

        Intent intent = new Intent(context, cls);
        context.startActivity(intent);

    }

    /**
     * 启动新的有返回值Activity页面
     *
     * @param activity
     * @param args
     * @param cls
     * @param requestCode
     */
    public static void startResultNewActivity(Activity activity, Map<String, Serializable> args,
                                              Class cls, int requestCode) {

        Intent intent = new Intent();
        intent.setClass(activity, cls);
        if (null != args) {

            Iterator iterator = args.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();

                String key = entry.getKey().toString();
                Serializable value = (Serializable) entry.getValue();
                intent.putExtra(key, value);

            }

        }

        activity.startActivityForResult(intent, requestCode);

    }

    /**
     * 启动新的Activity页面
     *
     * @param context
     * @param args
     * @param cls
     */
    public static void startNewActivity(Context context, Map<String, Serializable> args,
                                        Class cls) {

        Intent intent = new Intent();
        intent.setClass(context, cls);
        if (null != args) {

            Iterator iterator = args.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();

                String key = entry.getKey().toString();
                Serializable value = (Serializable) entry.getValue();
                intent.putExtra(key, value);

            }

        }

        context.startActivity(intent);

    }

    /**
     * Toast 提示框
     *
     * @param context
     * @param content
     */
    public static void showToast(Context context, String content) {

        Toast.makeText(context.getApplicationContext(), content, Toast.LENGTH_SHORT).show();
    }

    /**
     * Toast 提示框
     *
     * @param context
     * @param content
     */
    public static void showToast(Context context, String content,int showTime) {

        Toast.makeText(context.getApplicationContext(), content, showTime).show();
    }
    /**
     * Toast 提示框
     *
     * @param context
     * @param resId
     */
    public static void showToast(Context context, int resId) {

        Toast.makeText(context.getApplicationContext(), resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 检查是否有指定的权限 主要是6.0版本
     *
     * @param activity
     * @param permissions
     * @param requestCode
     */
    public static boolean checkSpPermission(Activity activity, String[] permissions,
                                            int requestCode) {

        if (Build.VERSION.SDK_INT >= 23) {

            for (String s : permissions) {
                if (!(activity.checkSelfPermission(s)//第一个元素做为需要的权限
                        == PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(activity, permissions, requestCode);
                    return false;
                }
            }
            return true;
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }

    }

    private static final String TAG = CommonUtils.class.getSimpleName();

    /**
     * 将位图文件写入到存储设备
     *
     * @param bitmap
     */
    public static String saveImageFileToSD(Bitmap bitmap) {

        FileOutputStream b = null;
        File file = new File(APP_CACHE_DIR);
        String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA))
                + ".jpg";
        file.mkdirs();// 创建文件夹
        String fileName = file.getAbsolutePath() + "/" + name;
        try {
            b = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            b.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (b != null) {
                    b.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return fileName;
    }

    public static final String ROOT = Environment.getExternalStorageDirectory()
            .getAbsolutePath();
    public static final String APP_CACHE_DIR = ROOT + "/Exhibition_data";

    /**
     * 从asset中获取文件并读取数据,并返回BitmapDrawable
     *
     * @param context
     * @param fileName
     */
    public static BitmapDrawable getFromAsset(Context context, String fileName) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName); // 从Assets中的文件获取输入流

            Bitmap bitmap = BitmapFactory.decodeStream(in);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
            return bitmapDrawable;
        } catch (IOException e) {
            e.printStackTrace(); // 捕获异常并打印
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据字符串数组id获取数组对象
     *
     * @param res
     * @param resId
     * @return
     */
    public static String[] getStringArray(Resources res, int resId) {
        String[] arrays = res.getStringArray(resId);

        return arrays;
    }

    public static void customDisplayText(Resources res, TextView tv, String content, int colorId) {
        SpannableString sp = new SpannableString(content);
        sp.setSpan(new ForegroundColorSpan(res.getColor(colorId)), content.lastIndexOf(" "),
                content.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(sp);
    }


    public static void customDisplayText(Resources res, TextView tv, String content, int colorId, int length) {
        SpannableString sp = new SpannableString(content);
        sp.setSpan(new ForegroundColorSpan(res.getColor(colorId)), content.lastIndexOf(" "),
                length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(sp);
    }

    public static void customDisplayText4(Resources res, TextView tv, String content, int colorId) {
        SpannableString sp = new SpannableString(content);
        sp.setSpan(new ForegroundColorSpan(res.getColor(colorId)), content.lastIndexOf(" "),
                content.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(sp);
    }

    /**
     * TextView分块定制化显示
     *
     * @param content
     * @param tv
     * @param res
     */
    public static void customDisplayText(String content, TextView tv, Resources res) {

        Pattern pattern = Pattern.compile("\\d{1,15}");
        Matcher matcher = pattern.matcher(content);
        SpannableString sp = new SpannableString(content);
        while (matcher.find()) {

            int start = matcher.start(0);
            int end = matcher.end(0);
            sp.setSpan(new ForegroundColorSpan(res.getColor(R.color.text_color16)), start, end,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        tv.setText(sp);
    }


    /**
     * TextView分块定制化显示
     *
     * @param content
     * @param tv
     * @param res
     */
    public static void customDisplayText(String content, TextView tv, Resources res, int color) {

        Pattern pattern = Pattern.compile("\\d{1,15}");
        Matcher matcher = pattern.matcher(content);
        SpannableString sp = new SpannableString(content);
        while (matcher.find()) {

            int start = matcher.start(0);
            int end = matcher.end(0);
            sp.setSpan(new ForegroundColorSpan(res.getColor(color)), start, end,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        tv.setText(sp);
    }

    /**
     * TextView分块定制化显示
     *
     * @param content
     * @param tv
     * @param res
     */
    public static void customDisplayText(Resources res, String content, TextView tv, int colorId) {

        Pattern pattern = Pattern.compile("\\d{1,15}");
        Matcher matcher = pattern.matcher(content);
        SpannableString sp = new SpannableString(content);
        while (matcher.find()) {

            int start = matcher.start(0);
            int end = matcher.end(0);
            sp.setSpan(new ForegroundColorSpan(res.getColor(colorId)), start, end,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        tv.setText(sp);
    }

    /**
     * TextView分块定制化显示
     *
     * @param content
     * @param tv
     * @param res
     */
    public static void customDisplayAttentionText(String content, TextView tv, Resources res) {

        SpannableString sp = new SpannableString(content);

        String str = ",   关注";
        int index = content.indexOf(str);
        if (index >= 0) {

            sp.setSpan(new ForegroundColorSpan(res.getColor(R.color.text_color12)), 0, str.length(),
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);


        }

        tv.setText(sp);
    }


    /**
     * TextView分块定制化显示
     *
     * @param content
     * @param tv
     * @param res
     */
    public static void customDisplayReplyText(String content, TextView tv, Resources res) {

        SpannableString sp = new SpannableString(content);

        int index = content.indexOf("回复");
        int index2 = content.indexOf(":");
        if (index >= 0) {
            String sendP = content.substring(0, index);

            sp.setSpan(new ForegroundColorSpan(res.getColor(R.color.text_color6)), 0, index,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            if (index2 >= 0) {
                String recieveP = content.substring(index + 2, index2);

                sp.setSpan(new ForegroundColorSpan(res.getColor(R.color.text_color6)), index + 2,
                        index2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }

        }

        tv.setText(sp);
    }

    /**
     * @param content
     * @param tv
     * @param textcolor
     * @param start
     * @param end
     */
    public static void customDisplayText(TextView tv, String content, int textcolor, int start, int end) {

        SpannableString sp = new SpannableString(content);


        sp.setSpan(new ForegroundColorSpan(textcolor), start, end,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);


        tv.setText(sp);


    }

    public static void customDisplayText2(Resources res, TextView tv, String content, int colorId) {
        SpannableString sp = new SpannableString(content);
        int index = content.indexOf("用");
        int index2 = content.indexOf("/");
        sp.setSpan(new ForegroundColorSpan(res.getColor(colorId)), index + 1, index2,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(sp);
    }

    public static void customDisplayText3(Resources res, TextView tv, String content, int colorId) {
        SpannableString sp = new SpannableString(content);
        int index = content.indexOf(":");
        sp.setSpan(new ForegroundColorSpan(res.getColor(colorId)), 0, index + 1,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(sp);
    }

    public static String getMoneyType(String string) {
        // 把string类型的货币转换为double类型。
        Double numDouble = Double.parseDouble(string);
        // 想要转换成指定国家的货币格式
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CHINA);
        // 把转换后的货币String类型返回
        String numString = format.format(numDouble);
        numString = numString.substring(1, numString.length());
        numString = numString.substring(0, numString.indexOf("."));
        return numString;
    }


    public static void hideKeyboard(Context context) {
        try {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            if ((((Activity) context).getCurrentFocus() != null) && (((Activity) context).getCurrentFocus().getWindowToken() != null)) {
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showKeyboard(Context context) {
        ((InputMethodManager) (context).getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    public static void openSoftKeyword(final EditText editView) {

        final InputMethodManager imm = (InputMethodManager) editView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                editView.requestFocus();
                editView.setClickable(true);
                imm.showSoftInput(editView, InputMethodManager.SHOW_FORCED);

            }
        }, 100);

    }

    public static void closeSoftKeyword(final EditText editView) {
        editView.requestFocus();
        final InputMethodManager imm = (InputMethodManager) editView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editView.getWindowToken(), 0); //强制隐藏键盘

    }


    /**
     * 跳转到总导航页
     *
     * @param context 当前应用上下文
     * @param index   0 首页 1 展会看 3 看车 4 我的
     */
    public static void gotoMainPage(Context context, int index) {

        List<Activity> allPages = KPlayCarApp.getActivityStack();
        for (Activity a : allPages) {
            a.finish();
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.IntentParams.INDEX, index);
        intent.setClass(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void saveLog() {
        File file = ImagePicker
                .createFile(new File(Constants.RIDE_LINE_CACHE),
                        "log.txt");
        try {
            Runtime.getRuntime().exec(String.format("logcat -v time -f %s", file.getAbsolutePath()));
        } catch (IOException e) {

            Logger.e("写入日志失败:"+e.getMessage());
        }
    }
}
