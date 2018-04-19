package com.tgf.kcwc.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.driv.SelectCityActivity;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.FilterPopwinUtil;
import com.tgf.kcwc.util.GsonUtil;
import com.tgf.kcwc.util.LocationUtil;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/5/3 0003
 * E-mail:hekescott@qq.com
 */

public class SelectAddressActivity extends BaseActivity
        implements TextWatcher, Inputtips.InputtipsListener,
        TextView.OnEditorActionListener, PoiSearch.OnPoiSearchListener {

    private EditText etSearch;
    private String mCity = "重庆";
    private String mKeyWord;
    private boolean isOnpoiSearch;
    private ListView mAddressLv;
    private List<Tip> mTipList;
    private WrapAdapter mTipadapter;
    private Intent fromIntent;
    private LinearLayout mCityLayout;
    private KPlayCarApp app;
    private ArrayList<PoiItem> poiItems = new ArrayList<>();
    private SpHistoryStorageUtil historyStorageUtil;
    private RelativeLayout historyRl;
    private AMapLocationClient locationClient;
    private AMapLocationListener aMapLocationListener;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        app = (KPlayCarApp) getApplication();
        mCity = app.locCityName;
        fromIntent = getIntent();
        initLocation();
        int isNosee = fromIntent.getIntExtra(Constants.IntentParams.ISSEE, 0);
        mCityLayout = (LinearLayout) findViewById(R.id.selectaddress_city);
        if (isNosee == 1) {
            mCityLayout.setVisibility(View.GONE);
        } else {
            mCityLayout.setVisibility(View.VISIBLE);
        }

        historyStorageUtil = SpHistoryStorageUtil.getInstance(getContext(), 20);
        poiItems.addAll(historyStorageUtil.sortHistory());
        historyRl = findViewById(R.id.selecAddress_historyrl);
        findViewById(R.id.tv_clear).setOnClickListener(this);

        FilterPopwinUtil.commonFilterTile(mCityLayout, mCity);
        mCityLayout.setOnClickListener(this);
        etSearch = (EditText) findViewById(R.id.etsearch);
        etSearch.clearFocus();
        etSearch.addTextChangedListener(this);
        etSearch.setOnEditorActionListener(this);
        mAddressLv = (ListView) findViewById(R.id.selectaddress_lv);
        mAddressLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiItem selectPoi = poiItems.get(position);
                Tip tip = new Tip();
                tip.setAdcode(selectPoi.getAdCode());
                tip.setPostion(selectPoi.getLatLonPoint());
                tip.setDistrict(selectPoi.getSnippet());
                tip.setName(selectPoi.getTitle());

                fromIntent.putExtra(Constants.IntentParams.DATA, tip);
                fromIntent.putExtra(Constants.IntentParams.DATA2, mCity);
//                SharedPreferenceUtil.putSearchHistory(getContext(),GsonUtil.objToJson(poiItemsHistory));
                if (!"1".equals(selectPoi.getPoiId())) {
                    historyStorageUtil.save(selectPoi);
                }
                setResult(RESULT_OK, fromIntent);
                finish();
            }
        });
        findViewById(R.id.selectpos_cancl).setOnClickListener(this);
        mTipadapter = new WrapAdapter<PoiItem>(getContext(), R.layout.listitem_selected_address, poiItems) {
            @Override
            public void convert(ViewHolder helper, PoiItem item) {
                item.getLatLonPoint();
//                                item.getDirection();
                helper.setText(R.id.selectaddress_name, item.getTitle());
                helper.setText(R.id.selectaddress_addess, item.getSnippet());
            }
        };
        mAddressLv.setAdapter(mTipadapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectaddress);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    private PoiItem mySelfpoiItem;

    private void initLocation() {
        locationClient = LocationUtil.getGaodeLocationClient(getContext());
        aMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation bdLocation) {
                if (!TextUtils.isEmpty(bdLocation.getCity())) {
                    LatLonPoint latLng = new LatLonPoint(bdLocation.getLatitude(), bdLocation.getLongitude());
                    mySelfpoiItem = new PoiItem("1", latLng, bdLocation.getPoiName(), bdLocation.getAddress());
                    mySelfpoiItem.setAdCode(bdLocation.getAdCode());
                    poiItems.add(0, mySelfpoiItem);
                    mTipadapter.notifyDataSetChanged();
                }
            }
        };
        locationClient.setLocationListener(aMapLocationListener);
        locationClient.startLocation();
    }

    @Override
    public void afterTextChanged(Editable newText) {
        if (!TextUtils.isEmpty(newText)) {
//            InputtipsQuery inputquery = new InputtipsQuery(newText.toString(), mCity);
//            Inputtips inputTips = new Inputtips(this, inputquery);
//            inputTips.setInputtipsListener(this);
//            inputTips.requestInputtipsAsyn();

            mKeyWord = newText.toString();
            doSearchQuery(mKeyWord);

        }
    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
//        mTipList = tipList;
//        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
//
//        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
//                || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
//                && KeyEvent.ACTION_DOWN == event.getAction())) {
//            //处理事件
//
//            if (!TextUtils.isEmpty(etSearch.getText())) {
//                mKeyWord = etSearch.getText().toString();
//                doSearchQuery(mKeyWord);
//            } else {
//                CommonUtils.showToast(this, "请输入关键字");
//            }
//        }
        return false;
    }

    private void doSearchQuery(@Nullable String keyWord) {
        Logger.d("   doSearchQuery;");
        isOnpoiSearch = true;
        int currentPage = 0;
        PoiSearch.Query query = new PoiSearch.Query(keyWord, "", mCity);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();

    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {
                poiItems.clear();
                poiItems.addAll(result.getPois());
                if (poiItems != null && poiItems.size() != 0) {
                    if (mTipadapter == null) {
                        mTipadapter = new WrapAdapter<PoiItem>(getContext(), R.layout.listitem_selected_address, poiItems) {
                            @Override
                            public void convert(ViewHolder helper, PoiItem item) {
                                item.getLatLonPoint();
//                                item.getDirection();
                                helper.setText(R.id.selectaddress_name, item.getTitle());
                                helper.setText(R.id.selectaddress_addess, item.getSnippet());
                            }
                        };
                        mAddressLv.setAdapter(mTipadapter);
                    } else {
                        Logger.d("   item.getAdName();4");
                        mTipadapter.notifyDataSetChanged(poiItems);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectaddress_city:
//                             if(myCitySelectView.getVisibility()!=View.VISIBLE){
//                                 myCitySelectView.setVisibility(View.VISIBLE);
//                             }else {
//                                 myCitySelectView.setVisibility(View.GONE);
//                             }
                Intent toCityActivity = new Intent(mContext, SelectCityActivity.class);
                startActivityForResult(toCityActivity, 100);
                break;
            case R.id.selectpos_cancl:
                finish();
            case R.id.tv_clear:
                historyRl.setVisibility(View.GONE);
                poiItems.clear();
                historyStorageUtil.clear();
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    //选择的城市
                    mCity = data.getStringExtra(Constants.IntentParams.DATA);
                    FilterPopwinUtil.commonFilterTile(mCityLayout, mCity);
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        if (locationClient != null && aMapLocationListener != null) {

            locationClient.unRegisterLocationListener(aMapLocationListener);
            aMapLocationListener = null;
        }
        super.onDestroy();
    }
}
