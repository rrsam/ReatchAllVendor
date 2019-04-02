package com.reatchall.charan.reatchallVendor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by NaNi on 20/02/18.
 */

public class ViewMoreAboutUsActivity extends AppCompatActivity {

    private static final String TAG = "ViewMoreAboutUsActivity";
    ImageView backArrow;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_view_more);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
