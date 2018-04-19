package com.tgf.kcwc.tripbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.view.FunctionView;

/**
 * Auther: Scott
 * Date: 2017/5/15 0015
 * E-mail:hekescott@qq.com
 */

public class TripBookAroudActivity extends BaseActivity {

    private RelativeLayout      searchRl;
    private RelativeLayout      tongxRl;
    private RelativeLayout      couponrl;
    private FragmentTransaction tran;
    private TripBookOrgFrag     tripBookOrgFrag;
    private FragmentManager     fm;
    private TripBookCouponFrag  tripBookCouponFrag;
    private TripBookTongxFrag   tripBookTongxFrag;
    private Intent fromIntent;
    private TextView titleBar;
    public int bookLindId;
    @Override
    protected void setUpViews() {
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripbookaround);
        searchRl = (RelativeLayout) findViewById(R.id.tripbook_around_searchrl);
        tongxRl = (RelativeLayout) findViewById(R.id.tripbook_around_tongxrl);
        couponrl = (RelativeLayout) findViewById(R.id.tripbook_around_couponrl);
        titleBar = (TextView) findViewById(R.id.title_bar_text);
        findViewById(R.id.title_bar_back).setOnClickListener(this);

        searchRl.setOnClickListener(this);
        tongxRl.setOnClickListener(this);
        couponrl.setOnClickListener(this);
//        tripBookOrgFrag = new TripBookOrgFrag();
        fm = getSupportFragmentManager();
        fromIntent = getIntent();
        bookLindId = fromIntent.getIntExtra(Constants.IntentParams.ID,0);
        titleBar.setText(fromIntent.getStringExtra(Constants.IntentParams.TITLE));
        initSelectTitle();


    }

    private void initSelectTitle() {
        int type =  fromIntent.getIntExtra(Constants.IntentParams.INTENT_TYPE,1);
        switch (type){
                    case 1:
                        searchRl.performClick();
                        break;
                    case 2:
                        couponrl.performClick();
                        break;
                    case 3:
                        tongxRl.performClick();
                        break;
                    default:
                        break;
                }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tripbook_around_searchrl:
                if (tripBookOrgFrag == null) {
                    tripBookOrgFrag = new TripBookOrgFrag();
                }
                setSelected(view.getId());
                tran = fm.beginTransaction();
                tran.replace(R.id.tripbookaround_fl, tripBookOrgFrag);
                tran.commit();
                break;
            case R.id.tripbook_around_couponrl:
                if (tripBookCouponFrag == null) {
                    tripBookCouponFrag = new TripBookCouponFrag();
                }
                setSelected(view.getId());
                tran = fm.beginTransaction();
                tran.replace(R.id.tripbookaround_fl, tripBookCouponFrag);
                tran.commit();
                break;
            case R.id.tripbook_around_tongxrl:
                setSelected(view.getId());
                if (tripBookTongxFrag == null) {
                    tripBookTongxFrag = new TripBookTongxFrag();
                }
                setSelected(view.getId());
                tran = fm.beginTransaction();
                tran.replace(R.id.tripbookaround_fl, tripBookTongxFrag);
                tran.commit();
                break;
            case  R.id.title_bar_back:
                finish();
                break;
            default:
                break;
        }
    }

    public void setSelected(int selectedId) {
        searchRl.setSelected(R.id.tripbook_around_searchrl == selectedId);
        couponrl.setSelected(R.id.tripbook_around_couponrl == selectedId);
        tongxRl.setSelected(R.id.tripbook_around_tongxrl == selectedId);

    }
}
