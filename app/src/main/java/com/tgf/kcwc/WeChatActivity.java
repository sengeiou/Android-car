package com.tgf.kcwc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.ContactUser;
import com.tgf.kcwc.mvp.view.DividerItemDecoration;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;


/**
 * 介绍：高仿微信通讯录界面 测试界面
 * 头部不是HeaderView 因为头部也需要快速导航，"↑"
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * 主页：http://blog.csdn.net/zxt0601
 * 时间： 2016/11/7.
 */

public class WeChatActivity extends BaseActivity {
    private RecyclerView mRv;
    private CityAdapter mAdapter;
    private LinearLayoutManager mManager;
    private List<ContactUser> mDatas = new ArrayList<>();

    private SuspensionDecoration mDecoration;

    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maint);

        mRv = (RecyclerView) findViewById(R.id.rv);
        mRv.setLayoutManager(mManager = new LinearLayoutManager(this));

        mAdapter = new CityAdapter(this, mDatas);
        mRv.setAdapter(mAdapter);
        mRv.addItemDecoration(mDecoration = new SuspensionDecoration(this, mDatas));
        //如果add两个，那么按照先后顺序，依次渲染。
        mRv.addItemDecoration(new DividerItemDecoration(WeChatActivity.this, DividerItemDecoration.VERTICAL_LIST));

        //使用indexBar
        mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);//HintTextView
        mIndexBar = (IndexBar) findViewById(R.id.indexBar);//IndexBar

        //indexbar初始化
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager

        boolean flag = CommonUtils.checkSpPermission(this, new String[]{android.Manifest.permission.READ_CONTACTS}, 1);
        if (flag) {
//            readContacts();
//            new Thread(runnable).start();
            for (int i = 0; i < 10; i++) {
                ContactUser u = new ContactUser();
                u.name = "hke" + i;
                u.userId = i;
                u.mobile = "1336824659" + i;
                mDatas.add(u);
            }
            Logger.d("contact 1");
            mIndexBar.setmSourceDatas(mDatas)
                    .invalidate();
            mAdapter.notifyDataSetChanged();
//            mHandler.sendEmptyMessageDelayed(MSG_SHOW_CONTACT,500);
        }
        //模拟线上加载数据
//        initDatas(getResources().getStringArray(R.array.provinces));
    }

    private final int MSG_SHOW_CONTACT = 100;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_CONTACT:{

                    mIndexBar.setmSourceDatas(mDatas)
                            .invalidate();
                    mAdapter.notifyDataSetChanged();

                }
                break;
            }
        }};
    Runnable runnable = new Runnable() {
        @Override
        public void run() {


            Logger.d("contact 2");
        }
    };
    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    /**
     * 组织数据源
     *
     * @param data
     * @return
     */
    private void initDatas(final String[] data) {

        mDatas = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            ContactUser cityBean = new ContactUser();
//            cityBean.setCity(data[i]);//设置城市名称
            cityBean.name=data[i];
            mDatas.add(cityBean);
        }
        mAdapter.setDatas(mDatas);
        mAdapter.notifyDataSetChanged();
        mIndexBar.setmSourceDatas(mDatas)//设置数据
                .invalidate();
        mDecoration.setmDatas(mDatas);

    }

    /**
     * 更新数据源
     *
     * @param view
     */
    public void updateDatas(View view) {
        for (int i = 0; i < 10; i++) {
            ContactUser cityBean = new ContactUser();
//            cityBean.setCity(data[i]);//设置城市名称
            cityBean.name="东京"+i;
            mDatas.add(cityBean);
//            mDatas.add(new CityBean("东京"+i));
//            mDatas.add(new CityBean("大阪"+i));
        }

        mIndexBar.setmSourceDatas(mDatas)
                .invalidate();
        mAdapter.notifyDataSetChanged();
    }
}
