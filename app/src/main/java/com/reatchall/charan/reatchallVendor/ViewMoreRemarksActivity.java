package com.reatchall.charan.reatchallVendor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 21/02/18.
 */

public class ViewMoreRemarksActivity extends AppCompatActivity {
    private static final String TAG = "ViewMoreContactUsActivi";

    ImageView backArrow;
    FontTextView submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remarks_view_more);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        submit=(FontTextView)findViewById(R.id.submit_remark);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
