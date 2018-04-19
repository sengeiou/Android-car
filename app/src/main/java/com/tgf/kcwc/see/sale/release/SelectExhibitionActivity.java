package com.tgf.kcwc.see.sale.release;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.SelectExhibitionModel;
import com.tgf.kcwc.mvp.presenter.ExhibitionPresenter;
import com.tgf.kcwc.mvp.presenter.SelectExhibitionPresenter;
import com.tgf.kcwc.mvp.view.ExhibitionView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/9/13
 * @describle 选择展会
 */
public class SelectExhibitionActivity extends BaseActivity implements ExhibitionView {
    ListView listView;
    AdapterView.OnItemClickListener onItemClickListener;
    WrapAdapter<SelectExhibitionModel.List> mAdapter;
    ArrayList<SelectExhibitionModel.List> mList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_exhibition);
    }

    @Override
    protected void setUpViews() {
        listView = (ListView) findViewById(R.id.list_view);
        mAdapter = new WrapAdapter<SelectExhibitionModel.List>(mContext,mList,R.layout.select_exhibition_item) {
            @Override
            public void convert(ViewHolder helper, SelectExhibitionModel.List item) {
                //展会图片
                helper.setSimpleDraweeViewURL(R.id.item_img, URLUtil.builderImgUrl(item.cover,144,144));
                //展会名
                helper.setText(R.id.item_exhibition_name,item.name);
                //展会时间
                helper.setText(R.id.item_exhibition_time, DateFormatUtil.formatTimePlus2(item.startTime)+"-"+DateFormatUtil.formatTimePlus2(item.endTime));
                //展会地址
                helper.setText(R.id.item_exhibition_address,item.address);
            }
        };
        onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SelectExhibitionModel.List list = mList.get(i);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.IntentParams.DATA2,list);
                Log.e("TAG", list.cover+"onItemClick: "+list.name );
                intent.putExtra(Constants.IntentParams.DATA,bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        };
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(onItemClickListener);

        ExhibitionPresenter exhibitionPresenter = new ExhibitionPresenter();
        exhibitionPresenter.attachView(this);
        KPlayCarApp kPlayCarApp = (KPlayCarApp) getApplication();
        int cityId = kPlayCarApp.cityId;
        exhibitionPresenter.getSelectExhibition(IOUtils.getToken(this),cityId);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        back.setVisibility(View.GONE);
        text.setText("选择展会");
        function.setImageResource(R.drawable.icon_friend_x);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void showExhibitionList(SelectExhibitionModel model) {
        if (model.list.size() != 0 && model.list != null){
            mList.addAll(model.list);
            mAdapter.notifyDataSetChanged(mList);
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
}
