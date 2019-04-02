package com.reatchall.charan.reatchallVendor.Intro;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.reatchall.charan.reatchallVendor.Login.LoginActivity;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class WelcomeActivityNew extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";

    FontTextView customer,business;

    LinearLayout langLayout;


    ArrayList<String> customerList = new ArrayList<>();
    ArrayList<String> businessList = new ArrayList<>();

    private FloatingActionButton fab;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_new);
        customer=(FontTextView)findViewById(R.id.customer);
        business=(FontTextView)findViewById(R.id.business);

        prefManager = new PrefManager(WelcomeActivityNew.this);
        prefManager.setFirstTimeLaunch(false);


        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customer.setBackgroundResource(R.color.primaryColor);
                business.setBackgroundResource(R.color.grey);
                fab=(FloatingActionButton)findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(WelcomeActivityNew.this, LoginActivity.class));
                        finish();
                    }
                });

                customerList.clear();
                customerList.add("s-UxOkfM2oE");
                customerList.add("HLyiHNBOmNE");
                customerList.add("LxJl0j7n_9g");
                RecyclerView recyclerView=(RecyclerView)findViewById(R.id.list);
                recyclerView.setHasFixedSize(true);


                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(WelcomeActivityNew.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                WelcomeAdapter adapter=new WelcomeAdapter(WelcomeActivityNew.this,customerList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE){
                            fab.show();
                        }
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (dy > 0 ||dy<0 && fab.isShown())
                            fab.hide();
                    }
                });


            }
        });
        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customer.setBackgroundResource(R.color.grey);

                business.setBackgroundResource(R.color.primaryColor);


                businessList.clear();
                businessList.add("zyU3Ju0f4eA");
                businessList.add("nh0jCcSLM3g");
                businessList.add("QNedueCcey0");
                RecyclerView recyclerView=(RecyclerView)findViewById(R.id.list);
                recyclerView.setHasFixedSize(true);


                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(WelcomeActivityNew.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                WelcomeAdapter adapter=new WelcomeAdapter(WelcomeActivityNew.this,businessList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE){
                            fab.show();
                        }
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (dy > 0 ||dy<0 && fab.isShown())
                            fab.hide();
                    }
                });

            }
        });

        customer.performClick();


    }
}
