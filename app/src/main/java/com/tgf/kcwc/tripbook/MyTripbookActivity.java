package com.tgf.kcwc.tripbook;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.MainActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.me.myline.CreateRideLineActivity;
import com.tgf.kcwc.mvp.model.TripBookModel;
import com.tgf.kcwc.mvp.presenter.MyTripbookPresenter;
import com.tgf.kcwc.mvp.view.MyTripBookView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/4/28 0028
 * E-mail:hekescott@qq.com
 */

public class MyTripbookActivity extends BaseActivity implements MyTripBookView {

    private ListView mytripbookLv;
    private String token = "";
    private MyTripbookPresenter myTripbookPresenter;
    private WrapAdapter mTripBookAdapter;
    private AdapterView.OnItemClickListener mlistItemLisenter;
    private View emptyLayout;
    private boolean mIsGoHome;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        if (mIsGoHome) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Intent goHomeIntent = new Intent(getContext(), MainActivity.class);
                    goHomeIntent.putExtra(Constants.IntentParams.INDEX, 0);
                    startActivity(goHomeIntent);
                }
            });
        } else {
            backEvent(back);
        }

        text.setText("我的路书");
        function.setImageResource(R.drawable.create_edit);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSelecRoadLine = new Intent(getContext(),
                        MakeTripbookActivity.class);
                toSelecRoadLine.putExtra(Constants.IntentParams.INTENT_TYPE, 100);
                startActivity(toSelecRoadLine);
            }
        });
    }

    @Override
    protected void setUpViews() {
        mIsGoHome = getIntent().getBooleanExtra(Constants.IntentParams.DATA, false);
        mytripbookLv = (ListView) findViewById(R.id.mytripbook_lv);
        emptyLayout = findViewById(R.id.emptyLayout);
        findViewById(R.id.buyTicketBtn2).setOnClickListener(this);

    }

    private void initApater(final ArrayList<TripBookModel.TripBookModelItem> list) {
        mTripBookAdapter = new WrapAdapter<TripBookModel.TripBookModelItem>(getContext(), list,
                R.layout.listitem_tripbook_my) {
            @Override
            public void convert(ViewHolder helper, TripBookModel.TripBookModelItem item) {
                String coverUrl = URLUtil.builderImgUrl(item.cover, 540, 270);
                helper.setSimpleDraweeViewURL(R.id.tripbook_cover, coverUrl);
                String avatarUrl = URLUtil.builderImgUrl(item.avatar, 144, 144);
                helper.setSimpleDraweeViewURL(R.id.tripbook_avatar, avatarUrl);
                helper.setText(R.id.mybook_title, item.title);
                helper.setText(R.id.listviewitem_xiazai, item.downloadcount + "");
                helper.setText(R.id.listviewitem_comment, item.reply_count + "");
                helper.setText(R.id.listviewitem_like, item.digg_count + "");
                helper.setText(R.id.tripbook_nicknametv, item.nickname);
                helper.setText(R.id.mybook_title, item.title);
                helper.getView(R.id.mybook_distance).setVisibility(View.GONE);
                helper.setText(R.id.tripbook_altitudetv, item.altitude_average + "");
                helper.setText(R.id.tripbook_actualtimetv, item.actual_time);
                helper.setText(R.id.tripbook_mileage, item.mileage);
                helper.setText(R.id.tripbook_speedmaxtv, item.speed_max);
                helper.setText(R.id.tripbook_numtv, item.quotient + "");
                helper.setText(R.id.tripbook_speedavgtv, item.speed_average);
                helper.setText(R.id.tripbook_startendtv, item.start_adds + "-" + item.end_adds);
                helper.getView(R.id.layou_item_recom).setVisibility(View.GONE);
//                if(item.is_recommend==1){
//                    helper.getView(R.id.layou_item_recom).setVisibility(View.VISIBLE);
//                }else {
//
//                }
                TextView rideTv = helper.getView(R.id.mytripbook_statustv);
                if (item.status == 2) {
                    rideTv.setVisibility(View.VISIBLE);
                    if (item.rideStatus == 1) {
//                        status == 2 && ride_status == 1, 表示路线已结束，路书保存草稿，显示为编辑中；
                        rideTv.setBackgroundResource(R.drawable.shape_tripeding_bg);
                        rideTv.setText("编辑中");
                    } else if (item.rideStatus == 2||item.rideStatus== 0) {
//                        2、status == 2 && ride_status == 2, 表示路线未结束，路书保存草稿，即描述过部分节点，显示为进行中；
                        rideTv.setBackgroundResource(R.drawable.shape_tripriding_bg);
                        rideTv.setText("进行中");
                    }
                } else {
//                    status == 1, 表示路书已发布。
                    rideTv.setVisibility(View.GONE);
                }
            }
        };
        mlistItemLisenter = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if(position>=0&&position<list.size()){
                   Intent toIntent = new Intent(getContext(), TripbookDetailActivity.class);
                   toIntent.putExtra(Constants.IntentParams.ID, list.get(position).id);
                   startActivity(toIntent);
               }

            }
        };
    }

    @Override
    public void onBackPressed() {
        if (mIsGoHome) {
            finish();
            Intent goHomeIntent = new Intent(getContext(), MainActivity.class);
            goHomeIntent.putExtra(Constants.IntentParams.INDEX, 0);
            startActivity(goHomeIntent);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytripbook);
        myTripbookPresenter = new MyTripbookPresenter();
        myTripbookPresenter.attachView(this);
        token = IOUtils.getToken(getContext());
        mytripbookLv.addFooterView(View.inflate(getContext(), R.layout.bottom_hint_layout, null));

    }

    @Override
    protected void onResume() {
        super.onResume();
        myTripbookPresenter.getMyTripBooklist(token);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void showMyBooklist(ArrayList<TripBookModel.TripBookModelItem> list) {
        if (list == null || list.size() == 0) {
            mytripbookLv.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            mytripbookLv.setVisibility(View.VISIBLE);
            initApater(list);
            mytripbookLv.setAdapter(mTripBookAdapter);
            mytripbookLv.setOnItemClickListener(mlistItemLisenter);

        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.buyTicketBtn2:
                Intent toSelecRoadLine = new Intent(getContext(),
                        MakeTripbookActivity.class);
                toSelecRoadLine.putExtra(Constants.IntentParams.INTENT_TYPE, 100);
                startActivity(toSelecRoadLine);
                break;
        }
    }
}
