package com.tgf.kcwc.posting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.tgf.kcwc.view.GridViewWithHeaderAndFooter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2017/9/5 14:50
 * E-mail：fishloveqin@gmail.com
 * 通过标签参品牌获取模特列表
 */

public class SelectModelByBrandActivity extends BaseActivity implements CarDataView<List<TopicTagDataModel>> {


    protected View split;
    protected GridViewWithHeaderAndFooter grid;
    private String eventId;
    private String mType = "";
    private String brandId = "";
    private CarDataPresenter mPresenter;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("选择模特");
        backEvent(back);
    }

    @Override
    protected void setUpViews() {
        initView();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mType = intent.getStringExtra(Constants.IntentParams.TAG_TYPE);
        eventId = intent.getStringExtra(Constants.IntentParams.ID);
        brandId = intent.getStringExtra(Constants.IntentParams.TAG_BRAND_ID);
        super.setContentView(R.layout.activity_select_model_by_brand);


    }


    private void initView() {
        split = (View) findViewById(R.id.split);
        grid = (GridViewWithHeaderAndFooter) findViewById(R.id.grid);
        grid.setOnItemClickListener(onItemClickListener);
        mPresenter = new CarDataPresenter();
        mPresenter.attachView(this);
        mPresenter.getTopicTagDatas(builderReqParams());
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


    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            TopicTagDataModel model = (TopicTagDataModel) parent.getAdapter().getItem(position);
            model.fromType = mType;
            List<Activity> activities = KPlayCarApp.getActivityStack();

            for (Activity a : activities) {

                if (a instanceof TopicListActivity || a instanceof CommonTagSettingActivity || a instanceof SelectModelByBrandActivity) {

                    a.finish();
                }
            }

            KPlayCarApp.putValue(Constants.KeyParams.SYS_TAG_MODEL_VALUE, model);//全局传递数据

        }
    };

    @Override
    public void showData(List<TopicTagDataModel> models) {


        WrapAdapter<TopicTagDataModel> adapter = new WrapAdapter<TopicTagDataModel>(mContext, R.layout.select_model_by_brand_list_item, models) {

            protected TextView beautyFellownum;
            protected TextView beautyFellowadd;
            protected ImageView modellistIsModeliv;
            protected TextView beautylistModelname;
            protected SimpleDraweeView beautyAvatarIv;
            protected View rootView;

            @Override
            public void convert(ViewHolder helper, TopicTagDataModel item) {

                beautyAvatarIv = helper.getView(R.id.beauty_avatar_iv);
                beautylistModelname = helper.getView(R.id.beautylist_modelname);
                modellistIsModeliv = helper.getView(R.id.modellist_isModeliv);
                beautyFellowadd = helper.getView(R.id.beauty_fellowadd);
                beautyFellownum = helper.getView(R.id.beauty_fellownum);
                beautyAvatarIv.setImageURI(URLUtil.builderImgUrl750(item.cover));
                beautylistModelname.setText(item.title);
            }
        };

        grid.setAdapter(adapter);
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
}
