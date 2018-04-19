package com.tgf.kcwc.seecar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.WaittingPriceModel;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/24 0024
 * E-mail:hekescott@qq.com
 */

public class NearOrgListActivity extends BaseActivity {
    private static final String INTENT_KEY_ORG = "orglist";
    private ListView orglist;
    private Intent fromIntent;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("附近的经销商");
    }

    @Override
    protected void setUpViews() {
        orglist = (ListView) findViewById(R.id.nearorglist_lv);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearorglist);
        fromIntent = getIntent();
       ArrayList< WaittingPriceModel.Org> orgArrayList =getIntent().getParcelableArrayListExtra(INTENT_KEY_ORG);
        if(orgArrayList!=null&&orgArrayList.size()!=0)
        orglist.setAdapter(new WrapAdapter<WaittingPriceModel.Org>(NearOrgListActivity.this,R.layout.listview_item_waittingorg,orgArrayList) {
            @Override
            public void convert(ViewHolder helper, WaittingPriceModel.Org item) {
                helper.getView(R.id.listitem_root_waitingorg).setBackgroundColor(mRes.getColor(R.color.white));
                helper.setText(R.id.org_title, item.fullName);
                helper.setText(R.id.orgs_addresstv, item.address);
                helper.setText(R.id.waitingpric_distance, item.distance + "km");
            }
        });
        orglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fromIntent.putExtra(Constants.IntentParams.INDEX,position);
                setResult(RESULT_OK,fromIntent);
                finish();
            }
        });
    }
}
