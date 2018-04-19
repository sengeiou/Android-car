package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.EvaluationModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/27 0027
 * E-mail:hekescott@qq.com
 */

public interface MotoEvaluationView extends WrapView {
   void showCommitSuccess();

    void showTags(ArrayList<EvaluationModel.Tag> tags);
}
