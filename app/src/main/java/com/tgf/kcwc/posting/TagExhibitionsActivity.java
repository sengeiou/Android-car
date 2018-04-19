package com.tgf.kcwc.posting;

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
 * Date:2017/9/5 9:52
 * E-mail：fishloveqin@gmail.com
 * 发帖标签展会列表
 */

public class TagExhibitionsActivity extends BaseActivity implements CarDataView<List<TopicTagDataModel>> {

    private ListView mList;
    private WrapAdapter<TopicTagDataModel> mAdapter;
    private CarDataPresenter mPresenter;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.select_exhibitions);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_exhibition_list);
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

            Intent intent = new Intent();
            intent.putExtra(Constants.IntentParams.ID, model.id + "");
            intent.putExtra(Constants.IntentParams.NAME, model.name);
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    @Override
    public void showData(List<TopicTagDataModel> models) {

        mAdapter = new WrapAdapter<TopicTagDataModel>(mContext, models, R.layout.select_tag_exhibition_list_item) {
            @Override
            public void convert(ViewHolder helper, TopicTagDataModel item) {

                TextView tv = helper.getView(R.id.name);
                tv.setText(item.name);

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
        params.put("vehicle_type", "car");
        params.put("city_id", mGlobalContext.cityId + "");
        params.put("token", IOUtils.getToken(mContext));
        return params;
    }
}
