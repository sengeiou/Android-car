package com.tgf.kcwc.seecar;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.MainActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.MySeecarMsgModel;
import com.tgf.kcwc.mvp.model.Pagination;
import com.tgf.kcwc.mvp.presenter.MySeecarMsgPresenter;
import com.tgf.kcwc.mvp.view.MySeecarMsgView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/4/18 0018
 * E-mail:hekescott@qq.com
 */

public class MySeecarMsgActivity extends BaseActivity implements MySeecarMsgView {
    private boolean isEditStatus;
    private ImageButton titleBackBtn;
    private TextView titleEditeTv;
    private TextView titleTextTv;
    private ListView mSeeCarmsgLv;
    private String mToken;
    private MySeecarMsgPresenter mySeecarMsgPresenter;
    private List<MySeecarMsgModel.OrderItem> mMySeecarList;
    private TextView mSeecarCountTv;
    private WrapAdapter myseecarMsgAdapter;
    private ListView mSeecarFilerLv;
    private ArrayList<DataItem> filterList = new ArrayList<>();
    private View filterLayout;
    private TextView filterTitleTv;
    private int filterId;
    private View mlistLayout;
    private RelativeLayout mEmptyLayout;
    private KPlayCarApp kPlayCarApp;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        kPlayCarApp = (KPlayCarApp) getApplication();
        titleBackBtn = (ImageButton) findViewById(R.id.title_back);
        titleBackBtn.setOnClickListener(this);
        titleEditeTv = (TextView) findViewById(R.id.title_editebtn);
        titleTextTv = (TextView) findViewById(R.id.title_text);
        mSeecarCountTv = (TextView) findViewById(R.id.seecarmsg_counttv);
        mlistLayout = findViewById(R.id.myseecarmag_listll);
        TextView msgEmptyTv = (TextView) findViewById(R.id.msgTv2);
        msgEmptyTv.setText("您尚未提交任何看车需求。\n现在提交需求，预约专享服务");
        filterTitleTv = (TextView) findViewById(R.id.filterTitle);
        mEmptyLayout = (RelativeLayout) findViewById(R.id.emptyLayout);
        findViewById(R.id.layoutfilter_title).setOnClickListener(this);
        findViewById(R.id.iseecarBtn2).setOnClickListener(this);
        titleTextTv.setText("我的看车请求");
        titleEditeTv.setText("编辑");
        filterLayout = findViewById(R.id.layout_filteritem);
        mSeeCarmsgLv = (ListView) findViewById(R.id.myseecar_msglistlv);
        mSeecarFilerLv = (ListView) findViewById(R.id.myseecar_filtertlv);
        mSeeCarmsgLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MySeecarMsgModel.OrderItem item = mMySeecarList.get(position);
                if (!isEditStatus) {
                    if (item.status == 1) {
                        PlacePoint myPlacePoint = new PlacePoint();
                        myPlacePoint.myLalng = new LatLng(Double.parseDouble(kPlayCarApp.getLattitude()), Double.parseDouble(kPlayCarApp.getLongitude()));
                        Intent toIntent = new Intent(getContext(), WaittingPriceActivity.class);
                        toIntent.putExtra(INTENT_KEY_ORDER, myPlacePoint);
                        toIntent.putExtra(Constants.IntentParams.ID, item.id);
                        toIntent.putExtra(Constants.IntentParams.TITLE, item.factoryName + " " + item.seriesName + " " + item.carName);
                        startActivity(toIntent);
                    } else if (item.status == 3 || item.status == 0) {//3-已完成、0-已关闭
                        Intent toIntent = new Intent(getContext(), CompleteServerActivity.class);
                        toIntent.putExtra(Constants.IntentParams.ID, item.id);
                        startActivity(toIntent);
                    } else {
                        Intent toYuyueIntent = new Intent(getContext(), YuyueSeecarActivity.class);
                        toYuyueIntent.putExtra(Constants.IntentParams.ID, item.id);
                        startActivity(toYuyueIntent);
                    }

                }
            }
        });
        filterLayout.setVisibility(View.GONE);
        titleEditeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditStatus) {
                    myseecarMsgAdapter.notifyDataSetChanged();
                    titleEditeTv.setText("编辑");
                    isEditStatus = false;
                } else {
                    myseecarMsgAdapter.notifyDataSetChanged();
                    titleEditeTv.setText("完成");
                    isEditStatus = true;
                }
            }
        });
        mySeecarMsgPresenter = new MySeecarMsgPresenter();
        mToken = IOUtils.getToken(this);
        mySeecarMsgPresenter.attachView(this);
        mPageSize = 999;
        iniFilter();
    }

    //    筛选类型：1、等待抢单，待响应，2、预约看车，服务中，3-已完成、0-已关闭
    private void iniFilter() {
        filterList.add(new DataItem(0, "全部"));
        filterList.add(new DataItem(1, "等待抢单"));
        filterList.add(new DataItem(2, "预约看车"));
        filterList.add(new DataItem(3, "已完成"));
        filterList.add(new DataItem(4, "已关闭"));

        mSeecarFilerLv.setAdapter(new WrapAdapter<DataItem>(getContext(), R.layout.filter_slide_item, filterList) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {
                helper.setText(R.id.name, item.name);

            }
        });
        mSeecarFilerLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataItem dataItem = filterList.get(position);

                filterTitleTv.setText(dataItem.name);
                filterLayout.setVisibility(View.GONE);
                if (filterId != dataItem.id) {
                    filterId = dataItem.id;
                    mySeecarMsgPresenter.getMySeecarMsgList(mToken, mPageIndex, mPageSize, filterId);
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myseecarmsg);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.title_back:
               finish();
                break;
            case R.id.iseecarBtn2:
                CommonUtils.startNewActivity(getContext(), CommitCarOrderActivity.class);
                break;
            case R.id.layoutfilter_title:
                if (filterLayout.getVisibility() == View.VISIBLE) {
                    filterLayout.setVisibility(View.GONE);
                } else {
                    filterLayout.setVisibility(View.VISIBLE);
                }
                break;

            default:
                break;
        }
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

    //    筛选类型：1、等待抢单，待响应，2、预约看车，服务中，3-已完成、0-已关闭
    @Override
    public void showMySeecarList(List<MySeecarMsgModel.OrderItem> mySeecarList) {
        mMySeecarList = mySeecarList;
        if (mMySeecarList != null && mMySeecarList.size() != 0) {
//            mlistLayout.setVisibility(View.VISIBLE);
//            titleEditeTv.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
        } else {
//            mlistLayout.setVisibility(View.GONE);
//            titleEditeTv.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
        }

        if (myseecarMsgAdapter == null) {
            myseecarMsgAdapter = new WrapAdapter<MySeecarMsgModel.OrderItem>(getContext(),
                    R.layout.listitem_seecar_mymsg, mMySeecarList) {
                @Override
                public void convert(final ViewHolder helper, final MySeecarMsgModel.OrderItem item) {
                    helper.setText(R.id.seecarmsg_createtimetv, item.createTime);
                    TextView statusTv = helper.getView(R.id.seecarmsg_statustv);
                    if (item.status == 1) {
                        statusTv.setText("等待抢单");
                    } else if (item.status == 2) {
                        statusTv.setText("预约看车");
                    } else if (item.status == 3) {
                        statusTv.setText("已完成");
                    } else if (item.status == 4) {
                        statusTv.setText("已关闭");
                    }
                    String coverUrl = URLUtil.builderImgUrl(item.cover, 270, 203);
                    helper.setSimpleDraweeViewURL(R.id.seecarmsg_cover, coverUrl);
                    helper.setText(R.id.seecar_brandname,
                            item.factoryName + " " + item.seriesName + " " + item.carName);

                    StringBuilder lookingStr = new StringBuilder();
                     TextView seecarLookingTV = helper.getView(R.id.seecar_looking);
                    if(TextUtil.isEmpty(item.outColorName)&&TextUtil.isEmpty(item.inColorName)) {
                        seecarLookingTV.setVisibility(View.GONE);
                    }else {
                        seecarLookingTV.setVisibility(View.VISIBLE);
                    }
                    if(!TextUtil.isEmpty(item.outColorName)){
                        lookingStr.append("外观: ").append(TextUtil.getColorText("#323539",item.outColorName)+"\t ");
                    }
                    if(!TextUtil.isEmpty(item.inColorName)){
                        lookingStr.append("内饰: ").append(TextUtil.getColorText("#323539",item.inColorName));
                    }
                    seecarLookingTV.setText(Html.fromHtml(lookingStr.toString()));
                    ImageView editStatusIv = helper.getView(R.id.seecar_seemoreiv);
                    if (isEditStatus) {
                        editStatusIv
                                .setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.btn_close));
                    } else {
                        editStatusIv
                                .setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.more_icon));
                    }
                    editStatusIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isEditStatus) {
                                mySeecarMsgPresenter.postRemoveItem(mToken, item.id);
                            } else {
                                if (item.status == 1) {
                                    PlacePoint myPlacePoint = new PlacePoint();
                                    myPlacePoint.myLalng = new LatLng(Double.parseDouble(kPlayCarApp.getLattitude()), Double.parseDouble(kPlayCarApp.getLongitude()));
                                    Intent toIntent = new Intent(getContext(), WaittingPriceActivity.class);
                                    toIntent.putExtra(INTENT_KEY_ORDER, myPlacePoint);
                                    toIntent.putExtra(Constants.IntentParams.ID, item.id);
                                    toIntent.putExtra(Constants.IntentParams.TITLE, item.factoryName + " " + item.seriesName + " " + item.carName);
                                    startActivity(toIntent);
                                } else if (item.status == 3 || item.status == 0) {//3-已完成、0-已关闭
                                    Intent toIntent = new Intent(getContext(), CompleteServerActivity.class);
                                    toIntent.putExtra(Constants.IntentParams.ID, item.id);
                                    startActivity(toIntent);
                                } else {
                                    Intent toYuyueIntent = new Intent(getContext(), YuyueSeecarActivity.class);
                                    toYuyueIntent.putExtra(Constants.IntentParams.ID, item.id);
                                    startActivity(toYuyueIntent);
                                }
                            }
                        }
                    });
                }
            };
            mSeeCarmsgLv.setAdapter(myseecarMsgAdapter);
        } else {
            myseecarMsgAdapter.notifyDataSetChanged(mMySeecarList);
        }
    }

    private final String INTENT_KEY_ORDER = "order";

    @Override
    protected void onResume() {
        super.onResume();
        mySeecarMsgPresenter.getMySeecarMsgList(mToken, mPageIndex, mPageSize, -1);
    }

    @Override
    public void showCount(Pagination pagination) {
        mSeecarCountTv.setText("共" + pagination.count + "条");
    }

    @Override
    public void showDeleteSuccess() {

        mySeecarMsgPresenter.getMySeecarMsgList(mToken, mPageIndex, mPageSize, -1);
//        if(deletePos!=-2){
//            mMySeecarList.remove(deletePos);
//            myseecarMsgAdapter.notifyDataSetChanged(mMySeecarList);
//        }
    }

    @Override
    public void showDeleteFailed() {
        CommonUtils.showToast(getContext(), "失败");
    }
}
