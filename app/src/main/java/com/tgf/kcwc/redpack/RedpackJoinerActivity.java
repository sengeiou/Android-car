package com.tgf.kcwc.redpack;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.mvp.model.RedpackJoinerModel;
import com.tgf.kcwc.mvp.view.RedpackJoinerView;
import com.tgf.kcwc.mvp.view.WrapView;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;

/**
 * 红包 参与人记录
 * Auther: Scott
 * Date: 2017/10/30 0030
 * E-mail:hekescott@qq.com
 */

public class RedpackJoinerActivity extends BaseActivity implements RedpackJoinerView{
    private ArrayList<RedpackJoinerModel> redpackJoinerModelList  =new ArrayList<>();
    private ListView joinerLv;

    @Override
    protected void setUpViews() {
        joinerLv = (ListView) findViewById(R.id.redpackjoiner_lv);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("参与人记录");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redpackjoiner);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showRedpackJoinerlist() {
        joinerLv.setAdapter(new WrapAdapter<RedpackJoinerModel>(getContext(),redpackJoinerModelList,R.layout.listitem_redpack_joiner) {
            @Override
            public void convert(ViewHolder helper, RedpackJoinerModel item) {

//                if(已关注){
//                    helper.setImageResource(R.id.redpack_joiner_addgaunzhuiv,R.drawable.redpack_quxiaoguanzhu);
//                }else {
//                    helper.setImageResource(R.id.redpack_joiner_addgaunzhuiv,R.drawable.redpack_tianjiaguanzhu);
//
//                }
            }
        });
    }
}
