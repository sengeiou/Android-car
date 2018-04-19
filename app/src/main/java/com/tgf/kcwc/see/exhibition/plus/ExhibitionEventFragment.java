package com.tgf.kcwc.see.exhibition.plus;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.ExhibitEvent;
import com.tgf.kcwc.see.exhibition.ExhibitionEventDetailActivity;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.URLUtil;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/9/18
 * @describle
 */
public class ExhibitionEventFragment extends BaseFragment {
    private ListView listView;
    private WrapAdapter<ExhibitEvent> eventAdapter;
    private ArrayList<ExhibitEvent> eventList = new ArrayList<>();

    public ExhibitionEventFragment() {
    }

    public ExhibitionEventFragment(ArrayList<ExhibitEvent> eventList) {
        super();
        this.eventList.addAll(eventList);
    }

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_exhibition_event;
    }

    @Override
    protected void initView() {
        listView = findView(R.id.list_view);
        eventAdapter = new WrapAdapter<ExhibitEvent>(getActivity(),eventList,R.layout.listviewitem_exhibitplace_event) {
            @Override
            public void convert(ViewHolder helper, ExhibitEvent item) {
                helper.setText(R.id.event_title, item.title);
                if (DateFormatUtil.getTime(item.endTime) < System.currentTimeMillis()) {
                    helper.setText(R.id.listviewitem_visitors, "已结束");
                } else {
                    helper.setText(R.id.listviewitem_visitors, "开始时间:  " + item.startTime);
                }
                String activtyCoverUrl = URLUtil.builderImgUrl(item.cover, 360, 360);
                helper.setSimpleDraweeViewURL(R.id.listitemt_activtyiv, activtyCoverUrl);
            }
        };
        listView.setAdapter(eventAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, ExhibitionEventDetailActivity.class);
                intent.putExtra(Constants.IntentParams.ID,eventList.get(i).id);
                startActivity(intent);
            }
        });
    }
}
