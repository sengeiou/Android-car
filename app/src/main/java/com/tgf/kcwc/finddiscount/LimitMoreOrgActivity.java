package com.tgf.kcwc.finddiscount;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.DiscountLimitModel;
import com.tgf.kcwc.mvp.model.OrgModel;
import com.tgf.kcwc.see.dealer.DealerHomeActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.countdown.CountdownView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Auther: Scott
 * Date: 2017/4/27 0027
 * E-mail:hekescott@qq.com
 */

public class LimitMoreOrgActivity extends BaseActivity {

    private ListView                               moreOrgLv;
    private ArrayList<DiscountLimitModel.LimitOrg> limiOrgs;
    private int                                    carId;
    private TextView                               orgNumTv;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        Intent fromIntent = getIntent();
        carId = fromIntent.getIntExtra(Constants.IntentParams.ID, -1);
        limiOrgs = fromIntent.getParcelableArrayListExtra(Constants.IntentParams.DATA);

        moreOrgLv = (ListView) findViewById(R.id.discount_moreorg_lv);
        findViewById(R.id.limitmoreorg_backiv).setOnClickListener(this);
        orgNumTv = (TextView) findViewById(R.id.orgnum_tv);
        orgNumTv.setText("共" + limiOrgs.size() + "家经销商同期大促");
        initAdapter();
        moreOrgLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DiscountLimitModel.LimitOrg model = limiOrgs.get(position);
                String orgId = model.id + "";
                String orgTitle = model.name;
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, orgId);
                args.put(Constants.IntentParams.TITLE, orgTitle);
                args.put(Constants.IntentParams.INDEX, 0);
                CommonUtils.startNewActivity(mContext, args, DealerHomeActivity.class);
            }
        });
    }

    private void initAdapter() {
        moreOrgLv.setAdapter(new WrapAdapter<DiscountLimitModel.LimitOrg>(this,
            R.layout.listitem_discountlimit_moreorg, limiOrgs) {
            @Override
            public void convert(ViewHolder helper, DiscountLimitModel.LimitOrg item) {
                helper.setText(R.id.discount_moreorgtv, item.name);
                helper.setText(R.id.discountlimit_org, "↓降 ¥ " + item.rate);
                CountdownView coundownView = helper.getView(R.id.discountlimit_settimetext);
                if (!TextUtils.isEmpty(item.endTime)) {
                    coundownView
                        .start(DateFormatUtil.getTime(item.endTime) - System.currentTimeMillis());
                } else {
                    coundownView.setVisibility(View.INVISIBLE);
                }
            }
        });
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
