package com.tgf.kcwc.util;

import java.text.DecimalFormat;

/**
 * Author:Jenny
 * Date:2017/5/6
 * E-mail:fishloveqin@gmail.com
 */

public final class NumFormatUtil {

    /**
     * 保留两位小数
     *
     * @param var
     * @return
     */
    public static double getFmtTwoNum(double var) {

        //方案二:
        DecimalFormat df = new DecimalFormat("#.##");
        double result = Double.parseDouble(df.format(var));

        return result;
    }


    /**
     * 保留两位小数
     *
     * @param var
     * @return
     */
    public static String getFmtMoneyTwoNum(double var) {
        //方案二:
        DecimalFormat df = new DecimalFormat("#.##");
        double result = Double.parseDouble(df.format(var));
        String value = result + "";
        if (value.length() == 3) {
            return value + "0";
        }
        return value;
    }


    /**
     * 保留1位小数
     *
     * @param var
     * @return
     */
    public static double getFmtOneNum(double var) {

        //方案二:
        DecimalFormat df = new DecimalFormat("#.#");
        double result = Double.parseDouble(df.format(var));

        return result;
    }

    /**
     * 保留整数
     *
     * @param var
     * @return
     */
    public static double getFmtNum(double var) {

        //方案二:
        DecimalFormat df = new DecimalFormat("#");
        double result = Double.parseDouble(df.format(var));

        return result;
    }

    /**
     * 保留整数 四舍五入
     *
     * @return
     */
    public static int getFmtString(String numStr) {

        if (TextUtil.isEmpty(numStr)) {
            return 0;
        }
        float numFloat = Float.parseFloat(numStr);
        return (int) (numFloat + 0.5);
    }
}
