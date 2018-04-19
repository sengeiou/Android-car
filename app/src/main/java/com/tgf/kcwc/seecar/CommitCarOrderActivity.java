package com.tgf.kcwc.seecar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.SelectBrandActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.mvp.model.CarColor;
import com.tgf.kcwc.mvp.presenter.CommitMotoOrderPresenter;
import com.tgf.kcwc.mvp.view.CommitOrdeView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.Validator;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Auther: Scott
 * Date: 2017/2/20 0020
 * E-mail:hekescott@qq.com
 */

public class CommitCarOrderActivity extends BaseActivity implements CommitOrdeView {
    private static final String KEY_INTENT_SELECTPOS = "slectpos";
    private final String KEY_CARTYPE_CARSERIES = "car_series";
    private final String KEY_CARTYPE_CAR = "car";
    //    private final String             KEY_INTENT_BRANDNAME = "brandname";
    private final int REQUEST_CODE = 100;
    private String brandName;
    private int carId = -1;
    private KPlayCarApp motoApp;
    private TextView commitorderBrandname;
    private TextView commitorderMyplace;
    private EditText commitorderMotodesc;
    private EditText commitorderTel;
    private EditText commitorderNickname;
    private TextView limitTv;
    private String latitude;
    private String longitude;
    //    private String                   addrStr;
    //    private AMapLocationClient       locationClient;
    private CommitMotoOrderPresenter commitMotoOrderPresenter;
    private String token;
    private PoiItem mLoacPoiIem;
    private PlacePoint myPlacePoint;
    //车系id或者车型id
    private int mId = -1;
    private Intent fromIntetn;
    private boolean isFromSeries = false;
    private TextView outlookTv;
    private GridView mOutLookGr;
    private TextView inlookTv;
    private WrapAdapter outColorAdapter;
    private List<CarColor> mOutLookColors;
    private String mCarType;
    private List<CarColor> mInLookColors;
    private WrapAdapter inColorAdapter;
    private GridView mInLookGr;
    private CarColor inLookColor;
    private CarColor outLookColor;
    private int carSeriesId = -1;
    private Account account;
    private CarBean carBean;

    //    private String titleCarName;
    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("我的看车需求");
        backEvent(back);

    }

    @Override
    protected void setUpViews() {
        motoApp = (KPlayCarApp) getApplication();
        fromIntetn = getIntent();
        carBean = fromIntetn.getParcelableExtra(Constants.IntentParams.DATA);
        if (carBean != null) {
            //id 为零没有车型id，则为车系
            isFromSeries = carBean.id == 0;
            StringBuilder tmpStr = new StringBuilder();
            if (!TextUtil.isEmpty(carBean.factoryName)) {
                tmpStr.append(carBean.factoryName);
            }
            if (!TextUtil.isEmpty(carBean.seriesName)) {
                tmpStr.append(" " + carBean.seriesName);
            }
            if (!TextUtil.isEmpty(carBean.name)) {
                tmpStr.append(" " + carBean.name);
            }
            brandName = tmpStr.toString();
        }

//        brandName = ;
        if (TextUtils.isEmpty(brandName)) {
            brandName = "请选择车型";
        }

//        isFromSeries = (fromIntetn.getIntExtra(Constants.IntentParams.INTENT_TYPE, 0) == 1);
        if (isFromSeries) {
            mCarType = KEY_CARTYPE_CARSERIES;
            carSeriesId = carBean.seriesId;
        } else {
            mCarType = KEY_CARTYPE_CAR;
            if (carBean != null) {
                mId = carBean.id;
            }
            carId = mId;
        }
        latitude = motoApp.getLattitude();
        longitude = motoApp.getLongitude();
        token = IOUtils.getToken(this);
        myPlacePoint = new PlacePoint();
        commitMotoOrderPresenter = new CommitMotoOrderPresenter();
        commitMotoOrderPresenter.attachView(this);
        if (carSeriesId != 0 || mId != 0) {
            if (isFromSeries) {
                commitMotoOrderPresenter.getCarOutLookColors(carSeriesId + "", mCarType);
                commitMotoOrderPresenter.getCarInLookColors(carSeriesId + "", mCarType);
            } else {
                commitMotoOrderPresenter.getCarOutLookColors(mId + "", mCarType);
                commitMotoOrderPresenter.getCarInLookColors(mId + "", mCarType);
            }
        }
        commitorderBrandname = (TextView) findViewById(R.id.commitorder_brandname);
        commitorderMyplace = (TextView) findViewById(R.id.commitorder_myplace);
        commitorderMotodesc = (EditText) findViewById(R.id.commitorder_motodesc);
        commitorderTel = (EditText) findViewById(R.id.commitorder_tel);
        commitorderNickname = (EditText) findViewById(R.id.commitorder_nickname);
        limitTv = (TextView) findViewById(R.id.commitorder_limmittv);
        findViewById(R.id.commitorder_commitbtn).setOnClickListener(this);
        findViewById(R.id.commitorder_address_ll).setOnClickListener(this);
        findViewById(R.id.commitorder_lv).setOnClickListener(this);
        outlookTv = (TextView) findViewById(R.id.outlookTv);
        mOutLookGr = (GridView) findViewById(R.id.commitorder_outlook);
        inlookTv = (TextView) findViewById(R.id.inlookTv);
        mInLookGr = (GridView) findViewById(R.id.commitorder_inlook);
        account = IOUtils.getAccount(getContext());
        if (account != null) {
            commitorderTel.setText("" + account.userInfo.tel);
        }
        initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        token = IOUtils.getToken(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commitmoto_order);
    }

    private void initData() {
        if (TextUtil.isEmpty(brandName) || brandName.equals("请选择车型")) {
            outlookTv.setVisibility(View.GONE);
            mOutLookGr.setVisibility(View.GONE);
            inlookTv.setVisibility(View.GONE);
            mInLookGr.setVisibility(View.GONE);
        }else {
            outlookTv.setVisibility(View.VISIBLE);
            mOutLookGr.setVisibility(View.VISIBLE);
            inlookTv.setVisibility(View.VISIBLE);
            mInLookGr.setVisibility(View.VISIBLE);
        }
        commitorderBrandname.setText(brandName);
        commitorderMotodesc.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                this.temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                limitTv.setText(temp.length() + "/500");
            }
        });

        mOutLookGr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CarColor carColor = mOutLookColors.get(position);
                outLookColor = carColor;
                singleChecked(mOutLookColors, carColor);
                outColorAdapter.notifyDataSetChanged(mOutLookColors);
            }
        });
        mInLookGr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CarColor carColor = mInLookColors.get(position);
                inLookColor = carColor;
                singleChecked(mInLookColors, carColor);
                inColorAdapter.notifyDataSetChanged(mInLookColors);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.commitorder_commitbtn:
                String nickname = commitorderNickname.getText().toString();
                String tel = commitorderTel.getText().toString();
                Log.e("TAG", "carId: " + carId + "carSeriesId:" + carSeriesId);
                if (carId == -1 && carSeriesId == -1) {
                    CommonUtils.showToast(this, "请选择车型");
                    return;
                }
                if (!Validator.isMobile(tel)) {
                    CommonUtils.showToast(this, "联系人电话不正确");
                    return;
                }
                if (TextUtils.isEmpty(nickname)) {
                    CommonUtils.showToast(this, "联系人不能为空");
                    return;
                }
                int  inLookColorId =0;
                int  outLookColorId =0;
                if (inLookColor != null) {
                    inLookColorId =inLookColor.id;
                }
                if (outLookColor != null) {
                    outLookColorId =outLookColor.id;
                }
                commitMotoOrderPresenter.commitMotoOrder(token, carId, carSeriesId, inLookColorId,
                        outLookColorId, nickname, commitorderMotodesc.getText().toString() + " ",
                        latitude, longitude, tel);
                break;
            case R.id.commitorder_address_ll:
                Intent intent = new Intent(getContext(), SelectPosActivity.class);
                intent.putExtra(KEY_INTENT_SELECTPOS, myPlacePoint);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.commitorder_lv:
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(
                        Constants.IntentParams.MODULE_TYPE,
                        Constants.ModuleTypes.OWNER_SEE);
                CommonUtils
                        .startNewActivity(
                                CommitCarOrderActivity.this,
                                args,
                                SelectBrandActivity.class);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        //        if (locationClient != null) {
        //            locationClient.onDestroy();
        //            locationClient = null;
        //        }
        commitMotoOrderPresenter.detachView();
        super.onDestroy();

    }

    private int mOrderId;
    private final String INTENT_KEY_ORDER = "order";

    @Override
    public void showSuccess(int orderId) {
        mOrderId = orderId;
        if (mOrderId > 0) {
            //            LatLonPoint latLonPoint = new LatLonPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
            //            mLoacPoiIem = new PoiItem(mOrderId + "", latLonPoint, "", "");

            //            PlacePoint myPlacePoint = new PlacePoint();
            Intent toIntent = new Intent(getContext(), WaittingPriceActivity.class);
            myPlacePoint.myLalng = new LatLng(Double.parseDouble(latitude),
                    Double.parseDouble(longitude));
            toIntent.putExtra(INTENT_KEY_ORDER, myPlacePoint);
            toIntent.putExtra(Constants.IntentParams.ID, mOrderId);
            toIntent.putExtra(Constants.IntentParams.TITLE, brandName);
            startActivity(toIntent);
//            finish();
        } else {
            CommonUtils.showToast(this, "提交订单失败");
        }

    }

    @Override
    public void showCommitOrderFailed(String msg) {

        CommonUtils.showToast(this, msg);
    }

    @Override
    public void showOutLook(List<CarColor> outLookColors) {
        mOutLookColors = outLookColors;
        if (mOutLookColors == null || mOutLookColors.size() == 0) {
            return;
        }
        mOutLookColors.remove(0);
        if (outColorAdapter == null) {
            final int selectedColor = mRes.getColor(R.color.voucher_yellow);
            final int unSelectedColor = mRes.getColor(R.color.style_bg4);
            outColorAdapter = new WrapAdapter<CarColor>(getContext(), R.layout.griditem_inlookcolor,
                    mOutLookColors) {
                @Override
                public void convert(ViewHolder helper, CarColor item) {
                    ImageView lookColorIv = helper.getView(R.id.grid_lookcoloriv);
                    lookColorIv.getWidth();
                    Logger.d("lookColorIv" + lookColorIv.getWidth());
                    ImageView selectedIv = helper.getView(R.id.grid_selectstatus);
                    String colors[] = item.value.split(",");
                    if (item.isSelected) {
                        lookColorIv.setImageBitmap(
                                BitmapUtils.getRectColors(colors, 110, 50, selectedColor, 1));
                        selectedIv.setVisibility(View.VISIBLE);
                    } else {
                        lookColorIv.setImageBitmap(
                                BitmapUtils.getRectColors(colors, 110, 50, unSelectedColor, 1));
                        selectedIv.setVisibility(View.INVISIBLE);
                    }
                    helper.setText(R.id.grid_lookcolorname, item.name);
                }
            };
            mOutLookGr.setAdapter(outColorAdapter);
        } else {
            outColorAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showInLook(List<CarColor> inLookColors) {
        mInLookColors = inLookColors;
        if (mInLookColors == null || mInLookColors.size() == 0) {
            return;
        }
        mInLookColors.remove(0);
        if (inColorAdapter == null) {
            final int selectedColor = mRes.getColor(R.color.voucher_yellow);
            final int unSelectedColor = mRes.getColor(R.color.style_bg4);
            inColorAdapter = new WrapAdapter<CarColor>(getContext(), R.layout.griditem_inlookcolor,
                    mInLookColors) {
                @Override
                public void convert(ViewHolder helper, CarColor item) {
                    ImageView lookColorIv = helper.getView(R.id.grid_lookcoloriv);
                    ImageView selectedIv = helper.getView(R.id.grid_selectstatus);
//                    GradientDrawable drawable = new GradientDrawable();
//                    drawable.setColor(Color.parseColor(item.value));
                    String colors[] = item.value.split(",");
                    if (item.isSelected) {
                        lookColorIv.setImageBitmap(
                                BitmapUtils.getRectColors(colors, 110, 50, selectedColor, 1));
                        selectedIv.setVisibility(View.VISIBLE);
                        selectedIv.setVisibility(View.VISIBLE);
                    } else {
                        lookColorIv.setImageBitmap(
                                BitmapUtils.getRectColors(colors, 110, 50, unSelectedColor, 1));
                        selectedIv.setVisibility(View.INVISIBLE);
                    }
//                    lookColorIv.setBackground(drawable);
                    helper.setText(R.id.grid_lookcolorname, item.name);
                }
            };
            mInLookGr.setAdapter(inColorAdapter);
        } else {
            inColorAdapter.notifyDataSetChanged();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            myPlacePoint = data.getParcelableExtra(KEY_INTENT_SELECTPOS);
            Logger.d("RESULT_OK");
        }

    }

    protected void singleChecked(List<CarColor> items, CarColor item) {
        for (CarColor carColor : items) {
            if (carColor.id != item.id) {
                carColor.isSelected = false;
            } else {
                carColor.isSelected = true;
            }
        }

    }

}
