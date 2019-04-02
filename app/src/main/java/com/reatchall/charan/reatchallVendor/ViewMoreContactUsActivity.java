package com.reatchall.charan.reatchallVendor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by NaNi on 21/02/18.
 */

public class ViewMoreContactUsActivity extends AppCompatActivity {
    private static final String TAG = "ViewMoreContactUsActivi";

    ImageView backArrow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us_view_more);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
