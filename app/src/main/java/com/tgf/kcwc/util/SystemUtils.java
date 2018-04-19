package com.tgf.kcwc.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;


/**
 * Auther: Scott
 * Date: 2017/9/13 0013
 * E-mail:hekescott@qq.com
 */

public class SystemUtils {
    //判断当前设备是否是模拟器。如果返回TRUE，则当前是模拟器，不是返回FALSE
    public static boolean isEmulator(Context context){
        String szOperatorName = ((TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();

        if(szOperatorName.toLowerCase().equals("android")== true){
            Log.v("Result:","Find Emulator by OperatorName!");
            return true;
        }
        Log.v("Result:","Not Find Emulator by OperatorName!");
        return false;
    }
}
