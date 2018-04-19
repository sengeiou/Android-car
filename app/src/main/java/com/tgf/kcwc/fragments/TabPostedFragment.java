package com.tgf.kcwc.fragments;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseFragment;

import android.os.Bundle;
import android.view.View;

/**
 * Author:Jenny
 * Date:16/4/23 22:16
 * E-mail:fishloveqin@gmail.com
 */
public class TabPostedFragment extends BaseFragment {

    private static final String tag = "TabHomePage";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_posted;
    }

    @Override
    protected void initView() {

        findView(R.id.functionBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();
        switch (id) {

            case R.id.functionBtn:
                // CommonUtils.startNewActivity(mContext, ReleaseSaleActivity.class);

                break;
        }
    }

}
