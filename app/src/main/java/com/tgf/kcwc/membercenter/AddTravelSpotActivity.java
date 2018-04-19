package com.tgf.kcwc.membercenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.ContinueBean;
import com.tgf.kcwc.mvp.presenter.ContinuePresenter;
import com.tgf.kcwc.mvp.view.ContinueView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/10.
 */

public class AddTravelSpotActivity extends BaseActivity implements ContinueView {
    private static final int ADDTRAVEL_FINISH = 1001;             //关闭

    private ContinuePresenter mContinuePresenter;
    private ListView mListView;
    private WrapAdapter<ContinueBean.ActivityLineNode> mCommonAdapter;
    private List<ContinueBean.ActivityLineNode> dataList = new ArrayList<>();
    private List<ContinueBean.ActivityLineNode> newDataList = new ArrayList<>();//处理过的列表
    private ContinueBean mContinueBean;
    private ContinueBean mNewContinueBean;                    //处理过的对象
    private ImageButton back;
    private int mThreadId;

    @Override
    protected void setUpViews() {
        mContinuePresenter = new ContinuePresenter();
        mContinuePresenter.attachView(this);
        mListView = (ListView) findViewById(R.id.listView);
        back = (ImageButton) findViewById(R.id.title_bar_back);
        mCommonAdapter = new WrapAdapter<ContinueBean.ActivityLineNode>(mContext,
                R.layout.activity_addtravespot_item, dataList) {
            @Override
            public void convert(ViewHolder helper, final ContinueBean.ActivityLineNode item) {
                final int position = helper.getPosition();
                RelativeLayout num = helper.getView(R.id.num);
                TextView endtext = helper.getView(R.id.endtext);
                TextView continuetext = helper.getView(R.id.continuetext);
                TextView title = helper.getView(R.id.title);
                TextView number = helper.getView(R.id.number);
                title.setText(item.address);
                if (position == 0) {
                    num.setVisibility(View.GONE);
                    endtext.setVisibility(View.VISIBLE);
                    endtext.setText("起");
                } else if (position == dataList.size() - 1) {
                    num.setVisibility(View.GONE);
                    endtext.setVisibility(View.VISIBLE);
                    continuetext.setVisibility(View.VISIBLE);
                    endtext.setText("终");
                } else {
                    continuetext.setVisibility(View.VISIBLE);
                    num.setVisibility(View.VISIBLE);
                    endtext.setVisibility(View.GONE);
                    if (position < 10) {
                        number.setPadding(22, 11, 0, 0);
                    } else {
                        number.setPadding(14, 11, 0, 0);
                    }
                    number.setText(position + "");

                    continuetext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dataList.get(position).isLight == 0) {
                                mNewContinueBean = mContinueBean;
                                newDataList.clear();
                                newDataList.addAll(dataList);
                                for (int i = position - 1; i >= 0; i--) {
                                    if (newDataList.get(i).isLight == 1) {
                                        newDataList.add(i + 1, mContinueBean.data.rideLineNode
                                                .get(mContinueBean.data.rideLineNode.size() - 1));

                                        mNewContinueBean.data.activityLineNode.clear();
                                        mNewContinueBean.data.activityLineNode.addAll(newDataList);
                                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                                        args.put(Constants.IntentParams.DATA, mNewContinueBean);
                                        args.put(Constants.IntentParams.ID, i + 1);
                                        args.put(Constants.IntentParams.ID2,
                                                mContinueBean.data.rideLineNode.get(0));
                                        CommonUtils.startResultNewActivity(
                                                AddTravelSpotActivity.this, args,
                                                ContinueMapActivity.class, ADDTRAVEL_FINISH);
                                        break;
                                    } else {
                                        newDataList.remove(i);
                                    }
                                }
                                // mCommonAdapter.notifyDataSetChanged();
                            } else {
                                CommonUtils.startNewActivity(mContext, ContinueMapActivity.class);
                            }

                        }
                    });
                }

                if (dataList.get(position).isLight == 1) {
                    continuetext.setText("已到达");
                    continuetext.setTextColor(getResources().getColor(R.color.addtraveltexgray));
                    continuetext.setFocusable(false);
                } else {
                    continuetext.setText("续接");
                    continuetext.setTextColor(getResources().getColor(R.color.addtraveltexblue));
                    continuetext.setFocusable(true);
                }

            }
        };
        mListView.setAdapter(mCommonAdapter);
        mContinuePresenter.getMergeLineDetail(IOUtils.getToken(mContext), mThreadId+"");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThreadId = getIntent().getIntExtra(Constants.IntentParams.RIDE_ID, -1);
        setContentView(R.layout.activity_addtravespot);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mContinuePresenter != null) {
            mContinuePresenter.detachView();
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
    public void DetailsSucceed(ContinueBean continueBean) {
        mContinueBean = continueBean;
        dataList.clear();
        dataList.addAll(mContinueBean.data.activityLineNode);
        mCommonAdapter.notifyDataSetChanged();
    }

    @Override
    public void DetaSucceed(BaseBean baseBean) {

    }

    @Override
    public void detailsDataFeated(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null && requestCode == ADDTRAVEL_FINISH) {
                finish();
            }
        }
    }
}
