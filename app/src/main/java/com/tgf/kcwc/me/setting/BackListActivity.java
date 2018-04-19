package com.tgf.kcwc.me.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.mvp.model.BackListModel;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.BackListDialog;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @anthor noti
 * @time 2017/8/23
 * @describle
 */
public class BackListActivity extends BaseActivity {
    private RelativeLayout empty;//空布局
    private ListView listView;
    private WrapAdapter mAdapter;
    private AdapterView.OnItemClickListener onItemClickListener;
    private ArrayList<BackListModel> mList = new ArrayList<>();
    private int page = 1;
    private boolean isRefresh;

    @Override
    protected void setUpViews() {
        empty = (RelativeLayout) findViewById(R.id.back_list_empty);
        listView = (ListView) findViewById(R.id.back_list);

        initAdapter();
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(onItemClickListener);

        initRefreshLayout(mBGDelegate);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("黑名单");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_list);
    }
    public void initAdapter(){
        mAdapter = new WrapAdapter<BackListModel>(this,mList,R.layout.back_list_item) {
            @Override
            public void convert(ViewHolder helper, final BackListModel item) {
                //头像
//                String avatarUrl = URLUtil.builderImgUrl(item.avatar, 144, 144);
//                SimpleDraweeView avatar = helper.getView(R.id.tagHeaderImg);
//                avatar.setImageURI(avatarUrl);
                //性别
                //昵称
//                helper.setText(R.id.item_name,item);
                TextView outBackList = helper.getView(R.id.item_out_back);
                outBackList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: 2017/8/23 确定移除黑名单dialog 
                        BackListDialog backListDialog = new BackListDialog(BackListActivity.this, item.toString(), new BackListDialog.OnClearItemClickListener() {
                            @Override
                            public void onClearClick() {
                                
                            }
                        });
                    }
                });
            }
        };
        onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                
            }
        };
    }
    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            page = 1;
            isRefresh = true;
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            isRefresh = false;
            page++;
            loadMore();
            return false;
        }
    };

    public void loadMore() {
    }
}
