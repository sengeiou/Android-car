package com.tgf.kcwc.see;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.ColorBean;
import com.tgf.kcwc.mvp.presenter.DealerDataPresenter;
import com.tgf.kcwc.mvp.view.DealerDataView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Jenny
 * Date:2017/4/17 18:27
 * E-mail：fishloveqin@gmail.com
 */

public class CarColorConfigActivity extends BaseActivity
                                    implements DealerDataView<List<ColorBean>> {

    private int                    mCarId = -1;

    private DealerDataPresenter    mPresenter;

    private WrapAdapter<ColorBean> mAdapter;

    private ListView               mListView;

    private List<ColorBean>        mDatas = new ArrayList<ColorBean>();

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        findViewById(R.id.titleBar).findViewById(R.id.split).setVisibility(View.VISIBLE);
        setTitleBarBg(R.color.white);
        back.setImageResource(R.drawable.btn_close_selector);
        backEvent(back);
        text.setText("选择展车/现车配置");
        text.setTextColor(mRes.getColor(R.color.text_color12));
        function.setTextResource("确定", R.color.text_color12, 14);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (ColorBean bean : mDatas) {
                    if (bean.isCurrent == 1) {

                        Intent intent = new Intent();
                        intent.putExtra(Constants.IntentParams.DATA, bean);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    }
                }

            }
        });

    }

    @Override
    protected void setUpViews() {
        mListView = (ListView) findViewById(R.id.list);
        mListView.setOnItemClickListener(mItemClickListener);
        mPresenter = new DealerDataPresenter();
        mPresenter.attachView(this);

        mPresenter.getColorsCfgLists(mCarId + "", IOUtils.getToken(mContext));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCarId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        setContentView(R.layout.activity_car_color_config);
    }

    @Override
    public void showData(List<ColorBean> datas) {

        mDatas.clear();
        mDatas.addAll(datas);
        mAdapter = new WrapAdapter<ColorBean>(mContext, R.layout.color_cfg_list_item, mDatas) {

            protected TextView  interiorTv;
            protected ImageView interiorImg;
            protected TextView  appearanceTv;
            protected ImageView appearanceImg;
            protected ImageView selectStatusImg;
            protected TextView  carName;

            @Override
            public void convert(ViewHolder helper, ColorBean item) {

                carName = helper.getView(R.id.carName);
                selectStatusImg = helper.getView(R.id.selectStatusImg);
                appearanceImg = helper.getView(R.id.appearanceImg);
                appearanceTv = helper.getView(R.id.appearanceTv);
                interiorImg = helper.getView(R.id.interiorImg);
                interiorTv = helper.getView(R.id.interiorTv);

                carName.setText(item.seriesName + item.carName);

                appearanceTv.setText("外观  " + item.appearanceColorName);
                interiorTv.setText("内饰  " + item.interiorColorName);

                String appearColorValue = item.appearanceColorValue;
                if(!appearColorValue.startsWith("#")){
                    appearColorValue="#fafafb";
                }
                String splitArrays[] = appearColorValue.split(",");
                Bitmap bitmap = BitmapUtils.getRectColors(splitArrays,
                    BitmapUtils.dp2px(mContext, 60), BitmapUtils.dp2px(mContext, 24),
                    R.color.style_bg4, 1);
                appearanceImg.setImageBitmap(bitmap);

                String interiorColorValue = item.interiorColorValue;
                if(!interiorColorValue.startsWith("#")){
                   interiorColorValue="#f0f0f0";
                }
                splitArrays = interiorColorValue.split(",");
                bitmap = BitmapUtils.getRectColors(splitArrays, BitmapUtils.dp2px(mContext, 60),
                    BitmapUtils.dp2px(mContext, 24), R.color.style_bg4, 1);

                interiorImg.setImageBitmap(bitmap);

                if (item.isCurrent == 1) {
                    selectStatusImg.setVisibility(View.VISIBLE);
                    carName.setSelected(true);
                    appearanceTv.setSelected(true);
                    interiorTv.setSelected(true);
                } else {
                    selectStatusImg.setVisibility(View.GONE);
                    carName.setSelected(false);
                    appearanceTv.setSelected(false);
                    interiorTv.setSelected(false);
                }
            }
        };

        mListView.setAdapter(mAdapter);
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

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            ColorBean bean = (ColorBean) parent.getAdapter().getItem(position);
            bean.isCurrent = 1;
            singleChecked(mDatas, bean);
            mAdapter.notifyDataSetChanged();

        }
    };

    private void singleChecked(List<ColorBean> items, ColorBean cb) {
        for (ColorBean bean : items) {
            if (bean.id != cb.id) {
                bean.isCurrent = 0;
            }
        }

    }
}
