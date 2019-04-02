package com.reatchall.charan.reatchallVendor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.Adapters.ViewMoreRatingsAdapter;

import java.util.ArrayList;

/**
 * Created by NaNi on 20/02/18.
 */

public class ViewMoreRatingsActivity extends AppCompatActivity {

    private static final String TAG = "ViewMoreRatingsActivity";

    ImageView backArrow;
    RecyclerView recyclerView;

    ArrayList<String> arrayList;
    ViewMoreRatingsAdapter viewMoreRatingsAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings_reviews_view_more);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.reviews_rcv_view_more);
        recyclerView.setHasFixedSize(true);
        arrayList=new ArrayList<>();
        for(int i=0;i<10;i++){
            arrayList.add("i"+i);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewMoreRatingsActivity.this, LinearLayoutManager.VERTICAL, false));
        viewMoreRatingsAdapter=new ViewMoreRatingsAdapter(arrayList,getApplicationContext());
        recyclerView.setAdapter(viewMoreRatingsAdapter);
    }
}
