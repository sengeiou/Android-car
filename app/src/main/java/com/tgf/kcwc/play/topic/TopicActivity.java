package com.tgf.kcwc.play.topic;

import java.util.ArrayList;
import java.util.List;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.MyPagerAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.fragments.TabHomeFragment;
import com.tgf.kcwc.mvp.model.Topic;
import com.tgf.kcwc.posting.TopicListActivity;
import com.tgf.kcwc.seek.SeekActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PagerSlidingTabStrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/5/13.
 */

public class TopicActivity extends BaseActivity {

    private FragmentTabHost mTabHost;
    private int currentTab = 0;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
       /* backEvent(back);
        text.setText(R.string.mytopic);
        function.setImageResource(R.drawable.btn_search_white);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CommonUtils.startResultNewActivity(TopicActivity.this, null, TopicListActivity.class,Constants.InteractionCode.REQUEST_CODE);
                CommonUtils.startNewActivity(mContext, SeekActivity.class);
            }
        });*/
    }

    @Override
    protected void setUpViews() {
        ImageButton viewById = (ImageButton) findViewById(R.id.title_bar_back);
        ImageButton function = (ImageButton) findViewById(R.id.title_function_btn);
        TextView text = (TextView) findViewById(R.id.title_bar_text);
        text.setText(R.string.mytopic);
        backEvent(viewById);
        function.setImageResource(R.drawable.btn_search_white);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CommonUtils.startResultNewActivity(TopicActivity.this, null, TopicListActivity.class,Constants.InteractionCode.REQUEST_CODE);
                CommonUtils.startNewActivity(mContext, SeekActivity.class);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Constants.InteractionCode.REQUEST_CODE == requestCode && resultCode == RESULT_OK) {

            Topic topic = data.getParcelableExtra(Constants.IntentParams.DATA);
            //CommonUtils.showToast(mContext, "回来了" + topic.title);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isTitleBar=false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        upData();
    }

    public void upData() {

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        View tabIndicator = null;
        TextView title = null;
        ImageView ivBtn;

        //推荐
        tabIndicator = getLayoutInflater().inflate(R.layout.tab_top_indicator,
                mTabHost.getTabWidget(), false);
        title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText(getResources().getText(R.string.topic_recommend).toString());
        ivBtn = (ImageView) tabIndicator.findViewById(R.id.striping);
        ivBtn.setImageResource(R.drawable.tab_top_selector);
        mTabHost.addTab(
                mTabHost.newTabSpec(Constants.Topiccode.RECOMMEND_TAB).setIndicator(tabIndicator),
                RecommendTopicFragment.class, null);

        //热门
        tabIndicator = getLayoutInflater().inflate(R.layout.tab_top_indicator,
                mTabHost.getTabWidget(), false);
        title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText(getResources().getText(R.string.topic_hot).toString());
        ivBtn = (ImageView) tabIndicator.findViewById(R.id.striping);
        ivBtn.setImageResource(R.drawable.tab_top_selector);
        mTabHost.addTab(
                mTabHost.newTabSpec(Constants.Topiccode.HOT_TAB).setIndicator(tabIndicator),
                HotTopicFragment.class, null);

        //我的
        tabIndicator = getLayoutInflater().inflate(R.layout.tab_top_indicator,
                mTabHost.getTabWidget(), false);
        title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText(getResources().getText(R.string.topic_me).toString());
        ivBtn = (ImageView) tabIndicator.findViewById(R.id.striping);
        ivBtn.setImageResource(R.drawable.tab_top_selector);
        mTabHost.addTab(
                mTabHost.newTabSpec(Constants.Topiccode.ME_TAB).setIndicator(tabIndicator),
                MyTopicFragment.class, null);

        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabName) {

                switchToTab(tabName);

            }

        });
    }

    public void switchToTab(String tabName) {

        switch (tabName) {
            case Constants.Topiccode.RECOMMEND_TAB:
                mTabHost.setCurrentTab(0);
                currentTab = 0;
                break;
            case Constants.Topiccode.HOT_TAB:
                mTabHost.setCurrentTab(1);
                currentTab = 1;
                break;
            case Constants.Topiccode.ME_TAB:
                if (!IOUtils.isLogin(mContext)) {
                    mTabHost.setCurrentTab(currentTab);
                    return;
                }
                mTabHost.setCurrentTab(2);
                currentTab = 2;
                break;

        }

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
