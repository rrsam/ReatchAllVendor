package com.reatchall.charan.reatchallVendor;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.Adapters.SliderAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import fr.arnaudguyon.smartfontslib.FontTextView;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by NaNi on 08/02/18.
 */

public class ViewBusinessActivity extends AppCompatActivity {

    private static final String TAG = "ViewBusinessActivity";

    FontTextView toolbar_title;
    ImageView backArrow;


    //Slider
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN= {R.drawable.user_login,R.drawable.user_login,R.drawable.user_login,R.drawable.user_login,R.drawable.user_login};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_business_new);

        toolbar_title=(FontTextView)findViewById(R.id.title_toolbar);
        backArrow=(ImageView)findViewById(R.id.back_arrow);

        initSlider();

    }


    private void initSlider(){
        for(int i=0;i<XMEN.length;i++)
            XMENArray.add(XMEN[i]);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SliderAdapter(ViewBusinessActivity.this,XMENArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
    }




}
