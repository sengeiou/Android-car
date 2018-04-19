package com.tgf.kcwc.me.integral;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.CityJsonBean;
import com.tgf.kcwc.mvp.model.IntegralConversionGoodDetailsBean;
import com.tgf.kcwc.mvp.model.IntegralExchangeSucceedBean;
import com.tgf.kcwc.mvp.presenter.IntegralConversionSucceedPresenter;
import com.tgf.kcwc.mvp.view.IntegralConversionSucceedView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.GetJsonDataUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.FunctionView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/24.
 * 修改收货地址
 */

public class ModificationAddressActivity extends BaseActivity implements IntegralConversionSucceedView {

    IntegralConversionSucceedPresenter mIntegralConversionSucceedPresenter;
    private LinearLayout city;
    private TextView cityText;
    private EditText name;
    private EditText tel;
    private EditText detailedaddress;
    private ArrayList<CityJsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private Thread thread;
    private boolean isLoaded = false;

    private String cityString = "";
    private  String ID="";
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    isLoaded = true;
                    ShowPickerView();
                    break;

                case MSG_LOAD_FAILED:
                    CommonUtils.showToast(mContext, "系统异常");
                    break;

            }
        }
    };

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("修改地址");
        function.setTextResource(R.string.confirm, R.color.white, 15);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtil.isEmpty(name.getText().toString().trim())) {
                    CommonUtils.showToast(mContext, "请输入收件人姓名");
                    return;
                }
                if (TextUtil.isEmpty(tel.getText().toString().trim())) {
                    CommonUtils.showToast(mContext, "请输入收件人电话");
                    return;
                }
                if (TextUtil.isEmpty(cityString)) {
                    CommonUtils.showToast(mContext, "请选择省市县");
                    return;
                }
                if (TextUtil.isEmpty(detailedaddress.getText().toString().trim())) {
                    CommonUtils.showToast(mContext, "请输入详细地址");
                    return;
                }
                mIntegralConversionSucceedPresenter.getEditAddress(builderParams());
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_modificationaddress);
        ID = getIntent().getStringExtra(Constants.IntentParams.ID);
        mIntegralConversionSucceedPresenter = new IntegralConversionSucceedPresenter();
        mIntegralConversionSucceedPresenter.attachView(this);
        city = (LinearLayout) findViewById(R.id.city);
        cityText = (TextView) findViewById(R.id.citytext);
        name = (EditText) findViewById(R.id.name);
        tel = (EditText) findViewById(R.id.tel);
        detailedaddress = (EditText) findViewById(R.id.detailedaddress);
        city.setOnClickListener(this);
    }


    private Map<String, String> builderParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", IOUtils.getToken(mContext));
        params.put("nickname", name.getText().toString().trim());
        params.put("tel", tel.getText().toString().trim());
        params.put("id", ID);
        params.put("address", cityString + detailedaddress.getText().toString().trim());
        return params;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.city:
                if (isLoaded) {
                    ShowPickerView();
                } else {
                    mHandler.sendEmptyMessage(MSG_LOAD_DATA);
                }
                break;
        }
    }

    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                cityText.setText(tx);
                cityString = tx;
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<CityJsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    public ArrayList<CityJsonBean> parseData(String result) {//Gson 解析
        ArrayList<CityJsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                CityJsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), CityJsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {
        CommonUtils.showToast(mContext, "系统异常");
    }

    @Override
    public void DataBuyProductSucceed(IntegralExchangeSucceedBean baseBean) {

    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, "系统异常");
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
