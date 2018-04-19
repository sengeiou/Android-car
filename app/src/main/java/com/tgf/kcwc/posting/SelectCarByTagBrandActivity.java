package com.tgf.kcwc.posting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.TopicTagDataModel;
import com.tgf.kcwc.mvp.presenter.CarDataPresenter;
import com.tgf.kcwc.mvp.view.CarDataView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2017/9/5 10:52
 * E-mail：fishloveqin@gmail.com
 * 通过发帖子标签参品牌获取车型
 */

public class SelectCarByTagBrandActivity extends BaseActivity implements CarDataView<List<TopicTagDataModel>> {

    private ListView mList;
    private WrapAdapter<TopicTagDataModel> mAdapter;
    private CarDataPresenter mPresenter;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.select_model);
        if (Constants.TopicParams.NEW_CAR.equals(mType)) {
            text.setText("选择新车");
        } else {
            text.setText("选择展车");
        }
        text.setTextColor(mRes.getColor(R.color.white));

    }

    @Override
    protected void setUpViews() {

        mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(onItemClickListener);
        mPresenter = new CarDataPresenter();
        mPresenter.attachView(this);
        mPresenter.getTopicTagDatas(builderReqParams());
    }

    private String eventId;
    private String mType = "";
    private String brandId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mType = intent.getStringExtra(Constants.IntentParams.TAG_TYPE);
        eventId = intent.getStringExtra(Constants.IntentParams.ID);
        brandId = intent.getStringExtra(Constants.IntentParams.TAG_BRAND_ID);
        setContentView(R.layout.activity_tag_model_list);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }

    }


    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            TopicTagDataModel model = (TopicTagDataModel) parent.getAdapter().getItem(position);
            model.fromType = mType;
            List<Activity> activities = KPlayCarApp.getActivityStack();

            for (Activity a : activities) {

                if (a instanceof TopicListActivity || a instanceof CommonTagSettingActivity || a instanceof SelectCarByTagBrandActivity) {

                    a.finish();
                }
            }

            KPlayCarApp.putValue(Constants.KeyParams.SYS_TAG_MODEL_VALUE, model);//全局传递数据

        }
    };

    @Override
    public void showData(List<TopicTagDataModel> models) {

        mAdapter = new WrapAdapter<TopicTagDataModel>(mContext, models, R.layout.select_tag_car_list_item) {
            @Override
            public void convert(ViewHolder helper, TopicTagDataModel item) {

                TextView tv = helper.getView(R.id.name);
                tv.setText(item.title);

                SimpleDraweeView simpleDraweeView = helper.getView(R.id.img);
                simpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 270, 203)));

            }
        };
        mList.setAdapter(mAdapter);

    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        dismissLoadingDialog();
    }

    @Override
    public Context getContext() {
        return mContext;
    }


    private Map<String, String> builderReqParams() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("parent_id", brandId);
        params.put("vehicle_type", "car");
        params.put("event_id", eventId);
        params.put("from_type", mType);
        params.put("token", IOUtils.getToken(mContext));
        return params;
    }
}
