package com.tgf.kcwc.mvp.view;


import com.tgf.kcwc.mvp.model.ContactUser;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/10 0010
 * E-mail:hekescott@qq.com
 */

public interface MyFellowView extends WrapView {
     void showMyFellows(ArrayList<ContactUser> fellowlist);
}
