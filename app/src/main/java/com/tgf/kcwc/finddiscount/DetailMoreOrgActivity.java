package com.tgf.kcwc.finddiscount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.OrgModel;
import com.tgf.kcwc.util.SystemInvoker;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/4/27 0027
 * E-mail:hekescott@qq.com
 */

public class DetailMoreOrgActivity extends BaseActivity {

    private ListView moreOrgLv;
    private Intent fromIntent;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        findViewById(R.id.limitmoreorg_backiv).setOnClickListener(this);
        moreOrgLv = (ListView) findViewById(R.id.discount_moreorg_lv);
        fromIntent = getIntent();
        giftOrgs = fromIntent.getParcelableArrayListExtra(Constants.IntentParams.DATA);
        initAdapter();
    }

    List<OrgModel> giftOrgs;

    private void initAdapter() {
        moreOrgLv.setAdapter(new WrapAdapter<OrgModel>(this, R.layout.listitem_giftpack_org, giftOrgs) {
            @Override
            public void convert(ViewHolder holder, final OrgModel limitOrg) {
                String url = URLUtil.builderImgUrl(limitOrg.logo, 360, 360);
                holder.setSimpleDraweeViewURL(R.id.giftpack_orgcover, url);
                holder.setText(R.id.giftpack_orgtitle, limitOrg.full_name);
                holder.setText(R.id.giftpack_orgaddress, limitOrg.address);
                holder.getView(R.id.giftpack_call)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SystemInvoker.launchDailPage(getContext(), limitOrg.tel);
                            }
                        });
            }
        });
    }

    private Context getContext() {
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limitmoreorg);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.limitmoreorg_backiv:
                finish();
                break;
            default:
                break;
        }
    }
}
