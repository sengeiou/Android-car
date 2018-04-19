package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.PopmanEsDetailModel;
import com.tgf.kcwc.mvp.model.Topic;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/7/13 0013
 * E-mail:hekescott@qq.com
 */

public interface PopmanEsDetailView  extends WrapView{
  void  showPopmanEsContent(String content);
  void  showCreateUser(PopmanEsDetailModel.CreateUser createUser);
  void  showTags(ArrayList<Topic> topiclist);

  void showHead(PopmanEsDetailModel esDetailModel);
}
