package com.reatchall.charan.reatchallVendor.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.Adapters.SliderAdapter;
import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by anupamchugh on 10/12/15.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN= {R.drawable.user_login,R.drawable.user_register,R.drawable.logonew,R.drawable.logonew,R.drawable.logonew};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();

    ImageView nextBanner,previousBanner;
    int next=0,previous=0;

    private static final String TAG = "HomeFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
        *
        * image slider
        *
        * */

        nextBanner=(ImageView)view.findViewById(R.id.nextBanner);
        previousBanner=(ImageView)view.findViewById(R.id.previousBanner);



        XMENArray= new ArrayList<>();
        for(int i=0;i<XMEN.length;i++)
            XMENArray.add(XMEN[i]);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(new SliderAdapter(getActivity(),XMENArray));
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);


        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                Log.e(TAG, "onPageSelected: "+position );
                previous=next;
                next=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        nextBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(next+1==XMEN.length){
                    next=-1;
                    previous=XMEN.length-1;
                }

                mPager.setCurrentItem(next+1,true);
            }
        });

        previousBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPager.setCurrentItem(next-1,true);
            }
        });


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
