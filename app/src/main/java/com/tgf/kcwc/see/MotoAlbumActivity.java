package com.tgf.kcwc.see;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2016/12/19 0019
 * E-mail:hekescott@qq.com
 */

public class MotoAlbumActivity extends BaseActivity {
    private ArrayList<String> tmpList = new ArrayList<>();
    private GridView          albumGv;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moto_album);
        WrapAdapter<String> wholemotoAdapter = new WrapAdapter<String>(this, tmpList,
            R.layout.gridview_item_motopics) {
            @Override
            public void convert(ViewHolder helper, String item) {

            }
        };

        albumGv.setAdapter(wholemotoAdapter);
    }

    @Override
    protected void setUpViews() {
        albumGv = (GridView) findViewById(R.id.album_gr);
        for (int i = 0; i < 26; i++) {
            tmpList.add("测试数据");
        }

    }
}
