package com.tgf.kcwc.util;

import com.tgf.kcwc.R;

import java.util.HashMap;
import java.util.Map;

public class FaceData {
    public static Map<String, Integer> gifFaceInfo = new HashMap<String, Integer>();

    public static final String         f1          = "[:f1]";
    public static final String         f2          = "[:f2]";
    public static final String         f3          = "[:f3]";
    public static final String         f4          = "[:f4]";
    public static final String         f5          = "[:f5]";
    public static final String         f6          = "[:f6]";
    public static final String         f7          = "[:f7]";
    public static final String         f8          = "[:f8]";
    public static final String         f9          = "[:f9]";
    public static final String         f10         = "[:f10]";
    public static final String         f11         = "[:f11]";
    public static final String         f12         = "[:f12]";
    public static final String         f13         = "[:f13]";
    public static final String         f14         = "[:f14]";
    public static final String         f15         = "[:f15]";
    public static final String         f16         = "[:f16]";
    public static final String         f17         = "[:f17]";
    public static final String         f18         = "[:f18]";
    public static final String         f19         = "[:f19]";
    public static final String         f20         = "[:f20]";
    public static final String         f21         = "[:f21]";
    public static final String         f22         = "[:f22]";
    public static final String         f23         = "[:f23]";
    public static final String         f24         = "[:f24]";
    public static final String         f25         = "[:f25]";
    public static final String         f26         = "[:f26]";
    public static final String         f27         = "[:f27]";
    public static final String         f28         = "[:f28]";
    public static final String         f29         = "[:f29]";
    public static final String         f30         = "[:f30]";
    public static final String         f31         = "[:f31]";
    public static final String         f32         = "[:f32]";
    public static final String         f33         = "[:f33]";
    public static final String         f34         = "[:f34]";
    public static final String         f35         = "[:f35]";
    public static final String         f36         = "[:f36]";
    public static final String         f37         = "[:f37]";
    public static final String         f38         = "[:f38]";
    public static final String         f39         = "[:f39]";
    public static final String         f40         = "[:f40]";

    static {

        addString(gifFaceInfo, f1, R.mipmap.f1);
        addString(gifFaceInfo, f2, R.mipmap.f2);
        addString(gifFaceInfo, f3, R.mipmap.f3);
        addString(gifFaceInfo, f4, R.mipmap.f4);
        addString(gifFaceInfo, f5, R.mipmap.f5);
        addString(gifFaceInfo, f6, R.mipmap.f6);
        addString(gifFaceInfo, f7, R.mipmap.f7);
        addString(gifFaceInfo, f8, R.mipmap.f8);
        addString(gifFaceInfo, f9, R.mipmap.f9);
    }

    private static void addString(Map<String, Integer> map, String smile, int resource) {
        map.put(smile, resource);
    }
}