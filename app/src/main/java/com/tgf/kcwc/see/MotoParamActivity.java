package com.tgf.kcwc.see;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.MotoParamInfoItem;
import com.tgf.kcwc.mvp.model.MotoParamTitleItem;
import com.tgf.kcwc.mvp.presenter.MotoParamPresenter;
import com.tgf.kcwc.mvp.view.MotoParamView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DensityUtils;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Auther: Scott
 * Date: 2016/12/20 0020
 * E-mail:hekescott@qq.com
 */

public class MotoParamActivity extends BaseActivity implements MotoParamView {
    private RecyclerView motoparamRyv;
    private MultiTypeAdapter adapter;
    private List<Object> items = new ArrayList<>();
    private LinearLayout mSuspensionBar;
    private int mSuspensionHeight;
    private LinearLayoutManager mLinearLayoutManager;
    private int mCurrentPosition = 0;
    private TextView snsbarMotoparamtitleTv;
    private PopupWindow popupWindow;
    private Button menubtn;
    private GridView menuGridView;
    private List<MotoParamTitleItem> motoParamTitleItemList;
    private MotoParamPresenter motoParamPresenter;
    private int mId;
    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("参数配置");
    }

    @Override
    protected void setUpViews() {
        menubtn = (Button) findViewById(R.id.motoparam_menu_btn);
        menubtn.setOnClickListener(this);
         mId=   getIntent().getIntExtra(Constants.IntentParams.ID,0);
        motoparamRyv = (RecyclerView) findViewById(R.id.motoparam_ryv);
        mSuspensionBar = (LinearLayout) findViewById(R.id.suspension_bar);
        snsbarMotoparamtitleTv = (TextView) findViewById(R.id.snsbar_motoparamtitle_tv);
        motoParamPresenter = new MotoParamPresenter();
        motoParamPresenter.attachView(this);
        motoParamPresenter.loadMotoParam(mId+"");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motoparam);
        adapter = new MultiTypeAdapter(items);
        adapter.applyGlobalMultiTypePool();
        adapter.register(MotoParamTitleItem.class, new MotoParamTitleProvider());
        adapter.register(MotoParamInfoItem.class, new MotoParamItemProvider());
        motoparamRyv.setAdapter(adapter);
        mLinearLayoutManager = (LinearLayoutManager) motoparamRyv.getLayoutManager();
        motoparamRyv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mSuspensionHeight = mSuspensionBar.getHeight();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (adapter.getItemViewType(mCurrentPosition + 1) == 0) {
                    View view = mLinearLayoutManager.findViewByPosition(mCurrentPosition + 1);
                    if (view != null) {
                        if (view.getTop() <= mSuspensionHeight) {
                            mSuspensionBar.setY(-(mSuspensionHeight - view.getTop()));
                        } else {
                            mSuspensionBar.setY(0);
                        }
                    }
                }

                if (mCurrentPosition != mLinearLayoutManager.findFirstVisibleItemPosition()) {
                    mCurrentPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
                    mSuspensionBar.setY(0);
                    updateSuspensionBar(mCurrentPosition);
                }
            }
        });
    }

    private void updateSuspensionBar(int mCurrentPosition) {

        if (items.get(mCurrentPosition) instanceof MotoParamTitleItem) {
            MotoParamTitleItem motoParamTitleItem = (MotoParamTitleItem) items.get(mCurrentPosition);
            snsbarMotoparamtitleTv.setText(motoParamTitleItem.title);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.motoparam_menu_btn:
                showMenu();
                break;
//            case R.id.baseparam_btn:
//                motoparamRyv.scrollToPosition(0);
//                CommonUtils.showToast(mContext, "baseparam_btn");
//                popupWindow.dismiss();
//                break;
        }
    }

    private void showMenu() {
        if (popupWindow != null) {
            popupWindow.showAtLocation(menubtn, Gravity.TOP,
                    (int) menubtn.getX(), (int) menubtn.getY() - DensityUtils.dp2px(mContext, 20));
            return;
        }
        View contentView = initDialogMenu();
        popupWindow = new PopupWindow(contentView, RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popupWindow
                .setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_green_dialog_bg));
        popupWindow.showAtLocation(menubtn, Gravity.TOP,
                (int) menubtn.getX(), (int) menubtn.getY() - DensityUtils.dp2px(mContext, 20));

    }

    @NonNull
    private View initDialogMenu() {

        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_motoparam, null);
        if(motoParamTitleItemList!=null&&motoParamTitleItemList.size()!=0) {
            menuGridView = (GridView) contentView.findViewById(R.id.dialogparam_menu_gv);

            WrapAdapter motoparamMenuAdapter = new WrapAdapter<MotoParamTitleItem>(mContext,
                    R.layout.griditem_dialog, motoParamTitleItemList) {
                @Override
                public void convert(ViewHolder helper, MotoParamTitleItem item) {
                    TextView textView = helper.getView(R.id.name);
                    textView.setText(item.title);
                }
            };
            menuGridView.setAdapter(motoparamMenuAdapter);
            menuGridView.setOnItemClickListener(menuItemClickListener);
        }
        return contentView;
    }

    private AdapterView.OnItemClickListener menuItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mLinearLayoutManager.scrollToPositionWithOffset(titlpos[position],0);
        }
    };
    private int[] titlpos;

    @Override
    public void showData(List<MotoParamTitleItem> datas) {
        Logger.d(datas.get(0).title);
        motoParamTitleItemList = datas;
        snsbarMotoparamtitleTv.setText(motoParamTitleItemList.get(0).title);
        titlpos = new int[datas.size()];
        for (int i = 0; i < datas.size(); i++) {
            if (i == 0) {
                titlpos[i] = 0;
            } else {
                titlpos[i] = titlpos[i - 1] + datas.get(i - 1).paramInfoItems.size() + 1;
            }
        }


        for (int i = 0; i < datas.size(); i++) {
            items.add(datas.get(i));
            List<MotoParamInfoItem> motoParamInfoItemList = datas.get(i).paramInfoItems;
            for (int j = 0; j < motoParamInfoItemList.size(); j++) {
                items.add(motoParamInfoItemList.get(j));
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onDestroy() {
        motoParamPresenter.detachView();
        super.onDestroy();
    }
}
