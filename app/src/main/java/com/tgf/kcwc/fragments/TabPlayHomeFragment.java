package com.tgf.kcwc.fragments;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.driv.DrivingFragment;
import com.tgf.kcwc.driving.please.PleasePlayFragment;
import com.tgf.kcwc.mvp.model.PlayTypeBean;
import com.tgf.kcwc.play.havefun.HaveFunActivity;
import com.tgf.kcwc.play.havefun.HaveFunFragment;
import com.tgf.kcwc.seek.SeekActivity;
import com.tgf.kcwc.tripbook.TripBookeListActivity;
import com.tgf.kcwc.tripbook.TripBookeListFragment;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DensityUtils;
import com.tgf.kcwc.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:16/4/23 22:16
 * E-mail:fishloveqin@gmail.com
 */
public class TabPlayHomeFragment extends BaseFragment {

    BaseFragment DrivingFragment, PleasePlayFragment, TabPlayFragment, HaveFunFragment, TripBookeList, ShadowShowFragment, knowFragment;
    private int type;
    private RecyclerView mRecyclerView;
    private CommonAdapter<PlayTypeBean> mType;                                          //顶部
    private List<PlayTypeBean> mTypeList = new ArrayList<>();
    PlayTypeBean mPlayTypeBean;

    ImageView mTitleFunctionBtn;

    public void setTypeDate() {
        mTypeList.clear();
        setTypeBean("车友", "index", 0, true);
        setTypeBean("路书", "hotPic", 1, false);
        // setTypeBean("光影秀", "hotMan", 2, false);
        setTypeBean("开车去", "attention", 3, false);
        setTypeBean("请你玩", "interest", 4, false);
        //setTypeBean("爱车", "loveCar", 5, false);
        mPlayTypeBean = mTypeList.get(0);
    }

    public void setTypeBean(String name, String type, int num, boolean isClick) {
        PlayTypeBean playTypeBean = null;
        playTypeBean = new PlayTypeBean();
        playTypeBean.name = name;
        playTypeBean.type = type;
        playTypeBean.isClick = isClick;
        playTypeBean.number = num;
        mTypeList.add(playTypeBean);
    }

    @Override
    protected void updateData() {

    }

    public void switchoverDriving(int type) {
        DrivingFragment = new DrivingFragment(TabPlayHomeFragment.this, type);
        addOrShowFragment(getActivity().getSupportFragmentManager().beginTransaction(), DrivingFragment);
        knowFragment = DrivingFragment;
    }

    public void switchoverPlease(int type) {
        PleasePlayFragment = new PleasePlayFragment(TabPlayHomeFragment.this, type);
        addOrShowFragment(getActivity().getSupportFragmentManager().beginTransaction(), PleasePlayFragment);
        knowFragment = PleasePlayFragment;
    }

    public void switchoverHaveFun() {
/*        HaveFunFragment = new HaveFunFragment(TabPlayHomeFragment.this);
        addOrShowFragment(getActivity().getSupportFragmentManager().beginTransaction(), HaveFunFragment);
        knowFragment = HaveFunFragment;*/
        CommonUtils.startNewActivity(mContext, HaveFunActivity.class);
    }

    public void switchoverTripBookeList() {
        TripBookeList = new TripBookeListNewFragment();
        addOrShowFragment(getActivity().getSupportFragmentManager().beginTransaction(), TripBookeList);
        knowFragment = TripBookeList;
        //CommonUtils.startNewActivity(mContext, TripBookeListActivity.class);
    }

    public void switchoverShadowShow() {
        ShadowShowFragment = new ShadowShowFragment();
        addOrShowFragment(getActivity().getSupportFragmentManager().beginTransaction(), ShadowShowFragment);
        knowFragment = ShadowShowFragment;
        //CommonUtils.startNewActivity(mContext, TripBookeListActivity.class);
    }

    public void switchoverPlayTab() {
        TabPlayFragment = new TabNewPlayFragment(TabPlayHomeFragment.this);
        addOrShowFragment(getActivity().getSupportFragmentManager().beginTransaction(), TabPlayFragment);
        knowFragment = TabPlayFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.play_fragmenthome;
    }

    @Override
    protected void initView() {
        mRecyclerView = findView(R.id.recyclerview);
        mTitleFunctionBtn = findView(R.id.title_function_btn);
        setTypeDate();
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mType = new CommonAdapter<PlayTypeBean>(mContext, R.layout.typeselect_item, mTypeList) {
            @Override
            public void convert(ViewHolder helper, final PlayTypeBean item) {
                final int position = helper.getPosition();
                LinearLayout selectlayout = helper.getView(R.id.selectlayout);
                TextView name = helper.getView(R.id.name);
                final TextView select = helper.getView(R.id.select);
                name.setText(item.name);

 /*               if (item.name.length() == 3) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            DensityUtils.dp2px(mContext, 70), DensityUtils.dp2px(mContext, 50));
                    layoutParams.leftMargin = DensityUtils.dp2px(mContext, 8);
                    selectlayout.setLayoutParams(layoutParams);
                } else if (item.name.length() == 2) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            DensityUtils.dp2px(mContext, 50), DensityUtils.dp2px(mContext, 50));
                    layoutParams.leftMargin = DensityUtils.dp2px(mContext, 8);
                    selectlayout.setLayoutParams(layoutParams);
                }*/
                if (item.isClick) {
                    select.setVisibility(View.VISIBLE);
                } else {
                    select.setVisibility(View.GONE);
                }

                selectlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mPlayTypeBean.number == item.number) {
                            return;
                        }
                        switch (item.number) {
                            case 0:
                                switchoverPlayTab();
                                break;
                            case 1:
                                switchoverTripBookeList();
                                break;
                            case 2:
                                switchoverShadowShow();
                                break;
                            case 3:
                                switchoverDriving(1);
                                break;
                            case 4:
                                switchoverPlease(1);
                                break;
                        }
                        select(item.number);
                    }
                });

            }
        };
        mRecyclerView.setAdapter(mType);

        String value = KPlayCarApp.getValue(Constants.IntentParams.PLAYINDEX);
        if (TextUtil.isEmpty(value)) {
            if (knowFragment == null) {
                PlayTab();
            } else {
                switchoverPlayTab();
            }
            select(0);
        } else {
            if (value.equals(Constants.PlayTabSkip.TABPLAYFRAGMENT)) {
                if (knowFragment == null) {
                    PlayTab();
                } else {
                    switchoverPlayTab();
                }
                select(0);
            } else if (value.equals(Constants.PlayTabSkip.DRIVINGFRAGMENT)) {
                if (knowFragment == null) {
                    DrivingLayout();
                } else {
                    switchoverDriving(1);
                }
                select(3);
            } else if (value.equals(Constants.PlayTabSkip.PLEASEPLAYFRAGMENT)) {
                if (knowFragment == null) {
                    PleasePlayFragmentLayout();
                } else {
                    switchoverPlease(1);
                }
                select(4);
            } else if (value.equals(Constants.PlayTabSkip.HAVEFUNFRAGMENT)) {
                if (knowFragment == null) {
                    HaveFunFragmentLayout();
                } else {
                    switchoverHaveFun();
                }

            } else if (value.equals(Constants.PlayTabSkip.TRIPBOOKELIST)) {
                if (knowFragment == null) {
                    TripBookeListFragmentLayout();
                } else {
                    switchoverTripBookeList();
                }
                select(1);
            } else {
                if (knowFragment == null) {
                    PlayTab();
                } else {
                    switchoverPlayTab();
                }
                select(0);
            }

        }

        mTitleFunctionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtils.startNewActivity(mContext, SeekActivity.class);
            }
        });
    }


    public void select(int num) {
        for (int i = 0; i < mTypeList.size(); i++) {
            if (mTypeList.get(i).number == num) {
                mTypeList.get(i).isClick = true;
                mPlayTypeBean = mTypeList.get(i);
            } else {
                mTypeList.get(i).isClick = false;
            }
        }
        mType.notifyDataSetChanged();
    }

    /**
     * 玩车
     */
    private void PlayTab() {
        if (TabPlayFragment == null) {
            TabPlayFragment = new TabNewPlayFragment(TabPlayHomeFragment.this);
        }
        if (!TabPlayFragment.isAdded()) {
            // 提交事务
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_layout, TabPlayFragment).commit();
            // 记录当前Fragment
            knowFragment = TabPlayFragment;
        }
    }

    /**
     * 开车去
     */
    private void DrivingLayout() {
        type = 1;
        if (DrivingFragment == null) {
            DrivingFragment = new DrivingFragment(TabPlayHomeFragment.this, type);
        }
        if (!DrivingFragment.isAdded()) {
            // 提交事务
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_layout, DrivingFragment).commit();
            // 记录当前Fragment
            knowFragment = DrivingFragment;
        }
    }

    /**
     * 请你玩
     */
    private void PleasePlayFragmentLayout() {
        type = 1;
        if (PleasePlayFragment == null) {
            PleasePlayFragment = new PleasePlayFragment(TabPlayHomeFragment.this, type);
        }
        if (!PleasePlayFragment.isAdded()) {
            // 提交事务
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_layout, PleasePlayFragment).commit();
            // 记录当前Fragment
            knowFragment = PleasePlayFragment;
        }
    }

    /**
     * 玩得爽
     */
    private void HaveFunFragmentLayout() {
        if (HaveFunFragment == null) {
            HaveFunFragment = new HaveFunFragment(TabPlayHomeFragment.this);
        }
        if (!HaveFunFragment.isAdded()) {
            // 提交事务
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_layout, HaveFunFragment).commit();
            // 记录当前Fragment
            knowFragment = HaveFunFragment;
        }
    }

    /**
     * 路书
     */
    private void TripBookeListFragmentLayout() {
        type = 1;
        if (TripBookeList == null) {
            TripBookeList = new TripBookeListNewFragment();
        }
        if (!TripBookeList.isAdded()) {
            // 提交事务
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_layout, TripBookeList).commit();
            // 记录当前Fragment
            knowFragment = TripBookeList;
        }
    }


    /**
     * 添加或者显示碎片
     *
     * @param transaction
     * @param fragment
     */
    private void addOrShowFragment(FragmentTransaction transaction, BaseFragment fragment) {
        if (knowFragment == fragment) {
            return;
        }

        if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(knowFragment).add(R.id.content_layout, fragment).commit();
        } else {
            transaction.hide(knowFragment).show(fragment).commit();
        }

        knowFragment = fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onAddress(int requestCode, int resultCode, Intent data) {
        super.onAddress(requestCode, resultCode, data);
        if (knowFragment != null) {
            knowFragment.onAddress(requestCode, resultCode, data);
        }
    }
}
