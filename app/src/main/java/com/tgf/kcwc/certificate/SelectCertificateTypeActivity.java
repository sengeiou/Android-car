package com.tgf.kcwc.certificate;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2017/1/12 10:01
 * E-mail：fishloveqin@gmail.com
 */

public class SelectCertificateTypeActivity extends BaseActivity {

    private GridView              mGridView;
    private WrapAdapter<DataItem> mAdapter;
    private Button                mNextStepBtn;
    private String                title = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = getIntent().getStringExtra(Constants.IntentParams.DATA);
        setContentView(R.layout.activity_select_certificate_type);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText(title + "");
    }

    private List<DataItem> mTypes = new ArrayList<DataItem>();

    @Override
    protected void setUpViews() {

        mGridView = (GridView) findViewById(R.id.gridView);
        mNextStepBtn = (Button) findViewById(R.id.nextStepBtn);
        mNextStepBtn.setOnClickListener(this);
        String[] arrays = mRes.getStringArray(R.array.viewers_type);

        int length = arrays.length;
        for (int i = 0; i < length; i++) {

            DataItem item = new DataItem();
            item.name = arrays[i];
            item.id = i + 1;
            mTypes.add(item);
        }
        mAdapter = new WrapAdapter<DataItem>(mContext, mTypes, R.layout.checkin_type_grid_item) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                TextView textView = helper.getView(R.id.name);
                textView.setText(item.name);
                View v = helper.getConvertView();
                if (item.isSelected) {
                    v.setBackgroundResource(R.drawable.corner_style_btn3);
                    textView.setTextColor(mRes.getColor(R.color.white));
                } else {
                    v.setBackgroundResource(R.drawable.corner_style_btn2);
                    textView.setTextColor(mRes.getColor(R.color.text_color));
                }
            }
        };
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(mItemClickListener);
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            DataItem item = (DataItem) parent.getAdapter().getItem(position);
            if (item.isSelected) {
                item.isSelected = false;
                mNextStepBtn.setEnabled(false);
                mNextStepBtn.setSelected(false);
            } else {
                item.isSelected = true;
                mNextStepBtn.setEnabled(true);
                mNextStepBtn.setSelected(true);
                singleChecked(mTypes, item);
            }
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onClick(View view) {

        int index = getItemIndex(mTypes);

        Map<String, Serializable> args = new HashMap<String, Serializable>();
        args.put(Constants.IntentParams.DATA, index);
        args.put(Constants.IntentParams.TITLE, title);
        CommonUtils.startNewActivity(mContext, args, CheckinActivity.class);

    }
}
