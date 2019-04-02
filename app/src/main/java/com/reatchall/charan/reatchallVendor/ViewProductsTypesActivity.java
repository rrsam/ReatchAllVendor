package com.reatchall.charan.reatchallVendor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.Adapters.ViewProductViewBusinessAdapter;

import java.util.ArrayList;

/**
 * Created by NaNi on 20/02/18.
 */

public class ViewProductsTypesActivity extends AppCompatActivity {

    ImageView backArrow;
    RecyclerView recyclerView;

    ArrayList<String> arrayList;
    ViewProductViewBusinessAdapter viewProductViewBusinessAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_more_view_product);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.viewProduct_rcv);
        recyclerView.setHasFixedSize(true);
        arrayList=new ArrayList<>();
        for(int i=0;i<11;i++){
            arrayList.add("i"+i);
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2,1,false));
        viewProductViewBusinessAdapter=new ViewProductViewBusinessAdapter(getApplicationContext(),arrayList);
        recyclerView.setAdapter(viewProductViewBusinessAdapter);
    }
}
