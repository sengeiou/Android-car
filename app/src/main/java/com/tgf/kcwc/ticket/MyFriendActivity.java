package com.tgf.kcwc.ticket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.MyPagerAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.coupon.manage.CouponSendSeeActivity;
import com.tgf.kcwc.mvp.model.ContactUser;
import com.tgf.kcwc.ticket.manage.TicketSendSeeActivity;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PagerSlidingTabStrip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/2/9 0009
 * E-mail:hekescott@qq.com
 */

public class MyFriendActivity extends BaseActivity {
    private static final String KEY_INTENT_LISTCONTACT = "select_contact";
    private  final String KEY_INTENT_TYPE= "type";
    private  final int VAULE_INTENT_TICKET= 1;
    private  final int VAULE_INTENT_COUPON= 2;
    private static final String TAG = "MyFriendActivity";
    private ViewPager mPager;
    private PagerSlidingTabStrip mTabs;
    private MyPagerAdapter mPagerAdapter;
    private String[] tabTitle ={"普通关注","商务关注"};
    public ArrayList<ContactUser> mSelecUsers = new ArrayList<>();
    private int intentType =-1;
    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("选择站内好友");
        function.setText("确定");
        function.setOnClickListener(mConfirmListener);
    }

    private View.OnClickListener mConfirmListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent toIntent = new Intent();
            toIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            switch (intentType){
                case VAULE_INTENT_COUPON:
//                    toIntent.setClass(MyFriendActivity.this, CouponSendSeeActivity.class);
                    break;
                case VAULE_INTENT_TICKET:
//                    toIntent.setClass(MyFriendActivity.this, TicketSendSeeActivity.class);
                    break;
                default:
                    break;
            }
            toIntent.putExtra(KEY_INTENT_LISTCONTACT, (Serializable) mSelecUsers);
            setResult(RESULT_OK,toIntent);
            finish();
        }
    };

    @Override
    protected void setUpViews() {
        Intent fromIntent = getIntent();
        Serializable contactSel = fromIntent.getSerializableExtra(KEY_INTENT_LISTCONTACT);
        if(contactSel!= null){
            mSelecUsers = (ArrayList<ContactUser>) contactSel;
        }
       intentType = fromIntent.getIntExtra(KEY_INTENT_TYPE,-1);

        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mPager = (ViewPager) findViewById(R.id.pager);
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        fragments.add(new MyFellowFragment());
//        fragments.add(new MyBusinessFellowFragment());
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, tabTitle);
        mPager.setAdapter(mPagerAdapter);
        mTabs.setViewPager(mPager);
        mPager.setCurrentItem(0);
        setTabsValue();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfriends);

    }
    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        mTabs.setShouldExpand(true);

        // 设置Tab的分割线的颜色
        // mTabs.setDividerColor(getResources().getColor(R.color.color_80cbc4));
        // 设置分割线的上下的间距,传入的是dp
        mTabs.setDividerPaddingTopBottom(12);

        // 设置Tab底部线的高度,传入的是dp
        mTabs.setUnderlineHeight(1);
        //设置Tab底部线的颜色
        mTabs.setUnderlineColor(getResources().getColor(R.color.split_line_color));

        // 设置Tab 指示器Indicator的高度,传入的是dp
        mTabs.setIndicatorHeight(2);
        // 设置Tab Indicator的颜色
        mTabs.setIndicatorColor(getResources().getColor(R.color.tab_text_s_color));

        // 设置Tab标题文字的大小,传入的是dp
        mTabs.setTextSize(16);
        // 设置选中Tab文字的颜色
        mTabs.setSelectedTextColor(getResources().getColor(R.color.tab_text_s_color));
        //设置正常Tab文字的颜色
        mTabs.setTextColor(getResources().getColor(R.color.text_color17));

        //  设置点击Tab时的背景色
//         mTabs.setTabBackground(R.color.white);

        //是否支持动画渐变(颜色渐变和文字大小渐变)
        // mTabs.setFadeEnabled(true);
        // 设置最大缩放,是正常状态的0.3倍
        //mTabs.setZoomMax(0.3F);
        //设置Tab文字的左右间距,传入的是dp
        // mTabs.setTabPaddingLeftRight(24);
    }





}
