package com.reatchall.charan.reatchallVendor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.reatchall.charan.reatchallVendor.Adapters.HomeFragmentAdapter;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 08/02/18.
 */

public class ViewAllActivity extends AppCompatActivity {

    private static final String TAG = "ViewAllActivity";

    private FontTextView services,products,individuals;
    ImageView search_tab;

    private ArrayList<Integer> listArray = new ArrayList<Integer>();
    private static final Integer[] list_products= {R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy,R.drawable.ic_dummy};
    private HomeFragmentAdapter homeFragmentAdapter;

    private RecyclerView recyclerView;

    LinearLayout search_bar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        services=(FontTextView)findViewById(R.id.services);
        products=(FontTextView)findViewById(R.id.products);
        individuals=(FontTextView)findViewById(R.id.individuals);
        search_tab=(ImageView)findViewById(R.id.search_tab);
        search_bar=(LinearLayout)findViewById(R.id.search_bar);


        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                services.setBackgroundResource(R.drawable.welcome_blue_view_all);
                products.setBackgroundResource(R.drawable.welcome_blue_light_view_all);
                individuals.setBackgroundResource(R.drawable.welcome_blue_light_view_all);
                search_tab.setBackgroundResource(R.drawable.welcome_blue_light_view_all_search);
                search_bar.setVisibility(View.GONE);

            }
        });
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                products.setBackgroundResource(R.drawable.welcome_blue_view_all);
                services.setBackgroundResource(R.drawable.welcome_blue_light_view_all);
                individuals.setBackgroundResource(R.drawable.welcome_blue_light_view_all);
                search_tab.setBackgroundResource(R.drawable.welcome_blue_light_view_all_search);
                search_bar.setVisibility(View.GONE);

            }
        });

        individuals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                individuals.setBackgroundResource(R.drawable.welcome_blue_view_all);
                services.setBackgroundResource(R.drawable.welcome_blue_light_view_all);
                products.setBackgroundResource(R.drawable.welcome_blue_light_view_all);
                search_tab.setBackgroundResource(R.drawable.welcome_blue_light_view_all_search);
                search_bar.setVisibility(View.GONE);

            }
        });

        search_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_tab.setBackgroundResource(R.drawable.welcome_blue_view_all_search);
                services.setBackgroundResource(R.drawable.welcome_blue_light_view_all);
                products.setBackgroundResource(R.drawable.welcome_blue_light_view_all);
                individuals.setBackgroundResource(R.drawable.welcome_blue_light_view_all);
                search_bar.setVisibility(View.VISIBLE);
            }
        });

        services.performClick();

        recyclerView=(RecyclerView)findViewById(R.id.home_rcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(ViewAllActivity.this,4,1,false));

        homeFragmentAdapter=new HomeFragmentAdapter(ViewAllActivity.this,list_products);
        recyclerView.setAdapter(homeFragmentAdapter);
    }
}
