package com.tgf.kcwc.finddiscount;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.util.CommonUtils;

/**
 * Author：Jenny
 * Date:2017/3/15 15:53
 * E-mail：fishloveqin@gmail.com
 * 找优惠
 */

public class FindDiscountFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup mDiscountRg;
    private FrameLayout mDiscountFl;
    private LimitDiscountFragment mLimitFrag;
    private GiftDiscountFragment mGiftFrag;
    private CouponDiscountFragment mCouponFrag;
    private FragmentManager mFragmentManager;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_finddiscount;
    }

    @Override
    protected void initView() {
        mDiscountRg = findView(R.id.discoutn_group);
        mDiscountRg.setOnCheckedChangeListener(this);
        mDiscountFl = findView(R.id.finddisount_fl);

        mFragmentManager = getChildFragmentManager();
        mFragmentManager.beginTransaction();
//        fm = getFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if(mLimitFrag ==null){
            mLimitFrag = new LimitDiscountFragment();
        }
        transaction.replace(R.id.finddisount_fl, mLimitFrag);
        transaction.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
             switch (checkedId){
                         case R.id.discount_limitrb:
                             if(mLimitFrag ==null){
                                 mLimitFrag = new LimitDiscountFragment();
                             }
                             transaction.replace(R.id.finddisount_fl, mLimitFrag);
                             break;
                         case R.id.discount_giftgrb:
                             if(mGiftFrag ==null){
                                 mGiftFrag = new GiftDiscountFragment();
                             }
                             transaction.replace(R.id.finddisount_fl, mGiftFrag);
                             break;
                         case R.id.discount_couponrb:
                             if(mCouponFrag ==null){
                                 mCouponFrag = new CouponDiscountFragment();
                             }
                             transaction.replace(R.id.finddisount_fl, mCouponFrag);
                             break;
             }
        transaction.commit();
    }
}
