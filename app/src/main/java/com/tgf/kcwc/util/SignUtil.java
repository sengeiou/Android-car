package com.tgf.kcwc.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.sina.weibo.sdk.utils.MD5;
import com.tgf.kcwc.logger.Logger;

/**
 * Auther: Scott
 * Date: 2017/7/4 0004
 * E-mail:hekescott@qq.com
 */

public class SignUtil {
    private void getSign(String paramStr, Context context )
    {
        Signature[]   paramString = getRawSignature(context, paramStr);
        if ((paramString == null) || (paramString.length == 0)) {
        }
        int j = paramString.length;
        int i = 0;
        while (i < j)
        {
            Logger.d("hekesign == "+ MD5.hexdigest(paramString[i].toByteArray()));
//                stdout(MD5.getMessageDigest(paramString[i].toByteArray()));
            i ++;
        }
    }
    private Signature[] getRawSignature(Context paramContext, String paramString)
    { PackageInfo packageInof =null;
        if ((paramString == null) || (paramString.length() == 0))
        {
            return null;
        }
        PackageManager packageManager = paramContext.getPackageManager();
        try
        {
             packageInof = packageManager.getPackageInfo(paramString, PackageManager.GET_SIGNATURES);
            if (paramContext == null)
            {
                return null;
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return null;
        }
        return packageInof.signatures;
    }
}
