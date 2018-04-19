package com.tgf.kcwc.ourglory;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.UserHomeDataModel;
import com.tgf.kcwc.transmit.TansmitWinningCrunchiesActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/28.
 */

public class OurGloryHomeActivity extends BaseActivity {


    private List<DataItem> mDataList = new ArrayList<>(); //type数据
    protected GridView showMenuGv;

    public void gainTypeDataList() {
        mDataList.clear();
        DataItem dataItem = null;
        dataItem = new DataItem();
        dataItem.id = 4;
        dataItem.icon = R.drawable.btn_graderanking;
        dataItem.name = "等级排行";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 5;
        dataItem.icon = R.drawable.btn_generalizeranking;
        dataItem.name = "推广明星";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 1;
        dataItem.icon = R.drawable.btn_savemoneyranking;
        dataItem.name = "省钱高手";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 3;
        dataItem.icon = R.drawable.btn_activityranking;
        dataItem.name = "活动领袖";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 2;
        dataItem.icon = R.drawable.btn_relationranking;
        dataItem.name = "关系大师";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 7;
        dataItem.icon = R.drawable.btn_influenceranking;
        dataItem.name = "影响力";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 8;
        dataItem.icon = R.drawable.btn_valueranking;
        dataItem.name = "最具价值群";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 6;
        dataItem.icon = R.drawable.btn_franchiserranking;
        dataItem.name = "十佳经销商";
        mDataList.add(dataItem);
    }

    @Override
    protected void setUpViews() {
        gainTypeDataList();
        showMenuGv = (GridView) findViewById(R.id.showMenuGv);
        WrapAdapter<DataItem> adapter = new WrapAdapter<DataItem>(
                mContext, mDataList, R.layout.gridview_item_ourglory) {

            @Override
            public void convert(ViewHolder helper, final DataItem item) {
                View v = helper.getConvertView();
                TextView titletv = helper.getView(R.id.gridview_item_menutv);
                ImageView iv = helper.getView(R.id.gridview_item_menuiv);
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, item.icon));
                titletv.setText(item.name);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, item.id + "");
                        CommonUtils.startNewActivity(mContext, args, RaffleRecordActivity.class);
                    }
                });
            }
        };
        showMenuGv.setAdapter(adapter);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("荣誉榜单");
        function.setImageResource(R.drawable.global_nav_n);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ourglory_home);
    }

}
